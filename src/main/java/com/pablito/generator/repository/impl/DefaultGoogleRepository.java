package com.pablito.generator.repository.impl;

import com.pablito.generator.factory.GeoLocalizationFactory;
import com.pablito.generator.model.google.GoogleAddressComponentModel;
import com.pablito.generator.model.google.GoogleGeoLocalizationModel;
import com.pablito.generator.model.google.GoogleGeoLocalizationResponseModel;
import com.pablito.generator.repository.GoogleRepository;
import com.pablito.generator.util.GoogleAddressComponentExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    @Value("${google.api.address.type.locality}")
    private String LOCALITY_TYPE;
    @Value("${google.api.address.type.administration1}")
    private String ADMINISTRATION1_TYPE;
    @Value("${google.api.address.type.administration2}")
    private String ADMINISTRATION2_TYPE;

    private GeoLocalizationFactory geoLocalizationFactory;
    private GoogleAddressComponentExtractor googleAddressComponentExtractor;

    @Autowired
    public DefaultGoogleRepository(final GeoLocalizationFactory geoLocalizationFactory,
                                   final GoogleAddressComponentExtractor googleAddressComponentExtractor) {
        this.geoLocalizationFactory = geoLocalizationFactory;
        this.googleAddressComponentExtractor = googleAddressComponentExtractor;
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
                        .filter(checkIfWithinCity(cityParam))
        ).repeat().take(sizeParam);
    }

    private Predicate<GoogleGeoLocalizationModel> checkIfWithinCity(final String cityParam) {
        return t -> {
            final String onlyCity = cityParam.split(",")[0];

            return googleAddressComponentExtractor.extractAddressComponent(t, LOCALITY_TYPE).map(j -> j.getLong_name()).orElse("").contains(onlyCity)
                    || googleAddressComponentExtractor.extractAddressComponent(t, ADMINISTRATION1_TYPE).map(j -> j.getLong_name()).orElse("").contains(onlyCity)
                    || googleAddressComponentExtractor.extractAddressComponent(t, ADMINISTRATION2_TYPE).map(j -> j.getLong_name()).orElse("").contains(onlyCity);
        };
    };
}
