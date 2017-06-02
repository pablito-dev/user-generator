package com.pablito.generator.repository;

import com.pablito.generator.model.uinames.UiNamesUserModel;
import reactor.core.publisher.Flux;

/**
 * Created by pavel_000 on 02/06/2017.
 */
public interface UiNamesRepository {
    Flux<UiNamesUserModel> getRandomUsersForRegion (final Integer sizeParam, final String regionParam);
}
