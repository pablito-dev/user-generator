package com.pablito.generator.repository.impl;

import com.pablito.generator.model.uinames.UiNamesUserModel;
import com.pablito.generator.repository.UiNamesRepository;
import com.pablito.generator.service.impl.ApplicationPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

/**
 * Created by pavel_000 on 02/06/2017.
 */
@Repository
public class DefaultUiNamesRepository implements UiNamesRepository {
    private final ApplicationPropertiesService applicationPropertiesService;

    @Autowired
    public DefaultUiNamesRepository(final ApplicationPropertiesService applicationPropertiesService) {
        this.applicationPropertiesService = applicationPropertiesService;
    }

    @Override
    public Flux<UiNamesUserModel> getRandomUsersForRegion (final Integer sizeParam, final String regionParam) {
        return WebClient.create(applicationPropertiesService.getUiNamesAPIUrl())
                .get()
                .uri(applicationPropertiesService.getUiNamesAmountParam() + sizeParam + applicationPropertiesService.getUiNamesRegionParam()
                        + regionParam + applicationPropertiesService.getUiNamesExtendedParam())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(UiNamesUserModel.class);
    }
}
