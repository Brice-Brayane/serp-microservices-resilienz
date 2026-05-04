package de.ostfalia.serp.consultant.config;

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
                        .title("SERP24 - Consultant Service API")
                        .version("1.0")
                        .description("Microservice zur Verwaltung von Consultant und der Organisationsstruktur für das Masterprojekt SERP24. " +
             "Dieser Dienst verwaltet Benutzerprofile sowie die Zuweisung von Beratern zu Projekten.")
                        .contact(new Contact()
                                .name("Brice")
                                .email("brice.email@ostfalia.de")));
    }
}

// Il sert à configurer Swagger (OpenAPI) pour qu'il génère une documentation élégante et professionnelle