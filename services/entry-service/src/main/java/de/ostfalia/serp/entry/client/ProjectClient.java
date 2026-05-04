package de.ostfalia.serp.entry.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "project-service", url = "http://project-service:8084")
public interface ProjectClient {
    
    @GetMapping("/api/projects/{id}")
    ExternalProjectDTO getProjectById(@PathVariable("id") Long id);

    @GetMapping("/api/projects")
    List<ExternalProjectDTO> getAllProjects();
}