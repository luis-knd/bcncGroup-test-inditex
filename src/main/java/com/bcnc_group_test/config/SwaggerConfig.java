package com.bcnc_group_test.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${bcnc_group_test.openapi.dev-url}")
    private String devUrl;

    @Value("${bcnc_group_test.openapi.prod-url}")
    private String prodUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        return new OpenAPI()
            .info(createInfo())
            .servers(createServers());
    }

    private Info createInfo() {
        Contact contact = new Contact()
            .email("luiscandelario41@gmail.com")
            .name("Luis Candelario")
            .url("https://www.lcandesign.com");

        return new Info()
            .title("BCNC Group API for inditex")
            .version("1.0")
            .contact(contact)
            .description("""
                    This project is part of a interview process for a backend developer position at BCNC Group.
                    The application is a Spring Boot service that provides an endpoint for querying product prices
                    based on brand, product, and application date.
                """);
    }

    private List<Server> createServers() {
        Server devServer = new Server()
            .url(devUrl)
            .description("Server URL in Development environment");

        Server prodServer = new Server()
            .url(prodUrl)
            .description("Server URL in Production environment");

        return Arrays.asList(devServer, prodServer);
    }

    @Bean
    public OpenApiCustomizer filterPaths() {
        return openApi -> {
            Paths paths = openApi.getPaths();
            Paths filteredPaths = new Paths();

            paths.forEach((key, value) -> {
                if (key.startsWith("/api/v1/")) {
                    filteredPaths.addPathItem(key, value);
                }
            });

            openApi.setPaths(filteredPaths);
        };
    }
}

