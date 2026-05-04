package de.ostfalia.serp.entry.config;

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
                        .title("SERP24 - Entry Service API")
                        .version("1.0")
                        .description("Microservice de gestion des saisies de temps (Time Entries) pour le projet de Master SERP24. " +
                                     "Ce service gère les imputations horaires des consultants sur les projets avec isolation des données via PostgreSQL.")
                        .contact(new Contact()
                                .name("Brice")
                                .email("brice.email@ostfalia.de")));
    }
}