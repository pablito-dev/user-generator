package com.pablito.generator.model;

/**
 * Created by pavel_000 on 28/05/2017.
 */
public class GeographicSquare {
    private GeographicPoint northeast;
    private GeographicPoint southwest;

    public GeographicPoint getNortheast() {
        return northeast;
    }

    public void setNortheast(GeographicPoint northeast) {
        this.northeast = northeast;
    }

    public GeographicPoint getSouthwest() {
        return southwest;
    }

    public void setSouthwest(GeographicPoint southwest) {
        this.southwest = southwest;
    }
}
