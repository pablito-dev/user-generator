package com.pablito.generator.handler;

import com.pablito.generator.converter.ResponseFormatConverter;
import com.pablito.generator.model.domain.UserModel;
import com.pablito.generator.service.GeneratorService;
import com.pablito.generator.service.impl.ApplicationPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;


/**
 * Created by pavel_000 on 27/05/2017.
 */
@Component
public class GeneratorHandler {
    private final GeneratorService generatorService;
    private final ResponseFormatConverter responseFormatConverter;
    private final ApplicationPropertiesService applicationPropertiesService;

    @Autowired
    public GeneratorHandler(final GeneratorService generatorService, final ResponseFormatConverter responseFormatConverter,
                            final ApplicationPropertiesService applicationPropertiesService) {
        this.generatorService = generatorService;
        this.responseFormatConverter = responseFormatConverter;
        this.applicationPropertiesService = applicationPropertiesService;
    }

    public Mono<ServerResponse> renderData(final ServerRequest request) {
        final Integer sizeParam = request.queryParam("size").filter(size -> size.matches("\\d+")).map(Integer::parseInt).orElse(applicationPropertiesService.getDefaultDataSize());
        final String regionParam = request.queryParam("region").filter(i -> !i.isEmpty()).orElse(applicationPropertiesService.getDefaultRegion());
        final String domainParam = request.queryParam("domain").filter(i -> !i.isEmpty()).orElse(applicationPropertiesService.getDefaultDomain());
        final String apiKey = request.queryParam("apikey").orElse("");

        return request.queryParam("city").filter(i -> !i.isEmpty())
                .map(cityParam -> ok().contentType(MediaType.TEXT_HTML).render("data", responseFormatConverter.convertDataToImpex(generatorService.generateUsers(cityParam.replace("+", " "),
                       sizeParam, domainParam, regionParam, apiKey))))
                .orElse(ok().contentType(MediaType.TEXT_HTML).render("index", "Please specify city"));
    }

    public Mono<ServerResponse> renderIndex(final ServerRequest request) {
        return ok().contentType(MediaType.TEXT_HTML).render("index", new HashMap<>());
    }
}
