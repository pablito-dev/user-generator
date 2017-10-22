package com.pablito.generator.service.impl;

import com.pablito.generator.factory.UserFactory;
import com.pablito.generator.model.domain.UserModel;
import com.pablito.generator.repository.impl.DefaultGoogleRepository;
import com.pablito.generator.repository.impl.DefaultUiNamesRepository;
import com.pablito.generator.service.GeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * Created by pavel_000 on 02/06/2017.
 */
@Service
public class DefaultGeneratorService implements GeneratorService{

    private final DefaultGoogleRepository googleRepository;
    private final DefaultUiNamesRepository uiNamesRepository;
    private final UserFactory userFactory;

    @Autowired
    public DefaultGeneratorService(final DefaultGoogleRepository googleRepository, final DefaultUiNamesRepository uiNamesRepository
            , final UserFactory userFactory) {
        this.googleRepository = googleRepository;
        this.uiNamesRepository = uiNamesRepository;
        this.userFactory = userFactory;
    }

    @Override
    public Flux<UserModel> generateUsers(final String cityParam, final Integer sizeParam, final String domainParam
            , final String regionParam, final String key) {
       return Flux.zip(googleRepository.getRandomGeoLocalizationsWithinCity(cityParam, sizeParam, googleRepository.getGeoLocalizationForCityName(cityParam, key), key)
               , uiNamesRepository.getRandomUsersForRegion(sizeParam, regionParam)
               , (i, j) -> userFactory.createUser(i, j, domainParam));
    }
}
