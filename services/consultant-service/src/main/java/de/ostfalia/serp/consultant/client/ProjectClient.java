package de.ostfalia.serp.consultant.client;

import de.ostfalia.serp.consultant.dto.ConsultantProjectDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "project-service", url = "http://project-service:8084")
public interface ProjectClient {
    @GetMapping("/projects/consultant/{id}")
    List<ConsultantProjectDTO> getProjectsForConsultant(@PathVariable("id") Long id);
}