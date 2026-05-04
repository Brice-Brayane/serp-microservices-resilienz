package de.ostfalia.serp.entry.service;

import de.ostfalia.serp.entry.client.ConsultantClient;
import de.ostfalia.serp.entry.client.ExternalConsultantDTO;
import de.ostfalia.serp.entry.client.ExternalProjectDTO;
import de.ostfalia.serp.entry.client.ProjectClient;
import de.ostfalia.serp.entry.dto.EntryConsultantDTO;
import de.ostfalia.serp.entry.dto.EntryDTO;
import de.ostfalia.serp.entry.dto.EntryProjectDTO;
import de.ostfalia.serp.entry.entity.EntryEntity;
import de.ostfalia.serp.entry.repository.EntryRepository;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntryService {

    private final EntryRepository entryRepository;
    private final ProjectClient projectClient;
    private final ConsultantClient consultantClient;

    public EntryService(EntryRepository entryRepository, ProjectClient projectClient, ConsultantClient consultantClient) {
        this.entryRepository = entryRepository;
        this.projectClient = projectClient;
        this.consultantClient = consultantClient;
    }

    // ==========================================
    // LOGIQUE DE MAPPING (NORMAL ET SECOURS)
    // ==========================================
    
    // MAPPER NORMAL (Avec appels réseau et gestion des 404)
    private EntryDTO convertToDTO(EntryEntity entity) {
        EntryDTO dto = new EntryDTO();
        dto.setEntryId(entity.getId());
        dto.setHours(entity.getHours());
        dto.setDate(entity.getDate());

        // 1. Récupération du Projet (Gestion propre du 404)
        EntryProjectDTO projectDTO = new EntryProjectDTO();
        projectDTO.setProjectId(entity.getProjectId());
        try {
            ExternalProjectDTO extProject = projectClient.getProjectById(entity.getProjectId());
            projectDTO.setName(extProject != null && extProject.getName() != null ? extProject.getName() : "Unbekanntes Projekt");
        } catch (FeignException.NotFound e) {
            projectDTO.setName("Projekt nicht gefunden (404)");
        }
        dto.setProject(projectDTO);

        // 2. Récupération du Consultant (Gestion propre du 404)
        EntryConsultantDTO consultantDTO = new EntryConsultantDTO();
        consultantDTO.setConsultantId(entity.getConsultantId());
        try {
            ExternalConsultantDTO extConsultant = consultantClient.getConsultantById(entity.getConsultantId());
            consultantDTO.setName(extConsultant != null && extConsultant.getName() != null ? extConsultant.getName() : "Unbekannter Berater");
        } catch (FeignException.NotFound e) {
            consultantDTO.setName("Berater nicht gefunden (404)");
        }
        dto.setConsultant(consultantDTO);

        return dto;
    }

    // MAPPER DE SECOURS (100% sûr, aucun appel réseau)
    private EntryDTO convertSafeDTO(EntryEntity entity) {
        EntryDTO dto = new EntryDTO();
        dto.setEntryId(entity.getId());
        dto.setHours(entity.getHours());
        dto.setDate(entity.getDate());

        EntryProjectDTO projectDTO = new EntryProjectDTO();
        projectDTO.setProjectId(entity.getProjectId());
        projectDTO.setName("Service nicht verfügbar");
        dto.setProject(projectDTO);

        EntryConsultantDTO consultantDTO = new EntryConsultantDTO();
        consultantDTO.setConsultantId(entity.getConsultantId());
        consultantDTO.setName("Service nicht verfügbar");
        dto.setConsultant(consultantDTO);

        return dto;
    }

    // ==========================================
    // LOGIQUE MÉTIER PRINCIPALE (AVEC CIRCUIT BREAKER)
    // ==========================================

    @CircuitBreaker(name = "projectServiceCB", fallbackMethod = "fallbackGetAllEntries")
    @Transactional(readOnly = true)
    public List<EntryDTO> getAllEntries() {
        return entryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EntryDTO> fallbackGetAllEntries(Throwable t) {
        System.err.println("⚡ CIRCUIT BREAKER AUSGELÖST (getAllEntries)! Ursache: " + t.getMessage());
        return entryRepository.findAll().stream()
                .map(this::convertSafeDTO)
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "projectServiceCB", fallbackMethod = "fallbackGetEntriesByConsultantId")
    @Transactional(readOnly = true)
    public List<EntryDTO> getEntriesByConsultantId(Long consultantId) {
        return entryRepository.findByConsultantId(consultantId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EntryDTO> fallbackGetEntriesByConsultantId(Long consultantId, Throwable t) {
        System.err.println("⚡ CIRCUIT BREAKER AUSGELÖST (getEntriesByConsultantId)! Ursache: " + t.getMessage());
        return entryRepository.findByConsultantId(consultantId).stream()
                .map(this::convertSafeDTO)
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "projectServiceCB", fallbackMethod = "fallbackCreateEntry")
    @Transactional
    public EntryDTO createEntry(Long consultantId, EntryDTO dto) {
        EntryEntity entity = new EntryEntity();
        entity.setConsultantId(consultantId);
        entity.setProjectId(dto.getProject().getProjectId());
        entity.setHours(dto.getHours());
        
        if (dto.getDate() != null) {
            entity.setDate(dto.getDate());
        } else {
            entity.setDate(OffsetDateTime.now());
        }
        return convertToDTO(entryRepository.save(entity));
    }

    public EntryDTO fallbackCreateEntry(Long consultantId, EntryDTO dto, Throwable t) {
        System.err.println("⚡ CIRCUIT BREAKER AUSGELÖST (createEntry)! Ursache: " + t.getMessage());
        // On sauvegarde quand même l'entrée en BDD pour ne pas perdre les heures travaillées !
        EntryEntity entity = new EntryEntity();
        entity.setConsultantId(consultantId);
        entity.setProjectId(dto.getProject().getProjectId());
        entity.setHours(dto.getHours());
        entity.setDate(dto.getDate() != null ? dto.getDate() : OffsetDateTime.now());
        
        return convertSafeDTO(entryRepository.save(entity));
    }

    @CircuitBreaker(name = "projectServiceCB", fallbackMethod = "fallbackUpdateEntry")
    @Transactional
    public EntryDTO updateEntry(Long consultantId, Long entryId, EntryDTO dto) {
        EntryEntity entity = entryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Zeiteintrag nicht gefunden"));

        entity.setConsultantId(consultantId); 
        entity.setProjectId(dto.getProject().getProjectId());
        entity.setHours(dto.getHours());
        if (dto.getDate() != null) {
            entity.setDate(dto.getDate());
        }
        return convertToDTO(entryRepository.save(entity));
    }

    public EntryDTO fallbackUpdateEntry(Long consultantId, Long entryId, EntryDTO dto, Throwable t) {
        System.err.println("⚡ CIRCUIT BREAKER AUSGELÖST (updateEntry)! Ursache: " + t.getMessage());
        EntryEntity entity = entryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Zeiteintrag nicht gefunden"));
        entity.setConsultantId(consultantId); 
        entity.setProjectId(dto.getProject().getProjectId());
        entity.setHours(dto.getHours());
        if (dto.getDate() != null) entity.setDate(dto.getDate());
        
        return convertSafeDTO(entryRepository.save(entity));
    }

    // ==========================================
    // RÉCUPÉRATION DES LISTES (DROPDOWNS REACT)
    // ==========================================

    @CircuitBreaker(name = "projectServiceCB", fallbackMethod = "fallbackGetAllProjects")
    public List<EntryProjectDTO> getAllProjects() {
        return projectClient.getAllProjects().stream().map(ext -> {
            EntryProjectDTO p = new EntryProjectDTO();
            p.setProjectId(ext.getId());
            p.setName(ext.getName());
            return p;
        }).collect(Collectors.toList());
    }

    public List<EntryProjectDTO> fallbackGetAllProjects(Throwable t) {
        System.err.println("⚡ CIRCUIT BREAKER AUSGELÖST (Dropdown Projekte)! Ursache: " + t.getMessage());
        return Collections.emptyList();
    }

    @CircuitBreaker(name = "consultantServiceCB", fallbackMethod = "fallbackGetAllEntryConsultants")
    public List<EntryConsultantDTO> getAllEntryConsultants() {
        return consultantClient.getAllConsultants().stream().map(ext -> {
            EntryConsultantDTO c = new EntryConsultantDTO();
            c.setConsultantId(ext.getId());
            c.setName(ext.getName());
            return c;
        }).collect(Collectors.toList());
    }

    public List<EntryConsultantDTO> fallbackGetAllEntryConsultants(Throwable t) {
        System.err.println("⚡ CIRCUIT BREAKER AUSGELÖST (Dropdown Berater)! Ursache: " + t.getMessage());
        return Collections.emptyList();
    }
}