package com.pablito.generator.repository.impl;

import com.pablito.generator.factory.GeoLocalizationFactory;
import com.pablito.generator.model.google.GoogleGeoLocalizationModel;
import com.pablito.generator.model.google.GoogleGeoLocalizationResponseModel;
import com.pablito.generator.repository.GoogleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by pavel_000 on 02/06/2017.
 */
@Repository
public class DefaultGoogleRepository implements GoogleRepository{
    private static final String GOOGLE_API_URL = "https://maps.googleapis.com/maps/api";
    private static final String GOOGLE_GEO_ENDPOINT = "/geocode/json";
    private static final String GOOGLE_ADDRESS_PARAM = "?address=";
    private static final String GOOGLE_REVERSE_PARAM = "?latlng=";
    private static final String GOOGLE_LANG_PARAM = "&language=en";

    private GeoLocalizationFactory geoLocalizationFactory;

    @Autowired
    public DefaultGoogleRepository(final GeoLocalizationFactory geoLocalizationFactory) {
        this.geoLocalizationFactory = geoLocalizationFactory;
    }

    @Override
    public Mono<GoogleGeoLocalizationModel> getGeoLocalizationForCityName(final String cityParam) {
        return WebClient.create(GOOGLE_API_URL)
                .get()
                .uri(GOOGLE_GEO_ENDPOINT + GOOGLE_ADDRESS_PARAM + cityParam + GOOGLE_LANG_PARAM)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(GoogleGeoLocalizationResponseModel.class)
                .filter(t -> t.getResults().size() > 0)
                .map(i -> i.getResults().get(0));
    }

    @Override
    public Flux<GoogleGeoLocalizationModel> getRandomGeoLocalizationsWithinCity(final String cityParam
            , final Integer sizeParam, final Mono<GoogleGeoLocalizationModel> startingPoint) {
        return startingPoint.flatMapMany(i ->
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
    }
}
