package com.pablito.generator.model.google;

import java.util.List;

/**
 * Created by pavel_000 on 27/05/2017.
 */
public class GoogleGeolocalizationResponseModel {
    private String status;
    private List<GoogleGeolocalizationModel> results;

    public List<GoogleGeolocalizationModel> getResults() {
        return results;
    }

    public void setResults(final List<GoogleGeolocalizationModel> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }
}

