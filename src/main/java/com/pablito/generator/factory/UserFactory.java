package com.pablito.generator.factory;

import com.pablito.generator.model.domain.UserModel;
import com.pablito.generator.model.google.GoogleAddressComponentModel;
import com.pablito.generator.model.google.GoogleGeolocalizationModel;
import com.pablito.generator.model.uinames.UiNamesUserModel;
import org.springframework.stereotype.Component;

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

    public UserModel createUser(final GoogleGeolocalizationModel googleGeolocalizationModel,
                                final UiNamesUserModel uiNamesUserModel) {
        final UserModel model = new UserModel();

        final Optional<GoogleAddressComponentModel> routeModel =
                googleGeolocalizationModel.getAddress_components().stream().filter(filterAddressComponent(ROUTE_TYPE)).findFirst();

        model.setFirstName(uiNamesUserModel.getName());
        model.setLastName(uiNamesUserModel.getSurname());
        model.setStreetName(routeModel.get().getLong_name());

        return model;
    }

    private Predicate<GoogleAddressComponentModel> filterAddressComponent(final String type) {
        return i -> i.getTypes().stream().anyMatch(t -> t.equals(type));
    }
}
