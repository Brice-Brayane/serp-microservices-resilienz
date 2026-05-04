package de.ostfalia.serp.consultant.service;

import de.ostfalia.serp.consultant.client.ProjectClient;
import de.ostfalia.serp.consultant.dto.ConsultantDTO;
import de.ostfalia.serp.consultant.dto.ConsultantProjectDTO;
import de.ostfalia.serp.consultant.entity.ConsultantEntity;
import de.ostfalia.serp.consultant.repository.ConsultantRepository;
// Import indispensable pour le disjoncteur
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker; 
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service 
public class ConsultantService {

    private final ConsultantRepository consultantRepository;
    private final ModelMapper modelMapper;
    private final ProjectClient projectClient; 

    public ConsultantService(ConsultantRepository consultantRepository, ModelMapper modelMapper, ProjectClient projectClient) {
        this.consultantRepository = consultantRepository;
        this.modelMapper = modelMapper;
        this.projectClient = projectClient;
    }

    // ==========================================
    // MÉTHODE 1 : FIND ALL
    // ==========================================
    
    // On installe le disjoncteur. S'il y a un crash, il exécute 'fallbackFindAll'
    @CircuitBreaker(name = "projectServiceCB", fallbackMethod = "fallbackFindAll")
    @Transactional(readOnly = true)
    public List<ConsultantDTO> findAll() {
        return consultantRepository.findAll().stream()
                .map(entity -> {
                    ConsultantDTO dto = modelMapper.map(entity, ConsultantDTO.class);
                    // C'est cet appel réseau qui est protégé par le Circuit Breaker
                    List<ConsultantProjectDTO> projects = projectClient.getProjectsForConsultant(entity.getId());
                    dto.setBookedProjects(projects);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // --- LE PLAN B POUR FIND ALL ---
    public List<ConsultantDTO> fallbackFindAll(Throwable t) {
        System.err.println("⚡ CIRCUIT BREAKER AUSGELÖST (findAll)! Ursache: " + t.getMessage());
        
        // Au lieu de faire une Erreur 500, on renvoie les consultants sans appeler le service Projet !
        return consultantRepository.findAll().stream()
                .map(entity -> {
                    ConsultantDTO dto = modelMapper.map(entity, ConsultantDTO.class);
                    // On injecte une liste VIDE de projets pour éviter que React ne plante
                    dto.setBookedProjects(new ArrayList<>());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // ==========================================
    // MÉTHODE 2 : FIND BY ID
    // ==========================================

    @CircuitBreaker(name = "projectServiceCB", fallbackMethod = "fallbackFindById")
    @Transactional(readOnly = true)
    public ConsultantDTO findById(Long id) {
        ConsultantEntity entity = consultantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Berater nicht gefunden mit ID: " + id)); // Traduit aussi ici
        
        ConsultantDTO dto = modelMapper.map(entity, ConsultantDTO.class);

        // Appel réseau protégé
        List<ConsultantProjectDTO> assignedProjects = projectClient.getProjectsForConsultant(id);
        dto.setBookedProjects(assignedProjects);
        
        return dto;
    }

    // --- LE PLAN B POUR FIND BY ID ---
    public ConsultantDTO fallbackFindById(Long id, Throwable t) {
        System.err.println("⚡ CIRCUIT BREAKER AUSGELÖST (findById)! Ursache: " + t.getMessage());
        
        ConsultantEntity entity = consultantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Berater nicht gefunden mit ID: " + id));
        ConsultantDTO dto = modelMapper.map(entity, ConsultantDTO.class);
        
        // On renvoie le consultant avec une liste vide
        dto.setBookedProjects(new ArrayList<>());
        return dto;
    }

    // ==========================================
    // AUTRES MÉTHODES (Intactes)
    // ==========================================

    @Transactional
    public ConsultantDTO save(ConsultantDTO consultantDTO) {
        ConsultantEntity entity = modelMapper.map(consultantDTO, ConsultantEntity.class);
        ConsultantEntity savedEntity = consultantRepository.save(entity);
        return modelMapper.map(savedEntity, ConsultantDTO.class);
    }

    @Transactional
    public void deleteById(Long id) {
        consultantRepository.deleteById(id);
    }

    @Transactional
    public ConsultantDTO update(Long id, ConsultantDTO consultantDTO) {
        ConsultantEntity entity = consultantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aktualisierung fehlgeschlagen: Berater nicht gefunden"));
        
        entity.setName(consultantDTO.getName());
        ConsultantEntity savedEntity = consultantRepository.save(entity);
        return modelMapper.map(savedEntity, ConsultantDTO.class);
    }
}