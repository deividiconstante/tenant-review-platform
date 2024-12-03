package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    /**
     * Find a property by its location (address), case-sensitive.
     *
     * This method uses Spring Data JPA's query derivation to automatically generate a query.
     *
     * @param location the exact address of the property
     * @return the property with the specified location, or null if no property is found
     */
    Property findByLocation(String location);

    /**
     * Find a property by its location (address), case-insensitive.
     *
     * This method uses a custom query to perform a case-insensitive search by converting both the
     * database column and the input parameter to lowercase.
     *
     * @param location the exact address of the property
     * @return the property with the specified location, or null if no property is found
     */
    @Query("SELECT p FROM Property p WHERE LOWER(p.location) = LOWER(:location)")
    Property findByLocationIgnoreCase(@Param("location") String location);

    /**
     * Find properties whose location partially matches the provided address, case-insensitive.
     *
     * This method uses a custom query with the SQL LIKE operator to match parts of the address.
     * Useful for search functionality where users may provide incomplete or partial addresses.
     *
     * @param location the partial address to search for
     * @return a list of properties whose location contains the specified string, case-insensitive
     */
    @Query("SELECT p FROM Property p WHERE LOWER(p.location) LIKE LOWER(CONCAT('%', :location, '%'))")
    List<Property> findByLocationContainingIgnoreCase(@Param("location") String location);

    // Other standard JPA repository methods (e.g., save, findById, findAll, delete) are inherited from JpaRepository
}
