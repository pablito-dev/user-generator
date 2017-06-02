package com.pablito.generator.handler;

import com.pablito.generator.factory.GeoLocalizationFactory;
import com.pablito.generator.factory.UserFactory;
import com.pablito.generator.model.domain.UserModel;
import com.pablito.generator.model.google.GoogleGeoLocalizationModel;
import com.pablito.generator.model.google.GoogleGeoLocalizationResponseModel;
import com.pablito.generator.model.uinames.UiNamesUserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;


/**
 * Created by pavel_000 on 27/05/2017.
 */
@Component
public class GeneratorHandler {
    private static final String GOOGLE_API_URL = "https://maps.googleapis.com/maps/api";
    private static final String GOOGLE_GEO_ENDPOINT = "/geocode/json";
    private static final String GOOGLE_ADDRESS_PARAM = "?address=";
    private static final String GOOGLE_REVERSE_PARAM = "?latlng=";
    private static final String GOOGLE_LANG_PARAM = "&language=en";

    private static final String UINAMES_API_URL = "https://uinames.com/api/";
    private static final String UINAMES_AMOUNT_PARAM = "?amount=";

    private UserFactory userFactory;
    private GeoLocalizationFactory geoLocalizationFactory;

    @Autowired
    public GeneratorHandler(final UserFactory userFactory, final GeoLocalizationFactory geoLocalizationFactory) {
        this.userFactory = userFactory;
        this.geoLocalizationFactory = geoLocalizationFactory;
    }

    public Mono<ServerResponse> generateUsers(final ServerRequest request) {
        final Integer sizeParam = request.queryParam("amount").isPresent() ? Integer.parseInt(request.queryParam("amount").get()) : 5;
        final String countryParam = request.queryParam("country").orElse("England");
        final String domainParam = request.queryParam("domain").orElse("example.io");

        return request.queryParam("city").map(cityParam -> {
            final Mono<GoogleGeoLocalizationModel> object = WebClient.create(GOOGLE_API_URL)
                    .get()
                    .uri(GOOGLE_GEO_ENDPOINT + GOOGLE_ADDRESS_PARAM + cityParam + GOOGLE_LANG_PARAM)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(GoogleGeoLocalizationResponseModel.class)
                    .filter(t -> t.getResults().size() > 0)
                    .map(i -> i.getResults().get(0));


            final Flux<GoogleGeoLocalizationModel> temp = object.flatMapMany(i ->
                    WebClient.create(GOOGLE_API_URL)
                            .get()
                            .uri(GOOGLE_GEO_ENDPOINT + GOOGLE_REVERSE_PARAM +
                                    geoLocalizationFactory.createRandomGeographicPointWithinBounds(i.getGeometry().getBounds()))
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(GoogleGeoLocalizationResponseModel.class)
                            .filter(t -> t.getResults().size() > 0)
                            .map(t -> t.getResults().get(0))
                            .filter(t -> t.getFormatted_address().contains(cityParam))
            ).repeat().take(sizeParam);

            final Flux<UiNamesUserModel> users = WebClient.create(UINAMES_API_URL)
                    .get()
                    .uri(UINAMES_AMOUNT_PARAM + sizeParam + "&region=" + countryParam + "&ext")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToFlux(UiNamesUserModel.class);

            final Flux<UserModel> result = Flux.zip(temp, users, (i, j) -> userFactory.createUser(i, j, domainParam));

            return ok().body(result, UserModel.class);
        }).orElse(badRequest().build());
    }
}
