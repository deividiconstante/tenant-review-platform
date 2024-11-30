package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Represents a property entity in the system.
 * This class maps to a database table using JPA annotations.
 */
@Entity // Marks this class as a JPA entity (a table in the database).
public class Property {

    @Id // Marks this field as the primary key of the table.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Automatically generates unique IDs for each property record in the database.
    private Long id;

    // The name of the property (e.g., "Apartment 101").
    private String propertyName;

    // The physical address or location of the property.
    private String location;

    // The postal or eircode of the property.
    private String eircode;

    // Tenant's rating of the property's hygiene on a scale (e.g., 1 to 5).
    private int hygiene;

    // Tenant's rating of the property's crowding level on a scale (e.g., 1 to 5).
    private int crowding;

    // Tenant's rating of the property's safety on a scale (e.g., 1 to 5).
    private int safety;

    // Tenant's rating of the landlord on a scale (e.g., 1 to 5).
    private int landlordRating;

    // Tenant's comments or feedback about the property.
    private String comments;

    // A unique identifier for the tenant associated with this property.
    private String tenantId;

    // A unique identifier for the landlord associated with this property.
    private String landlordId;

    // Landlord's comments or feedback about the property.
    private String landlordComment;

    // Getters and Setters allow access to the private fields.
    // They are required for JPA to read and write data.

    public Long getId() {
        return id; // Returns the unique ID of the property.
    }

    public void setId(Long id) {
        this.id = id; // Sets the unique ID of the property.
    }

    public String getPropertyName() {
        return propertyName; // Returns the name of the property.
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName; // Sets the name of the property.
    }

    public String getLocation() {
        return location; // Returns the address of the property.
    }

    public void setLocation(String location) {
        this.location = location; // Sets the address of the property.
    }

    public String getEircode() {
        return eircode; // Returns the eircode of the property.
    }

    public void setEircode(String eircode) {
        this.eircode = eircode; // Sets the eircode of the property.
    }

    public int getHygiene() {
        return hygiene; // Returns the hygiene rating.
    }

    public void setHygiene(int hygiene) {
        this.hygiene = hygiene; // Sets the hygiene rating.
    }

    public int getCrowding() {
        return crowding; // Returns the crowding rating.
    }

    public void setCrowding(int crowding) {
        this.crowding = crowding; // Sets the crowding rating.
    }

    public int getSafety() {
        return safety; // Returns the safety rating.
    }

    public void setSafety(int safety) {
        this.safety = safety; // Sets the safety rating.
    }

    public int getLandlordRating() {
        return landlordRating; // Returns the landlord rating.
    }

    public void setLandlordRating(int landlordRating) {
        this.landlordRating = landlordRating; // Sets the landlord rating.
    }

    public String getComments() {
        return comments; // Returns the tenant's comments.
    }

    public void setComments(String comments) {
        this.comments = comments; // Sets the tenant's comments.
    }

    public String getTenantId() {
        return tenantId; // Returns the unique tenant ID.
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId; // Sets the unique tenant ID.
    }

    public String getLandlordId() {
        return landlordId; // Returns the unique landlord ID.
    }

    public void setLandlordId(String landlordId) {
        this.landlordId = landlordId; // Sets the unique landlord ID.
    }

    public String getLandlordComment() {
        return landlordComment; // Returns the landlord's comments.
    }

    public void setLandlordComment(String landlordComment) {
        this.landlordComment = landlordComment; // Sets the landlord's comments.
    }
}
