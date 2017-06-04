package com.pablito.generator;

import com.pablito.generator.handler.GeneratorHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
public class GeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeneratorApplication.class, args);
	}

	@Bean
	RouterFunction<?> router(final GeneratorHandler generatorHandler) {
		return route(GET("/generate"), generatorHandler::renderData)
				.andRoute(GET("/index"), generatorHandler::renderIndex);
	}
}
