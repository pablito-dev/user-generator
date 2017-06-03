package com.pablito.generator.model.google;

import java.util.List;

/**
 * Created by pavel_000 on 01/06/2017.
 */
public class GoogleAddressComponentModel {
    private String long_name;
    private String short_name;
    private List<String> types;

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(final String long_name) {
        this.long_name = long_name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(final String short_name) {
        this.short_name = short_name;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(final List<String> types) {
        this.types = types;
    }
}
