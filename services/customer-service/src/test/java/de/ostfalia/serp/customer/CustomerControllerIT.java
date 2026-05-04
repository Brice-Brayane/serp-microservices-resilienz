package de.ostfalia.serp.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ostfalia.serp.customer.dto.CustomerDTO;
import de.ostfalia.serp.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // Charge tout le contexte Spring
@AutoConfigureMockMvc // Permet de simuler des appels HTTP
public class CustomerControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper; // Pour convertir les objets en JSON

    @BeforeEach
    void setup() {
        customerRepository.deleteAll(); // On repart d'une base propre avant chaque test
    }

    @Test
    void shouldCreateCustomerSuccessfully() throws Exception {
        // 1. Préparation de la donnée (DTO)
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("Master Test");
        customerDTO.setCity("Wolfenbüttel");
     

        // 2. Exécution de l'appel POST
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)))
                
                // 3. Vérification du résultat (Assertions)
                .andExpect(status().isCreated()) // On attend un code 201
                .andExpect(jsonPath("$.name").value("Master Test"))
                .andExpect(jsonPath("$.customerId").exists());

        // 4. Vérification finale en base de données
        assertThat(customerRepository.findAll()).hasSize(1);
        assertThat(customerRepository.findAll().get(0).getName()).isEqualTo("Master Test");
    }
}