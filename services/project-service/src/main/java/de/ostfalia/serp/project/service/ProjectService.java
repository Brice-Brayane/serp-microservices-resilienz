package de.ostfalia.serp.project.service;

import de.ostfalia.serp.project.client.ConsultantClient;
import de.ostfalia.serp.project.client.CustomerClient;
import de.ostfalia.serp.project.client.ExternalConsultantDTO;
import de.ostfalia.serp.project.client.ExternalCustomerDTO;
import de.ostfalia.serp.project.dto.ProjectDTO;
import de.ostfalia.serp.project.dto.ProjectConsultantDTO;
import de.ostfalia.serp.project.dto.ProjectCustomerDTO;
import de.ostfalia.serp.project.entity.ProjectEntity;
import de.ostfalia.serp.project.repository.ProjectRepository;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service 
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;
    private final CustomerClient customerClient;
    private final ConsultantClient consultantClient;

    public ProjectService(ProjectRepository projectRepository, ModelMapper modelMapper, 
                          CustomerClient customerClient, ConsultantClient consultantClient) {
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
        this.customerClient = customerClient;
        this.consultantClient = consultantClient;
    }

    private ProjectEntity convertToEntity(ProjectDTO dto) {
        ProjectEntity entity = new ProjectEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setStatus(dto.getStatus());
        
        if (dto.getStart() != null) entity.setStart(dto.getStart().toLocalDate());
        if (dto.getEnd() != null) entity.setEnd(dto.getEnd().toLocalDate());

        if (dto.getCustomer() != null && dto.getCustomer().getCustomerId() != null) {
            entity.setCustomerId(dto.getCustomer().getCustomerId());
        }
        
        if (dto.getProjectStaff() != null && !dto.getProjectStaff().isEmpty()) {
            List<Long> ids = dto.getProjectStaff().stream()
                    .map(ProjectConsultantDTO::getConsultantId)
                    .collect(Collectors.toList());
            entity.setConsultantIds(ids);
        } else {
            entity.setConsultantIds(new ArrayList<>());
        }
        return entity;
    }

    // MAPPER NORMAL
    private ProjectDTO convertToDTO(ProjectEntity entity) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus());
        
        if (entity.getStart() != null) dto.setStart(entity.getStart().atStartOfDay().atOffset(ZoneOffset.UTC));
        if (entity.getEnd() != null) dto.setEnd(entity.getEnd().atStartOfDay().atOffset(ZoneOffset.UTC));

        // Appel réseau (Customer) 
        if (entity.getCustomerId() != null) {
            dto.setCustomer(fetchCustomer(entity.getCustomerId()));
        } else {
            ProjectCustomerDTO emptyCust = new ProjectCustomerDTO();
            emptyCust.setName("Kein Kunde zugewiesen");
            dto.setCustomer(emptyCust);
        }
        
        // Appel réseau (Consultants) 
        if (entity.getConsultantIds() != null && !entity.getConsultantIds().isEmpty()) {
            List<ProjectConsultantDTO> staffList = entity.getConsultantIds().stream()
                    .map(this::fetchConsultant)
                    .collect(Collectors.toList());
            dto.setProjectStaff(staffList);
        } else {
            dto.setProjectStaff(new ArrayList<>());
        }
        
        return dto;
    }

    // ========================================================
    // LOGIQUE RÉSEAU INTERNE (SANS ANNOTATIONS AOP ICI)
    // ========================================================

    public ProjectCustomerDTO fetchCustomer(Long customerId) {
        System.out.println("🔄 Versuch, den Customer-Service aufzurufen... (Kunde-ID: " + customerId + ")");
        ProjectCustomerDTO customerDTO = new ProjectCustomerDTO();
        customerDTO.setCustomerId(customerId);
        try {
            ExternalCustomerDTO extCustomer = customerClient.getCustomerById(customerId);
            customerDTO.setName(extCustomer != null && extCustomer.getName() != null ? extCustomer.getName() : "Unbekannter Kunde");
        } catch (FeignException.NotFound e) {
            customerDTO.setName("Kunde nicht gefunden (404)");
        }
        return customerDTO;
    }

    public ProjectConsultantDTO fetchConsultant(Long consultantId) {
        System.out.println("🔄 Versuch, den Consultant-Service aufzurufen... (Berater-ID: " + consultantId + ")");
        ProjectConsultantDTO staffDTO = new ProjectConsultantDTO();
        staffDTO.setConsultantId(consultantId);
        try {
            ExternalConsultantDTO extConsultant = consultantClient.getConsultantById(consultantId);
            staffDTO.setName(extConsultant != null && extConsultant.getName() != null ? extConsultant.getName() : "Unbekannter Berater");
        } catch (FeignException.NotFound e) {
            staffDTO.setName("Berater nicht gefunden (404)");
        }
        return staffDTO;
    }


    // MAPPER DE SECOURS (Si le disjoncteur saute, on passe ici pour éviter les crashs)
    private ProjectDTO convertSafeDTO(ProjectEntity entity) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus());
        if (entity.getStart() != null) dto.setStart(entity.getStart().atStartOfDay().atOffset(ZoneOffset.UTC));
        if (entity.getEnd() != null) dto.setEnd(entity.getEnd().atStartOfDay().atOffset(ZoneOffset.UTC));

        ProjectCustomerDTO safeCust = new ProjectCustomerDTO();
        safeCust.setCustomerId(entity.getCustomerId());
        safeCust.setName("Service nicht verfügbar"); // En allemand pour le prof
        dto.setCustomer(safeCust);

        dto.setProjectStaff(new ArrayList<>());
        return dto;
    }

    // ========================================================
    // LOGIQUE MÉTIER PROTÉGÉE PAR RETRY ET CIRCUIT BREAKER
    // ========================================================

    // On place le Retry en premier : il tente 3 fois. S'il échoue 3 fois, le Circuit Breaker prend le relais.
    @Retry(name = "customerServiceRetry")
    @CircuitBreaker(name = "customerServiceCB", fallbackMethod = "fallbackFindAll")
    @Transactional(readOnly = true)
    public List<ProjectDTO> findAll() {
        return projectRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProjectDTO> fallbackFindAll(Throwable t) {
        System.err.println("⚡ CIRCUIT BREAKER AUSGELÖST (findAll Projekte) nach 3 Retry-Versuchen! Ursache: " + t.getMessage());
        return projectRepository.findAll().stream()
                .map(this::convertSafeDTO) 
                .collect(Collectors.toList());
    }

    @Retry(name = "customerServiceRetry")
    @CircuitBreaker(name = "customerServiceCB", fallbackMethod = "fallbackFindById")
    @Transactional(readOnly = true)
    public ProjectDTO findById(Long id) {
        ProjectEntity entity = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projekt nicht gefunden mit ID : " + id)); 
        return convertToDTO(entity);
    }

    public ProjectDTO fallbackFindById(Long id, Throwable t) {
        System.err.println("⚡ CIRCUIT BREAKER AUSGELÖST (findById Projekt) nach 3 Retry-Versuchen! Ursache: " + t.getMessage());
        ProjectEntity entity = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projekt nicht gefunden mit ID : " + id)); 
        return convertSafeDTO(entity); 
    }

    // ========================================================
    // MÉTHODES CRUD CLASSIQUES
    // ========================================================

    @Transactional
    public ProjectDTO save(ProjectDTO projectDTO) {
        ProjectEntity entity = convertToEntity(projectDTO);
        ProjectEntity savedEntity = projectRepository.save(entity);
        return convertToDTO(savedEntity);
    }

    @Transactional
    public void deleteById(Long id) {
        projectRepository.deleteById(id);
    }

    @Transactional
    public ProjectDTO update(Long id, ProjectDTO projectDTO) {
        ProjectEntity entity = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aktualisierung fehlgeschlagen: Projekt nicht gefunden")); 
        
        entity.setName(projectDTO.getName());
        entity.setStatus(projectDTO.getStatus());
        
        if (projectDTO.getStart() != null) entity.setStart(projectDTO.getStart().toLocalDate());
        else entity.setStart(null);
        
        if (projectDTO.getEnd() != null) entity.setEnd(projectDTO.getEnd().toLocalDate());
        else entity.setEnd(null);

        if (projectDTO.getCustomer() != null) {
            entity.setCustomerId(projectDTO.getCustomer().getCustomerId());
        }

        if (projectDTO.getProjectStaff() != null) {
            List<Long> staffIds = projectDTO.getProjectStaff().stream()
                    .map(ProjectConsultantDTO::getConsultantId)
                    .collect(Collectors.toList());
            if(entity.getConsultantIds() == null) {
                 entity.setConsultantIds(new ArrayList<>());
            }
            entity.getConsultantIds().clear();
            entity.getConsultantIds().addAll(staffIds);
        }
        
        ProjectEntity savedEntity = projectRepository.save(entity);
        return convertToDTO(savedEntity);
    }

    @Transactional(readOnly = true)
    public List<ProjectDTO> findByCustomerId(Long customerId) {
        return projectRepository.findByCustomerId(customerId).stream()
                .map(this::convertSafeDTO) 
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProjectDTO> findByConsultantId(Long consultantId) {
        return projectRepository.findAll().stream()
                .filter(project -> project.getConsultantIds() != null && 
                                   project.getConsultantIds().contains(consultantId))
                .map(this::convertSafeDTO) 
                .collect(Collectors.toList());
    }
}