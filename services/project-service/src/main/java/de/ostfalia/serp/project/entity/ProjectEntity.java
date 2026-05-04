package de.ostfalia.serp.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects") // Nom de la table en base de données
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "start_date")
    private LocalDate start;

    @Column(name = "end_date")
    private LocalDate end;

    private String status;

    // PATTERN CLOUD-NATIVE : On stocke l'ID du client externe, pas l'objet entier
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    // On stocke simplement la liste des IDs des consultants travaillant sur ce projet
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "project_staff", // Nom de la table de liaison générée automatiquement
            joinColumns = @JoinColumn(name = "project_id")
    )
    @Column(name = "consultant_id")
    @Builder.Default
    private List<Long> consultantIds = new ArrayList<>();
}