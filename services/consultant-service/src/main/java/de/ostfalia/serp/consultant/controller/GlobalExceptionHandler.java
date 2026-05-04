package de.ostfalia.serp.consultant.controller;

import de.ostfalia.serp.consultant.dto.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // Au lieu d'écrire des blocs try { ... } catch { ... }
// Intercepte les exceptions de tous les contrôleurs
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Error> handleRuntime(RuntimeException ex) {
        Error error = new Error();
        error.setCode(500);
        error.setMessage(ex.getMessage());
        
        // Renvoie l'objet Error (défini dans ton YAML) avec un statut HTTP 500
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

// C'est lui qui transforme un crash serveur "moche" 
// en un message JSON propre que ton frontend React peut lire et afficher proprement.