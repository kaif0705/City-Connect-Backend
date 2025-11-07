package com.cityconnect.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data // Lombok: Adds getters, setters, toString, etc.
@Entity
@Table(name = "issues") // Defines the table name
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing ID
    private Long id;

    @Column(nullable = false) // Cannot be null
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String category;

    // Default status to PENDING when a new issue is created
    @Column(nullable = false)
    private String status = "PENDING";

    private Double latitude;
    private Double longitude;

    //Image URL
    private String imageUrl;

    // Automatically sets the timestamp when the entity is created
    @CreationTimestamp
    private Instant createdAt;

    // We will add the @ManyToOne relationship to the User entity in Slice 4
}
