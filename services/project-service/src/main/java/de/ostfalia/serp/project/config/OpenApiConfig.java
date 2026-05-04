package de.ostfalia.serp.project.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SERP24 - Customer Service API")
                        .version("1.0")
                        .description("Microservice de gestion des clients pour le projet de Master SERP24. " +
                                     "Ce service permet le CRUD complet des clients et l'isolation des données via PostgreSQL.")
                        .contact(new Contact()
                                .name("Ton Nom")
                                .email("ton.email@ostfalia.de")));
    }
}