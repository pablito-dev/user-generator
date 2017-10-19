package com.pablito.generator.service;

import com.pablito.generator.model.domain.UserModel;
import reactor.core.publisher.Flux;

/**
 * Created by pavel_000 on 02/06/2017.
 */
public interface GeneratorService {
   Flux<UserModel> generateUsers(final String cityParam, final Integer sizeParam, final String domainParam , final String regionParam, final String key);
}
