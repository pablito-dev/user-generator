package com.pablito.generator.factory;

import com.pablito.generator.model.domain.UserModel;
import com.pablito.generator.model.google.GoogleAddressComponentModel;
import com.pablito.generator.model.google.GoogleGeoLocalizationModel;
import com.pablito.generator.model.uinames.UiNamesUserModel;
import com.pablito.generator.util.GoogleAddressComponentExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by pavel_000 on 01/06/2017.
 */
@Component
public class UserFactory {
    @Value("${google.api.address.type.street}")
    private String STREET_NUMBER_TYPE;
    @Value("${google.api.address.type.route}")
    private String ROUTE_TYPE;
    @Value("${google.api.address.type.country}")
    private String COUNTRY_TYPE;
    @Value("${google.api.address.type.postal}")
    private String POSTAL_CODE_TYPE;
    @Value("${google.api.address.type.locality}")
    private String LOCALITY_TYPE;

    private GoogleAddressComponentExtractor googleAddressComponentExtractor;

    @Autowired
    public UserFactory(final GoogleAddressComponentExtractor googleAddressComponentExtractor) {
        this.googleAddressComponentExtractor = googleAddressComponentExtractor;
    }

    public UserModel createUser(final GoogleGeoLocalizationModel googleGeoLocalizationModel,
                                final UiNamesUserModel uiNamesUserModel, final String emailDomain) {
        final UserModel model = new UserModel();

        final Optional<GoogleAddressComponentModel> routeModel = googleAddressComponentExtractor.extractAddressComponent(googleGeoLocalizationModel, ROUTE_TYPE);
        final Optional<GoogleAddressComponentModel> localityModel = googleAddressComponentExtractor.extractAddressComponent(googleGeoLocalizationModel, LOCALITY_TYPE);
        final Optional<GoogleAddressComponentModel> streetNumberModel = googleAddressComponentExtractor.extractAddressComponent(googleGeoLocalizationModel, STREET_NUMBER_TYPE);
        final Optional<GoogleAddressComponentModel> postalCodeModel =  googleAddressComponentExtractor.extractAddressComponent(googleGeoLocalizationModel, POSTAL_CODE_TYPE);
        final Optional<GoogleAddressComponentModel> countryModel =  googleAddressComponentExtractor.extractAddressComponent(googleGeoLocalizationModel, COUNTRY_TYPE);

        model.setFirstName(uiNamesUserModel.getName());
        model.setLastName(uiNamesUserModel.getSurname());
        model.setStreetName(routeModel.map(GoogleAddressComponentModel::getLong_name).orElse("") + " " +
                streetNumberModel.map(GoogleAddressComponentModel::getLong_name).orElse(""));
        model.setTown(localityModel.map(GoogleAddressComponentModel::getLong_name).orElse(""));
        model.setPostalCode(postalCodeModel.map(GoogleAddressComponentModel::getLong_name).orElse(""));
        model.setCountry(countryModel.map(GoogleAddressComponentModel::getShort_name).orElse(""));
        model.setPhone(uiNamesUserModel.getPhone());
        model.setFax(uiNamesUserModel.getPhone());
        model.setEmail(buildEmailWithDomain(uiNamesUserModel, emailDomain));
        model.setTitle(uiNamesUserModel.getTitle());
        model.setBirthday(convertUiNamesDateToImpexDate(uiNamesUserModel.getBirthday().getDmy()));

        return model;
    }

    private String buildEmailWithDomain(final UiNamesUserModel model, final String domain) {
        return (model.getName() + "." + model.getSurname() + "@" + domain).toLowerCase();
    }

    private String convertUiNamesDateToImpexDate(final String uiNamesDate) {
        return uiNamesDate.replace("/", "-");
    }
}
