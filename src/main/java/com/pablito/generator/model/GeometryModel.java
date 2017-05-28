package com.pablito.generator.model;

/**
 * Created by pavel_000 on 28/05/2017.
 */
class GeometryModel {
    private GeographicSquareModel bounds;
    private GeographicSquareModel viewport;
    private String location_type;
    private GeographicPointModel location;

    public GeographicSquareModel getGeographicSquare() {
        return bounds;
    }

    public void setBounds(final GeographicSquareModel bounds) {
        this.bounds = bounds;
    }

    public GeographicSquareModel getViewport() {
        return viewport;
    }

    public void setViewport(final GeographicSquareModel viewport) {
        this.viewport = viewport;
    }

    public String getLocation_type() {
        return location_type;
    }

    public void setLocation_type(final String location_type) {
        this.location_type = location_type;
    }

    public GeographicPointModel getLocation() {
        return location;
    }

    public void setLocation(final GeographicPointModel location) {
        this.location = location;
    }
}