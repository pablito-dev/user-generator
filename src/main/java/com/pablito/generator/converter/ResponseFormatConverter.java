package com.pablito.generator.converter;

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


    public Mono<String> convertDataToAddressImpex(final Flux<UserModel> generatedUsers) {
        return generatedUsers
                .map(convertUserToAddressImpex())
                .reduce((i, j) -> i + "\n" + j)
                .map(i -> ADDRESS_IMPEX_SCRIPT_LINE + "\n" + i);
    }

    public Mono<String> convertDataToCustomerImpex(final Flux<UserModel> generatedUsers) {
        return generatedUsers
                .map(convertUserToCustomerImpex())
                .reduce((i, j) -> i + "\n" + j)
                .map(i -> USER_IMPEX_SCRIPT_LINE + "\n" + i)
                //filter is used as a hack because it seems there is no other way to change the name of the model param
                //name. right now if 2 mono's are returned with the same last operation the second one will overwrite
                //the first one
                .filter(i -> true);
    }

    private Function<UserModel, String> convertUserToAddressImpex() {
        return i -> new StringBuilder()
                .append(convertFieldToImpex(i.getFirstName() + "." + i.getLastName()))
                .append(convertFieldToImpex(i.getFirstName()))
                .append(convertFieldToImpex(i.getLastName()))
                .append(convertFieldToImpex(i.getStreetName()))
                .append(convertFieldToImpex(i.getTown()))
                .append(convertFieldToImpex(i.getCountry()))
                .append(convertFieldToImpex(i.getPostalCode()))
                .append(convertFieldToImpex(i.getEmail()))
                .append(convertFieldToImpex(i.getPhone()))
                .append(convertFieldToImpex(i.getFax()))
                .append(convertFieldToImpex(i.getEmail()))
                .toString();
    }

    private Function<UserModel, String> convertUserToCustomerImpex() {
        return i -> new StringBuilder()
                .append(convertFieldToImpex(i.getEmail()))
                .append(convertFieldToImpex(i.getEmail()))
                .append(convertFieldToImpex(i.getTitle()))
                .append(convertFieldToImpex(generateGenderFromTitle(i.getTitle())))
                .append(convertFieldToImpex(i.getFirstName() + " " + i.getLastName()))
                .append(convertFieldToImpex(i.getFirstName() + " " + i.getLastName()))
                .append(convertFieldToImpex(i.getCountry().toLowerCase()))
                .append(convertFieldToImpex(IMPEX_DEFAULT_CURRENCY))
                .append(convertFieldToImpex(i.getBirthday()))
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
