package de.ostfalia.serp.project.repository;

import de.ostfalia.serp.project.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    
    // Pour l'harmonisation, on ajoute les méthodes de recherche spécifiques 
    // dont ton API aura besoin plus tard
    
    // Trouver les projets par ID client
    List<ProjectEntity> findByCustomerId(Long customerId);

    // Trouver les projets où un consultant spécifique est assigné
    List<ProjectEntity> findByConsultantIdsContaining(Long consultantId);
}