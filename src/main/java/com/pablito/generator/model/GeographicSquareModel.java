package com.pablito.generator.model;

/**
 * Created by pavel_000 on 28/05/2017.
 */
public class GeographicSquareModel {
    private GeographicPointModel northeast;
    private GeographicPointModel southwest;

    public GeographicPointModel getNortheast() {
        return northeast;
    }

    public void setNortheast(final GeographicPointModel northeast) {
        this.northeast = northeast;
    }

    public GeographicPointModel getSouthwest() {
        return southwest;
    }

    public void setSouthwest(final GeographicPointModel southwest) {
        this.southwest = southwest;
    }

    public double getDeltaLongitude() {
        return southwest.getLongitudeDifference(northeast);
    }
    public double getDeltaLatitude() {
        return southwest.getLatitudeDifference(northeast);
    }
    public double getLowerLeftLongitude() {
        return southwest.getLng();
    }
    public double getLowerLeftLatitude() {
        return southwest.getLat();
    }
}
