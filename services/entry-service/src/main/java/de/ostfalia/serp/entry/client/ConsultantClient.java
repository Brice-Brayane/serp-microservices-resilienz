package de.ostfalia.serp.entry.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "consultant-service", url = "http://consultant-service:8082")
public interface ConsultantClient {
    
    @GetMapping("/api/consultants/{id}")
    ExternalConsultantDTO getConsultantById(@PathVariable("id") Long id);

    @GetMapping("/api/consultants")
    List<ExternalConsultantDTO> getAllConsultants();
}