package com.cityconnect.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for safely sending user profile data to the frontend.
 * It omits the password and other sensitive fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

    private Long id;
    private String username;
    private String email;
    private String role;

    // We can add more fields here later if needed,
    // e.g., String fullName, String profileImageUrl
}