package de.ostfalia.serp.entry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EntryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EntryServiceApplication.class, args);
    }

}