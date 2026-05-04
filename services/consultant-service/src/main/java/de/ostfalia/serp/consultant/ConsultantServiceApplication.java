package de.ostfalia.serp.consultant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients 
public class ConsultantServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsultantServiceApplication.class, args);
    }
}