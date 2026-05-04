package de.ostfalia.serp.customer.controller;

import de.ostfalia.serp.customer.api.CustomersApi;
import de.ostfalia.serp.customer.dto.CustomerDTO;
import de.ostfalia.serp.customer.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Implémentation du contrôleur généré par OpenAPI.
 * Le mot-clé 'implements CustomersApi' garantit la conformité au contrat.
 */
@RestController
public class CustomerController implements CustomersApi {  // <-- On implémente le contrat

    // On injecte le service pour séparer la logique API de la logique métier
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // POST /customers : Créer un nouveau client
    @Override
    public ResponseEntity<CustomerDTO> createCustomer(@Valid CustomerDTO customerDTO) {
        // Appelle le service pour sauvegarder en BDD et retourne 201 Created
        CustomerDTO savedCustomer = customerService.save(customerDTO);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    // GET /customers : Récupérer la liste de tous les clients
    @Override
    public ResponseEntity<List<CustomerDTO>> getCustomers() {
        // Retourne la liste avec un code 200 OK
        return ResponseEntity.ok(customerService.findAll());
    }

    // GET /customers/{id} : Récupérer un client spécifique
    @Override
    public ResponseEntity<CustomerDTO> getCustomerById(@NotNull Long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    // PUT /customers/{id} : Mettre à jour un client
    @Override
    public ResponseEntity<CustomerDTO> updateCustomer(@NotNull Long id, @Valid CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.update(id, customerDTO));
    }

    // DELETE /customers/{id} : Supprimer un client
    @Override
    public ResponseEntity<Void> deleteCustomer(@NotNull Long id) {
        customerService.deleteById(id);
        return ResponseEntity.ok().build(); // Retourne 200 OK comme spécifié dans l'interface
    }
}