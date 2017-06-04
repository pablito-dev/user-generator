package com.pablito.generator.converter;

import com.pablito.generator.model.domain.UserModel;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * Created by pavel_000 on 04/06/2017.
 */
@Component
public class ResponseFormatConverter {

    private static final String ADDRESS_IMPEX_SCRIPT_LINE = "INSERT_UPDATE Address; &addrID; firstname; lastname; " +
            "streetname[unique = true]; town; country(isocode); " +
            "postalcode[unique = true]; email; fax; phone1; owner(Customer.uid)\n";

    public Mono<String> convertDataToAddressImpex(final Flux<UserModel> generatedUsers) {
        return generatedUsers
                .map(convertUserToImpex())
                .reduce((i, j) -> i + "\n" + j)
                .map(i -> ADDRESS_IMPEX_SCRIPT_LINE + i);
    }

    private Function<UserModel, String> convertUserToImpex() {
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

    private String convertFieldToImpex(final String field) {
        return ";" + field;
    }
}
