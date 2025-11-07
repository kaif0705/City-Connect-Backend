package com.cityconnect.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String token; // The JWT token
    private String username;
    private String role; // e.g., "ROLE_CITIZEN"

    // We can also add other safe user info here if needed
}