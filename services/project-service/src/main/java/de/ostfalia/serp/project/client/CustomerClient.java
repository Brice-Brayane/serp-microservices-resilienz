package de.ostfalia.serp.project.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// On force l'URL vers le conteneur du customer-service
@FeignClient(name = "customer-service", url = "http://customer-service:8081")
public interface CustomerClient {
    
    // Assure-toi que cette URL correspond à l'endpoint réel de ton Customer API
    @GetMapping("/api/customers/{id}")
    ExternalCustomerDTO getCustomerById(@PathVariable("id") Long id);
}