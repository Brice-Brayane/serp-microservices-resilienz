package de.ostfalia.serp.customer.service;

import de.ostfalia.serp.customer.dto.CustomerDTO;
import de.ostfalia.serp.customer.entity.CustomerEntity;
import de.ostfalia.serp.customer.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service // Indique à Spring que c'est un composant de logique métier
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    // Injection par constructeur (recommandé pour la testabilité)
    public CustomerService(CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    // Récupérer tous les clients
    @Transactional(readOnly = true)
    public List<CustomerDTO> findAll() {
        return customerRepository.findAll().stream()
                .map(entity -> modelMapper.map(entity, CustomerDTO.class)) // Conversion Entity -> DTO
                .collect(Collectors.toList());
    }

    // Créer ou sauvegarder un client
    @Transactional
    public CustomerDTO save(CustomerDTO customerDTO) {
        // Conversion DTO (API) -> Entity (BDD)
        CustomerEntity entity = modelMapper.map(customerDTO, CustomerEntity.class);
        CustomerEntity savedEntity = customerRepository.save(entity);
        // Retourne le résultat converti en DTO pour le contrôleur
        return modelMapper.map(savedEntity, CustomerDTO.class);
    }

    // Trouver un client par ID
    @Transactional(readOnly = true)
    public CustomerDTO findById(Long id) {
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id : " + id));
        return modelMapper.map(entity, CustomerDTO.class);
    }

    // Supprimer un client
    @Transactional
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    // Mettre à jour un client
    @Transactional
    public CustomerDTO update(Long id, CustomerDTO customerDTO) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Impossible de mettre à jour : Client introuvable");
        }
        CustomerEntity entity = modelMapper.map(customerDTO, CustomerEntity.class);
        entity.setId(id); // On force l'ID pour l'écrasement (update)
        return modelMapper.map(customerRepository.save(entity), CustomerDTO.class);
    }
}