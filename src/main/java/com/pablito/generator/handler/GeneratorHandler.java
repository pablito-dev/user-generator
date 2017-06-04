package com.pablito.generator.handler;

import com.pablito.generator.converter.ResponseFormatConverter;
import com.pablito.generator.service.GeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;


/**
 * Created by pavel_000 on 27/05/2017.
 */
@Component
public class GeneratorHandler {

    private GeneratorService generatorService;
    private ResponseFormatConverter responseFormatConverter;

    @Autowired
    public GeneratorHandler(final GeneratorService generatorService, final ResponseFormatConverter responseFormatConverter) {
        this.generatorService = generatorService;
        this.responseFormatConverter = responseFormatConverter;
    }

    public Mono<ServerResponse> renderData(final ServerRequest request) {
        final Integer sizeParam = request.queryParam("size").map(Integer::parseInt).orElse(5);
        final String regionParam = request.queryParam("region").orElse("England");
        final String domainParam = request.queryParam("domain").orElse("example.io");

        return request.queryParam("city")
                .map(cityParam -> ok().contentType(MediaType.TEXT_HTML).render("data",
                        responseFormatConverter.convertDataToAddressImpex(generatorService.generateUsers(cityParam, sizeParam, domainParam, regionParam))))
                .orElse(badRequest().build());
    }

    public Mono<ServerResponse> renderIndex(final ServerRequest request) {
        return ok().contentType(MediaType.TEXT_HTML).render("index", new HashMap<>());
    }
}
