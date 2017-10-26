package com.pablito.generator.repository.impl;

import com.pablito.generator.exception.GoogleRequestDeniedException;
import com.pablito.generator.factory.GeoLocalizationFactory;
import com.pablito.generator.model.google.GoogleGeoLocalizationModel;
import com.pablito.generator.model.google.GoogleGeoLocalizationResponseModel;
import com.pablito.generator.repository.GoogleRepository;
import com.pablito.generator.service.impl.ApplicationPropertiesService;
import com.pablito.generator.strategy.GeoLocalizationStrategy;
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
public class DefaultGoogleRepository implements GoogleRepository {
    private final GeoLocalizationFactory geoLocalizationFactory;
    private final GeoLocalizationStrategy geoLocalizationStrategy;
    private final ApplicationPropertiesService applicationPropertiesService;

    @Autowired
    public DefaultGoogleRepository(final GeoLocalizationFactory geoLocalizationFactory,
                                   final GeoLocalizationStrategy geoLocalizationStrategy,
                                   final ApplicationPropertiesService applicationPropertiesService) {
        this.geoLocalizationFactory = geoLocalizationFactory;
        this.geoLocalizationStrategy = geoLocalizationStrategy;
        this.applicationPropertiesService = applicationPropertiesService;
    }

    @Override
    public Mono<GoogleGeoLocalizationModel> getGeoLocalizationForCityName(final String cityParam, final String key) {
        return WebClient.create(applicationPropertiesService.getGoogelAPIUrl())
                .get()
                .uri(applicationPropertiesService.getGoogleGeoEndpoint() + applicationPropertiesService.getGoogleAddressParam()
                        + cityParam + applicationPropertiesService.getGoogleLangParam()
                        + applicationPropertiesService.getGoogleAPIKey() + key)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(GoogleGeoLocalizationResponseModel.class)
                .map(i -> {
                    if (i.getStatus().equalsIgnoreCase(applicationPropertiesService.getGoogleAccessDeniedStatus())) {
                        throw new GoogleRequestDeniedException();
                    }
                    return i.getResults().get(0);
                });
    }

    @Override
    public Flux<GoogleGeoLocalizationModel> getRandomGeoLocalizationsWithinCity(final String cityParam
            , final Integer sizeParam, final Mono<GoogleGeoLocalizationModel> startingPoint, final String key) {
        return startingPoint.flatMapMany(i ->
                WebClient.create(applicationPropertiesService.getGoogelAPIUrl())
                        .get()
                        .uri(applicationPropertiesService.getGoogleGeoEndpoint() + applicationPropertiesService.getGoogleReverseParam()
                                + geoLocalizationFactory.createRandomGeographicPointWithinBounds(i.getGeometry().getBounds())
                                + applicationPropertiesService.getGoogleAPIKey() + key)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(GoogleGeoLocalizationResponseModel.class)
                        .filter(t -> t.getResults().size() > 0)
                        .map(t -> t.getResults().get(0))
                        .filter(geoLocalizationStrategy.checkIfWithinCity(cityParam))
        ).repeat(sizeParam * 15).take(sizeParam);
    }
}
