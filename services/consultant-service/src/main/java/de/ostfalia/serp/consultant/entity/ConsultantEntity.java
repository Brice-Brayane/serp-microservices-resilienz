package de.ostfalia.serp.consultant.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "consultants") 
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    
}