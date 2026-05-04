package de.ostfalia.serp.customer.repository;

import de.ostfalia.serp.customer.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    // Cette interface hérite de toutes les méthodes CRUD (save, delete, find...)
}