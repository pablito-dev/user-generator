package com.pablito.generator.model.google;

import java.util.List;

/**
 * Created by pavel_000 on 27/05/2017.
 */
public class GoogleGeoLocalizationResponseModel {
    private String status;
    private List<GoogleGeoLocalizationModel> results;

    public List<GoogleGeoLocalizationModel> getResults() {
        return results;
    }

    public void setResults(final List<GoogleGeoLocalizationModel> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }
}

