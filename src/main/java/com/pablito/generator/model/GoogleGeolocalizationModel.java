package com.pablito.generator.model;

/**
 * Created by pavel_000 on 28/05/2017.
 */
public class GoogleGeolocalizationModel {
    private GeometryModel geometry;
    private String formatted_address;

    public GeometryModel getGeometry() {
        return geometry;
    }

    public void setGeometry(final GeometryModel geometry) {
        this.geometry = geometry;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(final String formatted_address) {
        this.formatted_address = formatted_address;
    }
}
