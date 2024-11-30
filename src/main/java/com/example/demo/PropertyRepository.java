package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    /**
     * Find a property by its location (address).
     *
     * @param location the address of the property
     * @return the property with the specified location, or null if no property is found
     */
    Property findByLocation(String location);

    // Standard JPA repository functionality (CRUD methods like save, findById, findAll, delete)
}
