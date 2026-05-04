package de.ostfalia.serp.entry.repository;

import de.ostfalia.serp.entry.entity.EntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<EntryEntity, Long> {
    // Méthode magique Spring Data pour trouver les entrées d'un consultant spécifique
    List<EntryEntity> findByConsultantId(Long consultantId);
}