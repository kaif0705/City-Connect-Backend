package com.cityconnect.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class IssueRequest {

    // Use validation to ensure we always get a title
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Category is required")
    private String category;

    // Latitude and Longitude are optional
    private Double latitude;
    private Double longitude;

    // Note: We deliberately do NOT include 'id', 'status', or 'createdAt'.
    // These will be set by the backend, not by the user.
}

