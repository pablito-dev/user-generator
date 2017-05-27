package com.pablito.generator.handler;

import com.pablito.generator.model.GoogleGeolocalizationModel;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import org.springframework.web.reactive.function.client.WebClient;


/**
 * Created by pavel_000 on 27/05/2017.
 */
@Component
public class GeneratorHandler {
    private static final String GOOGLE_API_URL = "https://maps.googleapis.com/maps/api";
    private static final String GOOGLE_GEO_ENDPOINT = "/geocode/json";
    private static final String GOOGLE_ADDRESS_PARAM = "?address=";

    public Mono<ServerResponse> generateUsers(final ServerRequest request) {
        if (request.queryParam("city").isPresent()) {
            return ok()
                    .body(WebClient.create(GOOGLE_API_URL)
                    .get()
                    .uri(GOOGLE_GEO_ENDPOINT + GOOGLE_ADDRESS_PARAM + request.queryParam("city").get())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToFlux(String.class), String.class);
        }
        else {
            return badRequest().build();
        }
    }
}
