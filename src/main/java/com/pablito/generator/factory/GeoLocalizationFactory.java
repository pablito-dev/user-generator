package com.pablito.generator.factory;

import com.pablito.generator.model.domain.geography.GeographicPointModel;
import com.pablito.generator.model.domain.geography.GeographicSquareModel;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by pavel_000 on 01/06/2017.
 */
@Component
public class GeoLocalizationFactory {
    public String createRandomGeographicPointWithinBounds(final GeographicSquareModel bounds) {
        final double deltaLong = bounds.getDeltaLatitude();
        final double deltaLat = bounds.getDeltaLongitude();

        return new GeographicPointModel(
                bounds.getLowerLeftLatitude() + deltaLat/2 * ThreadLocalRandom.current().nextDouble(Double.MIN_VALUE, 1.0d),
                bounds.getLowerLeftLongitude() + deltaLong/2 * ThreadLocalRandom.current().nextDouble(Double.MIN_VALUE, 1.0d)
        ).toString();
    }
}
