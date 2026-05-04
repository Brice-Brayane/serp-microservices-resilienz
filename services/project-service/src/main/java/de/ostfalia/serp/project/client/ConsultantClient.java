package de.ostfalia.serp.project.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// On force l'URL vers le conteneur du consultant-service que tu as déjà réparé
@FeignClient(name = "consultant-service", url = "http://consultant-service:8082")
public interface ConsultantClient {
    
    // Assure-toi que cette URL correspond à l'endpoint réel de ton Consultant API
    @GetMapping("/api/consultants/{id}")
    ExternalConsultantDTO getConsultantById(@PathVariable("id") Long id);
}