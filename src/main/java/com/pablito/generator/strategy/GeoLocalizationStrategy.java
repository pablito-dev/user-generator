package com.pablito.generator.strategy;

import com.pablito.generator.model.google.GoogleGeoLocalizationModel;

import java.util.function.Predicate;

/**
 * Created by Paweł Nowak on 22/10/2017.
 */
public interface GeoLocalizationStrategy {
    Predicate<GoogleGeoLocalizationModel> checkIfWithinCity(final String cityName);
}
