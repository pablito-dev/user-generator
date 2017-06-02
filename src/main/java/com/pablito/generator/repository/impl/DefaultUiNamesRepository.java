package com.pablito.generator.repository.impl;

import com.pablito.generator.model.uinames.UiNamesUserModel;
import com.pablito.generator.repository.UiNamesRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

/**
 * Created by pavel_000 on 02/06/2017.
 */
@Repository
public class DefaultUiNamesRepository implements UiNamesRepository{
    private static final String UINAMES_API_URL = "https://uinames.com/api/";
    private static final String UINAMES_AMOUNT_PARAM = "?amount=";
    private static final String UINAMES_REGION_PARAM = "&region=";
    private static final String UI_NAMES_EXTENDED_PARAM = "&ext";

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
