package com.pablito.generator.factory;

import com.pablito.generator.model.domain.UserModel;
import com.pablito.generator.model.google.GoogleAddressComponentModel;
import com.pablito.generator.model.google.GoogleGeoLocalizationModel;
import com.pablito.generator.model.uinames.UiNamesUserModel;
import com.pablito.generator.service.impl.ApplicationPropertiesService;
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
    private final GoogleAddressComponentExtractor googleAddressComponentExtractor;
    private final ApplicationPropertiesService applicationPropertiesService;

    @Autowired
    public UserFactory(final GoogleAddressComponentExtractor googleAddressComponentExtractor,
                       final ApplicationPropertiesService applicationPropertiesService) {
        this.googleAddressComponentExtractor = googleAddressComponentExtractor;
        this.applicationPropertiesService = applicationPropertiesService;
    }

    public UserModel createUser(final GoogleGeoLocalizationModel googleGeoLocalizationModel,
                                final UiNamesUserModel uiNamesUserModel, final String emailDomain) {
        final UserModel model = new UserModel();

        final Optional<GoogleAddressComponentModel> routeModel = googleAddressComponentExtractor.extractAddressComponent(googleGeoLocalizationModel,
                applicationPropertiesService.getRouteType());
        final Optional<GoogleAddressComponentModel> localityModel = googleAddressComponentExtractor.extractAddressComponent(googleGeoLocalizationModel,
                applicationPropertiesService.getLocalityType());
        final Optional<GoogleAddressComponentModel> streetNumberModel = googleAddressComponentExtractor.extractAddressComponent(googleGeoLocalizationModel,
                applicationPropertiesService.getStreetNumberType());
        final Optional<GoogleAddressComponentModel> postalCodeModel = googleAddressComponentExtractor.extractAddressComponent(googleGeoLocalizationModel,
                applicationPropertiesService.getPostalCodeType());
        final Optional<GoogleAddressComponentModel> countryModel = googleAddressComponentExtractor.extractAddressComponent(googleGeoLocalizationModel,
                applicationPropertiesService.getCountryType());

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
