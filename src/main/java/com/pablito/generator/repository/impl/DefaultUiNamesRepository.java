package com.pablito.generator.repository.impl;

import com.pablito.generator.model.uinames.UiNamesUserModel;
import com.pablito.generator.repository.UiNamesRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

/**
 * Created by pavel_000 on 02/06/2017.
 */
@Repository
public class DefaultUiNamesRepository implements UiNamesRepository {
    @Value("${uinames.api.url}")
    private String UINAMES_API_URL;
    @Value("${uinames.api.param.amount}")
    private String UINAMES_AMOUNT_PARAM;
    @Value("${uinames.api.param.region}")
    private String UINAMES_REGION_PARAM;
    @Value("${uinames.api.param.extended}")
    private String UI_NAMES_EXTENDED_PARAM;

    @Override
    public Flux<UiNamesUserModel> getRandomUsersForRegion (final Integer sizeParam, final String regionParam) {
        return WebClient.create(UINAMES_API_URL)
                .get()
                .uri(UINAMES_AMOUNT_PARAM + sizeParam + UINAMES_REGION_PARAM + regionParam + UI_NAMES_EXTENDED_PARAM)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(UiNamesUserModel.class);
    }
}
