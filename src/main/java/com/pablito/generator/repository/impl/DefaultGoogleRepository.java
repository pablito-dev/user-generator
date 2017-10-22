package com.pablito.generator.repository.impl;

import com.pablito.generator.factory.GeoLocalizationFactory;
import com.pablito.generator.model.google.GoogleAddressComponentModel;
import com.pablito.generator.model.google.GoogleGeoLocalizationModel;
import com.pablito.generator.model.google.GoogleGeoLocalizationResponseModel;
import com.pablito.generator.repository.GoogleRepository;
import com.pablito.generator.strategy.GeoLocalizationStrategy;
import com.pablito.generator.util.GoogleAddressComponentExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by pavel_000 on 02/06/2017.
 */
@Repository
public class DefaultGoogleRepository implements GoogleRepository {
    @Value("${google.api.url}")
    private String GOOGLE_API_URL;
    @Value("${google.api.endpoint}")
    private String GOOGLE_GEO_ENDPOINT;
    @Value("${google.api.param.address}")
    private String GOOGLE_ADDRESS_PARAM;
    @Value("${google.api.param.latlng}")
    private String GOOGLE_REVERSE_PARAM;
    @Value("${google.api.param.lang}")
    private String GOOGLE_LANG_PARAM;
    @Value("${google.api.param.key}")
    private String API_KEY;

    private GeoLocalizationFactory geoLocalizationFactory;
    private GeoLocalizationStrategy geoLocalizationStrategy;

    @Autowired
    public DefaultGoogleRepository(final GeoLocalizationFactory geoLocalizationFactory,
                                   final GeoLocalizationStrategy geoLocalizationStrategy) {
        this.geoLocalizationFactory = geoLocalizationFactory;
        this.geoLocalizationStrategy = geoLocalizationStrategy;
    }

    @Override
    public Mono<GoogleGeoLocalizationModel> getGeoLocalizationForCityName(final String cityParam, final String key) {
        return WebClient.create(GOOGLE_API_URL)
                .get()
                .uri(GOOGLE_GEO_ENDPOINT + GOOGLE_ADDRESS_PARAM + cityParam + GOOGLE_LANG_PARAM + API_KEY + key)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(GoogleGeoLocalizationResponseModel.class)
                .filter(t -> t.getResults().size() > 0)
                .map(i -> i.getResults().get(0));
    }

    @Override
    public Flux<GoogleGeoLocalizationModel> getRandomGeoLocalizationsWithinCity(final String cityParam
            , final Integer sizeParam, final Mono<GoogleGeoLocalizationModel> startingPoint, final String key) {
        return startingPoint.flatMapMany(i ->
                WebClient.create(GOOGLE_API_URL)
                        .get()
                        .uri(GOOGLE_GEO_ENDPOINT + GOOGLE_REVERSE_PARAM +
                                geoLocalizationFactory.createRandomGeographicPointWithinBounds(i.getGeometry().getBounds()) + API_KEY + key)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(GoogleGeoLocalizationResponseModel.class)
                        .filter(t -> t.getResults().size() > 0)
                        .map(t -> t.getResults().get(0))
                        .filter(geoLocalizationStrategy.checkIfWithinCity(cityParam))
        ).repeat(sizeParam * 15).take(sizeParam);
    }
}
