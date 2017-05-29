package com.pablito.generator.handler;

import com.pablito.generator.model.GeographicPointModel;
import com.pablito.generator.model.GeographicSquareModel;
import com.pablito.generator.model.GoogleGeolocalizationModel;
import com.pablito.generator.model.GoogleGeolocalizationResponseModel;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;
import java.util.Random;


/**
 * Created by pavel_000 on 27/05/2017.
 */
@Component
public class GeneratorHandler {
    private static final String GOOGLE_API_URL = "https://maps.googleapis.com/maps/api";
    private static final String GOOGLE_GEO_ENDPOINT = "/geocode/json";
    private static final String GOOGLE_ADDRESS_PARAM = "?address=";
    private static final String GOOGLE_REVERSE_PARAM = "?latlng=";

    public Mono<ServerResponse> generateUsers(final ServerRequest request) {
        final Optional<String> cityParam = request.queryParam("city");
        final String sizeParam = request.queryParam("amount").orElse("10");

        if (cityParam.isPresent()) {
            final Mono<GoogleGeolocalizationModel> object = WebClient.create(GOOGLE_API_URL)
                    .get()
                    .uri(GOOGLE_GEO_ENDPOINT + GOOGLE_ADDRESS_PARAM + cityParam.get())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(GoogleGeolocalizationResponseModel.class)
                    .log()
                    .map(i -> i.getResults().get(0));


            final Flux<GoogleGeolocalizationModel> temp = object.flatMapMany(i ->
                WebClient.create(GOOGLE_API_URL)
                        .get()
                        .uri(GOOGLE_GEO_ENDPOINT + GOOGLE_REVERSE_PARAM + generateRandomGeographicPointWithinBounds(i.getGeometry().getBounds()))
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(GoogleGeolocalizationResponseModel.class)
                        .map(t -> t.getResults().get(0))
            ).repeat();

            return ok().body(temp, GoogleGeolocalizationModel.class);
        }
        else {
            return badRequest().build();
        }
    }

    private String generateRandomGeographicPointWithinBounds(final GeographicSquareModel bounds) {
        final double deltaLong = bounds.getDeltaLatitude();
        final double deltaLat = bounds.getDeltaLongitude();
        final Random randomGenerator = new Random();
        String kek = new GeographicPointModel(
                bounds.getLowerLeftLatitude() + deltaLat + randomGenerator.nextDouble(),
                bounds.getLowerLeftLongitude() + deltaLong + randomGenerator.nextDouble()
        ).toString();
        System.out.println("Generating: " + kek);
        return kek;

    }
}
