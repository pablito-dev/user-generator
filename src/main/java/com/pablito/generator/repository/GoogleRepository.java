package com.pablito.generator.repository;

import com.pablito.generator.model.google.GoogleGeoLocalizationModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by pavel_000 on 02/06/2017.
 */
public interface GoogleRepository {
    Mono<GoogleGeoLocalizationModel> getGeoLocalizationForCityName(final String cityParam);

    Flux<GoogleGeoLocalizationModel> getRandomGeoLocalizationsWithinCity(final String cityParam
            , final Integer sizeParam, final Mono<GoogleGeoLocalizationModel> startingPoint);
}
