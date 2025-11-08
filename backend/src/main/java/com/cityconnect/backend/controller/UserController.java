package com.cityconnect.backend.controller;

import com.cityconnect.backend.dto.UserProfileResponse;
import com.cityconnect.backend.dto.UserProfileUpdateRequest;
import com.cityconnect.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST API Controller for managing User profiles.
 * All endpoints in this controller are secured and require an authenticated user.
 */
@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "http://localhost:5173") // Allow requests from our React frontend
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Endpoint for retrieving the profile of the currently authenticated user.
     * Mapped to GET /api/v1/users/me
     *
     * @return ResponseEntity with UserProfileResponse and HTTP 200.
     */
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getUserProfile() {
        // The service gets the user from the SecurityContext
        UserProfileResponse userProfile = userService.getUserProfile();
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

    /**
     * Endpoint for updating the profile of the currently authenticated user.
     * Mapped to PUT /api/v1/users/me
     *
     * @param updateRequest DTO containing the fields to update (e.g., email).
     * @return ResponseEntity with the updated UserProfileResponse and HTTP 200.
     */
    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateUserProfile(
            @Valid @RequestBody UserProfileUpdateRequest updateRequest) {

        UserProfileResponse updatedProfile = userService.updateUserProfile(updateRequest);
        return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
    }

    /**
     * Endpoint for deleting the profile of the currently authenticated user.
     * Mapped to DELETE /api/v1/users/me
     *
     * @return ResponseEntity with HTTP 204 (No Content).
     */
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUserProfile() {
        userService.deleteUserProfile();
        // Return 204 No Content, which is the standard for a successful DELETE
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}