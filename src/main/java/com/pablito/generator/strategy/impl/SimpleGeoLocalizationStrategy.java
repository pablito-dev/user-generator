package com.pablito.generator.strategy.impl;

import com.pablito.generator.model.google.GoogleAddressComponentModel;
import com.pablito.generator.model.google.GoogleGeoLocalizationModel;
import com.pablito.generator.strategy.GeoLocalizationStrategy;
import com.pablito.generator.util.GoogleAddressComponentExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by Paweł Nowak on 22/10/2017.
 */
@Component
public class SimpleGeoLocalizationStrategy implements GeoLocalizationStrategy {
    @Value("${google.api.address.type.locality}")
    private String LOCALITY_TYPE;
    @Value("${google.api.address.type.administration1}")
    private String ADMINISTRATION1_TYPE;
    @Value("${google.api.address.type.administration2}")
    private String ADMINISTRATION2_TYPE;
    @Value("${google.api.address.type.route}")
    private String ROUTE_TYPE;

    private GoogleAddressComponentExtractor googleAddressComponentExtractor;

    @Autowired
    public SimpleGeoLocalizationStrategy(final GoogleAddressComponentExtractor googleAddressComponentExtractor) {
        this.googleAddressComponentExtractor = googleAddressComponentExtractor;
    }

    @Override
    public Predicate<GoogleGeoLocalizationModel> checkIfWithinCity(final String cityName) {
        return t -> {
            final Optional<String> routeValue = googleAddressComponentExtractor.extractAddressComponent(t, ROUTE_TYPE)
                    .map(GoogleAddressComponentModel::getLong_name);

            return !routeValue.orElse("Unnamed Road").equalsIgnoreCase("Unnamed Road");
        };
    }
}
