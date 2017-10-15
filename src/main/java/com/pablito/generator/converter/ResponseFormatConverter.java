package com.pablito.generator.converter;

import com.pablito.generator.model.domain.ImpexModel;
import com.pablito.generator.model.domain.UserModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * Created by Paweł Nowak on 04/06/2017.
 */
@Component
public class ResponseFormatConverter {

    @Value("${app.impex.address.script}")
    private String ADDRESS_IMPEX_SCRIPT_LINE;
    @Value("${app.impex.user.script}")
    private String USER_IMPEX_SCRIPT_LINE;
    @Value("${app.impex.gender.male}")
    private String IMPEX_MALE_GENDER;
    @Value("${app.impex.gender.female}")
    private String IMPEX_FEMALE_GENDER;
    @Value("${app.default.currency}")
    private String IMPEX_DEFAULT_CURRENCY;
    @Value("${app.default.group}")
    private String IMPEX_DEFAULT_GROUP;

    public Mono<ImpexModel> convertDataToImpex(final Flux<UserModel> users) {
        return users
                .map(i -> {
                    final ImpexModel impexModel = new ImpexModel();
                    impexModel.setAddressImpex(convertUserToAddressImpex(i));
                    impexModel.setUserImpex(convertUserToCustomerImpex(i));

                    return impexModel;
                })
                .reduce((i, j) -> {
                    i.setUserImpex(i.getUserImpex() + "\n" + j.getUserImpex());
                    i.setAddressImpex(i.getAddressImpex() + "\n" + j.getAddressImpex());

                    return i;
                })
                .map(i -> {
                    i.setAddressImpex(ADDRESS_IMPEX_SCRIPT_LINE + "\n" + i.getAddressImpex());
                    i.setUserImpex(USER_IMPEX_SCRIPT_LINE + "\n" + i.getUserImpex());

                    return i;
                });
    }

    private String convertUserToAddressImpex(UserModel user) {
        return new StringBuilder()
                .append(convertFieldToImpex(user.getFirstName() + "." + user.getLastName()))
                .append(convertFieldToImpex(user.getFirstName()))
                .append(convertFieldToImpex(user.getLastName()))
                .append(convertFieldToImpex(user.getStreetName()))
                .append(convertFieldToImpex(user.getTown()))
                .append(convertFieldToImpex(user.getCountry()))
                .append(convertFieldToImpex(user.getPostalCode()))
                .append(convertFieldToImpex(user.getEmail()))
                .append(convertFieldToImpex(user.getPhone()))
                .append(convertFieldToImpex(user.getFax()))
                .append(convertFieldToImpex(user.getEmail()))
                .toString();
    }

    private String convertUserToCustomerImpex(final UserModel user) {
        return new StringBuilder()
                .append(convertFieldToImpex(user.getEmail()))
                .append(convertFieldToImpex(user.getEmail()))
                .append(convertFieldToImpex(user.getTitle()))
                .append(convertFieldToImpex(generateGenderFromTitle(user.getTitle())))
                .append(convertFieldToImpex(user.getFirstName() + " " + user.getLastName()))
                .append(convertFieldToImpex(user.getFirstName() + " " + user.getLastName()))
                .append(convertFieldToImpex(user.getCountry().toLowerCase()))
                .append(convertFieldToImpex(IMPEX_DEFAULT_CURRENCY))
                .append(convertFieldToImpex(user.getBirthday()))
                .append(convertFieldToImpex(IMPEX_DEFAULT_GROUP))
                .toString();
    }

    private String convertFieldToImpex(final String field) {
        return ";" + field;
    }

    private String generateGenderFromTitle(final String title) {
        return title.equalsIgnoreCase("mr") ? IMPEX_MALE_GENDER : IMPEX_FEMALE_GENDER;
    }
}
