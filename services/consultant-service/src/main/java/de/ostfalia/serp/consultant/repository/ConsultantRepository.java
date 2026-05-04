package de.ostfalia.serp.consultant.repository;

import de.ostfalia.serp.consultant.entity.ConsultantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultantRepository extends JpaRepository<ConsultantEntity, Long> {
    // Cette interface hérite de toutes les méthodes CRUD (save, delete, findById, findAll...)
    // adaptées spécifiquement à ConsultantEntity.
}