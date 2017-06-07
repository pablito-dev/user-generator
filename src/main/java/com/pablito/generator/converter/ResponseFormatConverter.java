package com.pablito.generator.converter;

import com.pablito.generator.model.domain.UserModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * Created by pavel_000 on 04/06/2017.
 */
@Component
public class ResponseFormatConverter {

    @Value("${app.impex.address.script}")
    private String ADDRESS_IMPEX_SCRIPT_LINE;

    public Mono<String> convertDataToAddressImpex(final Flux<UserModel> generatedUsers) {
        return generatedUsers
                .map(convertUserToAddressImpex())
                .reduce((i, j) -> i + "\n" + j)
                .map(i -> ADDRESS_IMPEX_SCRIPT_LINE + "\n" + i);
    }

    public Mono<String> convertDataToUserImpex(final Flux<UserModel> generatedUsers) {
        return generatedUsers
                .map(convertUserToUserImpex())
                .reduce((i, j) -> i + "\n" + j)
                .map(i -> ADDRESS_IMPEX_SCRIPT_LINE + "\n" + i);
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
                .append(convertFieldToImpex(i.getPhone1()))
                .append(convertFieldToImpex(i.getFax()))
                .append(convertFieldToImpex(i.getEmail()))
                .toString();
    }

    private Function<UserModel, String> convertUserToUserImpex() {
        return i -> i.toString();
    }

    private String convertFieldToImpex(final String field) {
        return ";" + field;
    }
}
