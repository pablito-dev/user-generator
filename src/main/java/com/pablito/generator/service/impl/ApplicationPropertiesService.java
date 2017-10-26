package com.pablito.generator.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by Paweł Nowak on 22/10/2017.
 */
@Service
public class ApplicationPropertiesService {
    @Value("${google.api.address.type.street}")
    private String streetNumberType;
    @Value("${google.api.address.type.route}")
    private String routeType;
    @Value("${google.api.address.type.country}")
    private String countryType;
    @Value("${google.api.address.type.postal}")
    private String postalCodeType;
    @Value("${google.api.address.type.locality}")
    private String localityType;

    @Value("${google.api.error.requestdenied}")
    private String googleAccessDeniedStatus;

    @Value("${app.default.region}")
    private String defaultRegion;
    @Value("${app.default.domain}")
    private String defaultDomain;
    @Value("${app.default.datasize}")
    private Integer defaultDataSize;

    @Value("${app.impex.address.script}")
    private String addressImpexScriptLine;
    @Value("${app.impex.user.script}")
    private String userImpexScriptLone;
    @Value("${app.impex.gender.male}")
    private String impexMaleGender;
    @Value("${app.impex.gender.female}")
    private String impexFemaleGender;
    @Value("${app.default.currency}")
    private String impexCurrency;
    @Value("${app.default.group}")
    private String impexDefaultGroup;

    @Value("${google.api.url}")
    private String googelAPIUrl;
    @Value("${google.api.endpoint}")
    private String googleGeoEndpoint;
    @Value("${google.api.param.address}")
    private String googleAddressParam;
    @Value("${google.api.param.latlng}")
    private String googleReverseParam;
    @Value("${google.api.param.lang}")
    private String googleLangParam;
    @Value("${google.api.param.key}")
    private String googleAPIKey;

    @Value("${uinames.api.url}")
    private String uiNamesAPIUrl;
    @Value("${uinames.api.param.amount}")
    private String uiNamesAmountParam;
    @Value("${uinames.api.param.region}")
    private String uiNamesRegionParam;
    @Value("${uinames.api.param.extended}")
    private String uiNamesExtendedParam;

    public String getStreetNumberType() {
        return streetNumberType;
    }

    public String getRouteType() {
        return routeType;
    }

    public String getCountryType() {
        return countryType;
    }

    public String getPostalCodeType() {
        return postalCodeType;
    }

    public String getLocalityType() {
        return localityType;
    }

    public String getDefaultRegion() {
        return defaultRegion;
    }

    public String getDefaultDomain() {
        return defaultDomain;
    }

    public Integer getDefaultDataSize() {
        return defaultDataSize;
    }

    public String getAddressImpexScriptLine() {
        return addressImpexScriptLine;
    }

    public String getUserImpexScriptLone() {
        return userImpexScriptLone;
    }

    public String getImpexMaleGender() {
        return impexMaleGender;
    }

    public String getImpexFemaleGender() {
        return impexFemaleGender;
    }

    public String getImpexCurrency() {
        return impexCurrency;
    }

    public String getImpexDefaultGroup() {
        return impexDefaultGroup;
    }

    public String getGoogelAPIUrl() {
        return googelAPIUrl;
    }

    public String getGoogleGeoEndpoint() {
        return googleGeoEndpoint;
    }

    public String getGoogleAddressParam() {
        return googleAddressParam;
    }

    public String getGoogleReverseParam() {
        return googleReverseParam;
    }

    public String getGoogleLangParam() {
        return googleLangParam;
    }

    public String getGoogleAPIKey() {
        return googleAPIKey;
    }

    public String getUiNamesAPIUrl() {
        return uiNamesAPIUrl;
    }

    public String getUiNamesAmountParam() {
        return uiNamesAmountParam;
    }

    public String getUiNamesRegionParam() {
        return uiNamesRegionParam;
    }

    public String getUiNamesExtendedParam() {
        return uiNamesExtendedParam;
    }

    public String getGoogleAccessDeniedStatus()
    {
        return googleAccessDeniedStatus;
    }
}
