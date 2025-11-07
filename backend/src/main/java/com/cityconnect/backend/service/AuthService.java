package com.cityconnect.backend.service;

import com.cityconnect.backend.dto.AuthResponse;
import com.cityconnect.backend.dto.LoginRequest;
import com.cityconnect.backend.dto.RegisterRequest;

public interface AuthService {

    /**
     * Registers a new user, hashes their password, and returns a JWT.
     * @param registerRequest DTO containing username, email, and password.
     * @return AuthResponse DTO containing a JWT, username, and role.
     */
    AuthResponse registerUser(RegisterRequest registerRequest);

    /**
     * Authenticates a user and returns a JWT.
     * @param loginRequest DTO containing username and password.
     * @return AuthResponse DTO containing a JWT, username, and role.
     */
    AuthResponse loginUser(LoginRequest loginRequest);
}