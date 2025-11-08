package com.cityconnect.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for receiving user profile updates from the frontend.
 * We are only allowing the email to be updated for now.
 */
@Data
public class UserProfileUpdateRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;
}