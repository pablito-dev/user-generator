package com.pablito.generator.util;

import com.pablito.generator.model.google.GoogleAddressComponentModel;
import com.pablito.generator.model.google.GoogleGeoLocalizationModel;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by pavel_000 on 06/06/2017.
 */
@Component
public class GoogleAddressComponentExtractor {

    public Optional<GoogleAddressComponentModel> extractAddressComponent(final GoogleGeoLocalizationModel model, final String type) {
        return model.getAddress_components().stream().filter(filterAddressComponent(type)).findFirst();
    }

    private Predicate<GoogleAddressComponentModel> filterAddressComponent(final String type) {
        return i -> i.getTypes().stream().anyMatch(t -> t.equals(type));
    }
}
