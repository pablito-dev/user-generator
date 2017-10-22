package com.pablito.generator.converter;

import com.pablito.generator.model.domain.ImpexModel;
import com.pablito.generator.model.domain.UserModel;
import com.pablito.generator.service.impl.ApplicationPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by Paweł Nowak on 04/06/2017.
 */
@Component
public class ResponseFormatConverter {
    private ApplicationPropertiesService applicationPropertiesService;

    @Autowired
    public ResponseFormatConverter(final ApplicationPropertiesService applicationPropertiesService) {
        this.applicationPropertiesService = applicationPropertiesService;
    }

    public Mono<ImpexModel> convertDataToImpex(final Flux<UserModel> users) {
        return users
                .map(mapToImpexModel())
                .reduce(reduceToImpexModel())
                .map(addImpexScriptLines());
    }

    private Function<UserModel, ImpexModel> mapToImpexModel() {
        return i -> new ImpexModel(convertUserToCustomerImpex(i), convertUserToAddressImpex(i));
    }

    private BiFunction<ImpexModel, ImpexModel, ImpexModel> reduceToImpexModel() {
        return (i, j) -> {
            i.setUserImpex(i.getUserImpex() + "\n" + j.getUserImpex());
            i.setAddressImpex(i.getAddressImpex() + "\n" + j.getAddressImpex());

            return i;
        };
    }

    private Function<ImpexModel, ImpexModel> addImpexScriptLines() {
        return i -> {
            i.setAddressImpex(applicationPropertiesService.getAddressImpexScriptLine() + "\n" + i.getAddressImpex());
            i.setUserImpex(applicationPropertiesService.getUserImpexScriptLone() + "\n" + i.getUserImpex());

            return i;
        };
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
                .append(convertFieldToImpex(applicationPropertiesService.getImpexCurrency()))
                .append(convertFieldToImpex(user.getBirthday()))
                .append(convertFieldToImpex(applicationPropertiesService.getImpexDefaultGroup()))
                .toString();
    }

    private String convertFieldToImpex(final String field) {
        return ";" + field;
    }

    private String generateGenderFromTitle(final String title) {
        return title.equalsIgnoreCase("mr") ? applicationPropertiesService.getImpexMaleGender() :
                applicationPropertiesService.getImpexFemaleGender();
    }
}
