package de.ostfalia.serp.project.controller;

import de.ostfalia.serp.project.api.ProjectsApi;
import de.ostfalia.serp.project.dto.ProjectDTO;
import de.ostfalia.serp.project.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProjectController implements ProjectsApi {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // 1. POST /projects
    @Override
    public ResponseEntity<ProjectDTO> createProject(ProjectDTO projectDTO) {
        return new ResponseEntity<>(projectService.save(projectDTO), HttpStatus.CREATED);
    }

    // 2. GET /projects
    @Override
    public ResponseEntity<List<ProjectDTO>> getProjects() {
        return ResponseEntity.ok(projectService.findAll());
    }

    // 3. GET /projects/{id} -> Nouveau nom généré par ton nouveau YAML
    @Override
    public ResponseEntity<ProjectDTO> getProjectById(Long id) {
        return ResponseEntity.ok(projectService.findById(id));
    }

    // 4. PUT /projects/{id} -> Renvoie maintenant le DTO mis à jour selon ton nouveau YAML
    @Override
    public ResponseEntity<ProjectDTO> updateProject(Long id, ProjectDTO projectDTO) {
        ProjectDTO updatedProject = projectService.update(id, projectDTO);
        return ResponseEntity.ok(updatedProject);
    }

    // 5. DELETE /projects/{id} -> Nouvelle route de ton YAML
    @Override
    public ResponseEntity<Void> deleteProject(Long id) {
        projectService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // 6. GET /projects/consultant/{id}
    @Override
    public ResponseEntity<List<ProjectDTO>> getProjectsByConsultant(Long id) {
        return ResponseEntity.ok(projectService.findByConsultantId(id));
    }
}