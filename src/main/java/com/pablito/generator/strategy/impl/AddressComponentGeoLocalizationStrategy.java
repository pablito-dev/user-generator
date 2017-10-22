package com.pablito.generator.strategy.impl;

import com.pablito.generator.model.google.GoogleAddressComponentModel;
import com.pablito.generator.model.google.GoogleGeoLocalizationModel;
import com.pablito.generator.strategy.GeoLocalizationStrategy;
import com.pablito.generator.util.GoogleAddressComponentExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by Paweł Nowak on 22/10/2017.
 */
public class AddressComponentGeoLocalizationStrategy implements GeoLocalizationStrategy{
    @Value("${google.api.address.type.route}")
    private String ROUTE_TYPE;

    private GoogleAddressComponentExtractor googleAddressComponentExtractor;

    @Autowired
    public AddressComponentGeoLocalizationStrategy(final GoogleAddressComponentExtractor googleAddressComponentExtractor) {
        this.googleAddressComponentExtractor = googleAddressComponentExtractor;
    }

    @Override
    public Predicate<GoogleGeoLocalizationModel> checkIfWithinCity(final String cityName) {
        return t -> {
            final String onlyCity = cityName.split(",")[0];
            final Optional<String> routeValue = googleAddressComponentExtractor.extractAddressComponent(t, ROUTE_TYPE)
                    .map(GoogleAddressComponentModel::getLong_name);

            return t.getAddress_components().stream().anyMatch(i -> i.getLong_name().contains(onlyCity)) &&
                    !routeValue.orElse("Unnamed Road").equalsIgnoreCase("Unnamed Road");
        };
    }
}
