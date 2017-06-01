package com.pablito.generator.model.google;

import java.util.List;

/**
 * Created by pavel_000 on 28/05/2017.
 */
public class GoogleGeolocalizationModel {
    private GoogleGeometryModel geometry;
    private String formatted_address;
    private List<GoogleAddressComponentModel> address_components;

    public GoogleGeometryModel getGeometry() {
        return geometry;
    }

    public void setGeometry(final GoogleGeometryModel geometry) {
        this.geometry = geometry;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(final String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public List<GoogleAddressComponentModel> getAddress_components() {
        return address_components;
    }

    public void setAddress_components(final List<GoogleAddressComponentModel> address_components) {
        this.address_components = address_components;
    }
}
