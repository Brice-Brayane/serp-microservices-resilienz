package de.ostfalia.serp.customer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customers") // Nom de la table en base de données
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String city;

    @Column(unique = true)
    private String email;
}