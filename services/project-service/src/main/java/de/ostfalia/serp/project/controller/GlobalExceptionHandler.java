package de.ostfalia.serp.project.controller;

import de.ostfalia.serp.project.dto.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // Intercepte les exceptions de tous les contrôleurs du microservice Project
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Error> handleRuntime(RuntimeException ex) {
        Error error = new Error();
        // Harmonisation : On utilise les setters du DTO généré par OpenAPI
        error.setCode(500);
        error.setMessage("Project Service Error: " + ex.getMessage());
        
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Optionnel : Tu peux ajouter un handler spécifique pour les ressources non trouvées
    @ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
    public ResponseEntity<Error> handleNotFound(org.springframework.web.server.ResponseStatusException ex) {
        Error error = new Error();
        error.setCode(ex.getStatusCode().value());
        error.setMessage(ex.getReason());
        return new ResponseEntity<>(error, ex.getStatusCode());
    }
}