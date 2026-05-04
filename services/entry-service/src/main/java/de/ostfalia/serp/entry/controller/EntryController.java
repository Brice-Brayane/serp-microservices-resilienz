package de.ostfalia.serp.entry.controller;

import de.ostfalia.serp.entry.api.TimeApi;
import de.ostfalia.serp.entry.dto.EntryConsultantDTO;
import de.ostfalia.serp.entry.dto.EntryDTO;
import de.ostfalia.serp.entry.dto.EntryProjectDTO;
import de.ostfalia.serp.entry.service.EntryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EntryController implements TimeApi {

    private final EntryService entryService;

    // L'injection de dépendance par constructeur
    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    @Override
    public ResponseEntity<List<EntryDTO>> getAllEntries() {
        return ResponseEntity.ok(entryService.getAllEntries());
    }

    @Override
    public ResponseEntity<List<EntryDTO>> getEntriesByConsultantId(Long consultantId) {
        return ResponseEntity.ok(entryService.getEntriesByConsultantId(consultantId));
    }

    @Override
    public ResponseEntity<EntryDTO> createEntry(Long consultantId, EntryDTO entryDTO) {
        // Retourne un statut 201 (CREATED) comme défini dans ton YAML
        return new ResponseEntity<>(entryService.createEntry(consultantId, entryDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<EntryDTO> updateEntry(Long consultantId, Long entryId, EntryDTO entryDTO) {
        return ResponseEntity.ok(entryService.updateEntry(consultantId, entryId, entryDTO));
    }

    @Override
    public ResponseEntity<List<EntryProjectDTO>> getAllProjects() {
        return ResponseEntity.ok(entryService.getAllProjects());
    }

    @Override
    public ResponseEntity<List<EntryConsultantDTO>> getAllEntryConsultants() {
        return ResponseEntity.ok(entryService.getAllEntryConsultants());
    }
}