package de.ostfalia.serp.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProjectServiceApplication {

    public static void main(String[] args) {
        // CORRECTION ICI : on utilise .class et non .java
        SpringApplication.run(ProjectServiceApplication.class, args);
    }
}