package com.pablito.generator.handler;

import com.pablito.generator.factory.GeoLocalizationFactory;
import com.pablito.generator.factory.UserFactory;
import com.pablito.generator.model.domain.UserModel;
import com.pablito.generator.model.google.GoogleGeoLocalizationModel;
import com.pablito.generator.model.google.GoogleGeoLocalizationResponseModel;
import com.pablito.generator.model.uinames.UiNamesUserModel;
import com.pablito.generator.service.GeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;


/**
 * Created by pavel_000 on 27/05/2017.
 */
@Component
public class GeneratorHandler {

    private GeneratorService generatorService;

    @Autowired
    public GeneratorHandler(final GeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    public Mono<ServerResponse> generateUsers(final ServerRequest request) {
        final Integer sizeParam = request.queryParam("size").map(Integer::parseInt).orElse(5);
        final String regionParam = request.queryParam("region").orElse("England");
        final String domainParam = request.queryParam("domain").orElse("example.io");

        return request.queryParam("city")
                .map(cityParam -> ok().body(generatorService.generateUsers(cityParam, sizeParam, domainParam, regionParam), UserModel.class))
                .orElse(badRequest().build());
    }
}
