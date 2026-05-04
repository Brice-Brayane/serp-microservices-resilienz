package de.ostfalia.serp.consultant.controller;

// On importe les deux nouvelles interfaces générées
import de.ostfalia.serp.consultant.api.ConsultantsApi;
import de.ostfalia.serp.consultant.api.UsersApi;
import de.ostfalia.serp.consultant.dto.ConsultantDTO;
import de.ostfalia.serp.consultant.dto.UserDTO;
import de.ostfalia.serp.consultant.service.ConsultantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// C'est lui ( le controller) qui reçoit les appels venant du Gateway et qui décide quoi en faire.
// Il reçoit la requête HTTP.
// Il appelle le ConsultantService pour faire le travail.
// Il renvoie une ResponseEntity avec le bon code de statut (ex: 200 OK ou 201 CREATED).

@RestController
// On implémente les deux contrats !
public class ConsultantController implements ConsultantsApi, UsersApi {

    private final ConsultantService consultantService;

    public ConsultantController(ConsultantService consultantService) {
        this.consultantService = consultantService;
    }

    // ==========================================
    // 1. ENDPOINTS CONSULTANTS
    // ==========================================

    @Override
    public ResponseEntity<List<ConsultantDTO>> getConsultants(Boolean shallow) {
        return ResponseEntity.ok(consultantService.findAll());
    }

    @Override
    public ResponseEntity<ConsultantDTO> createConsultant(ConsultantDTO consultantDTO) {
        return new ResponseEntity<>(consultantService.save(consultantDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ConsultantDTO> getConsultant(Long id) {
        return ResponseEntity.ok(consultantService.findById(id));
    }

    @Override
    public ResponseEntity<ConsultantDTO> updateConsultant(Long id, ConsultantDTO consultantDTO) {
        return ResponseEntity.ok(consultantService.update(id, consultantDTO));
    }

    @Override
    public ResponseEntity<Void> deleteConsultant(Long id) {
        consultantService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // ==========================================
    // 2. ENDPOINTS USERS
    // ==========================================

    @Override
    public ResponseEntity<List<UserDTO>> getUsers() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<UserDTO> createUser(UserDTO userDTO) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<UserDTO> getUser(Long id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<Void> updateUser(Long id, UserDTO userDTO) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}