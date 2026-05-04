package de.ostfalia.serp.entry.controller;

import de.ostfalia.serp.entry.dto.Error; // Import du DTO généré pour Entry
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // Intercepte les exceptions de tous les contrôleurs du service Entry
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Error> handleRuntime(RuntimeException ex) {
        Error error = new Error();
        error.setCode(500);
        error.setMessage(ex.getMessage());
        
        // Retourne l'objet Error conforme au YAML avec un statut HTTP 500
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}