package com.pablito.generator.factory;

import com.pablito.generator.model.domain.UserModel;
import com.pablito.generator.model.google.GoogleAddressComponentModel;
import com.pablito.generator.model.google.GoogleGeoLocalizationModel;
import com.pablito.generator.model.uinames.UiNamesUserModel;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by pavel_000 on 01/06/2017.
 */
@Component
public class UserFactory {
    private static final String STREET_NUMBER_TYPE = "street_number";
    private static final String ROUTE_TYPE = "route";
    private static final String COUNTRY_TYPE = "country";
    private static final String POSTAL_CODE_TYPE = "postal_code";
    private static final String LOCALITY_TYPE = "locality";

    public UserModel createUser(final GoogleGeoLocalizationModel googleGeoLocalizationModel,
                                final UiNamesUserModel uiNamesUserModel, final String emailDomain) {
        final UserModel model = new UserModel();

        final Optional<GoogleAddressComponentModel> routeModel = extractAddressComponent(googleGeoLocalizationModel, ROUTE_TYPE);
        final Optional<GoogleAddressComponentModel> localityModel = extractAddressComponent(googleGeoLocalizationModel, LOCALITY_TYPE);
        final Optional<GoogleAddressComponentModel> streetNumberModel = extractAddressComponent(googleGeoLocalizationModel, STREET_NUMBER_TYPE);
        final Optional<GoogleAddressComponentModel> postalCodeModel =  extractAddressComponent(googleGeoLocalizationModel, POSTAL_CODE_TYPE);
        final Optional<GoogleAddressComponentModel> countryModel =  extractAddressComponent(googleGeoLocalizationModel, COUNTRY_TYPE);

        model.setFirstName(uiNamesUserModel.getName());
        model.setLastName(uiNamesUserModel.getSurname());
        model.setStreetName(routeModel.map(GoogleAddressComponentModel::getLong_name).orElse("") + " " +
                streetNumberModel.map(GoogleAddressComponentModel::getLong_name).orElse(""));
        model.setTown(localityModel.map(GoogleAddressComponentModel::getLong_name).orElse(""));
        model.setPostalCode(postalCodeModel.map(GoogleAddressComponentModel::getLong_name).orElse(""));
        model.setCountry(countryModel.map(GoogleAddressComponentModel::getShort_name).orElse(""));
        model.setPhone1(uiNamesUserModel.getPhone());
        model.setFax(uiNamesUserModel.getPhone());
        model.setEmail(buildEmailWithDomain(uiNamesUserModel, emailDomain));

        return model;
    }

    private Optional<GoogleAddressComponentModel> extractAddressComponent(final GoogleGeoLocalizationModel model, final String type) {
        return model.getAddress_components().stream().filter(filterAddressComponent(type)).findFirst();
    }

    private Predicate<GoogleAddressComponentModel> filterAddressComponent(final String type) {
        return i -> i.getTypes().stream().anyMatch(t -> t.equals(type));
    }

    private String buildEmailWithDomain(final UiNamesUserModel model, final String domain) {
        return (model.getName() + "." + model.getSurname() + "@" + domain).toLowerCase();
    }
}
