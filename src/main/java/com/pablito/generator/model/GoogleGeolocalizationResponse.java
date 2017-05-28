package com.pablito.generator.model;

import java.util.List;

/**
 * Created by pavel_000 on 27/05/2017.
 */
public class GoogleGeolocalizationResponse {
    private String status;
    private List<Results> results;

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

class Results {
    private Geometry geometry;

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}

class Geometry {
    private GeographicSquare bounds;
    private GeographicSquare viewport;
    private String location_type;
    private GeographicPoint location;

    public GeographicSquare getGeographicSquare() {
        return bounds;
    }

    public void setBounds(GeographicSquare bounds) {
        this.bounds = bounds;
    }

    public GeographicSquare getViewport() {
        return viewport;
    }

    public void setViewport(GeographicSquare viewport) {
        this.viewport = viewport;
    }

    public String getLocation_type() {
        return location_type;
    }

    public void setLocation_type(String location_type) {
        this.location_type = location_type;
    }

    public GeographicPoint getLocation() {
        return location;
    }

    public void setLocation(GeographicPoint location) {
        this.location = location;
    }
}

