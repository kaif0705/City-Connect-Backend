package com.cityconnect.backend.service;

import com.cityconnect.backend.dto.UserProfileUpdateRequest;
import com.cityconnect.backend.dto.UserProfileResponse;

/**
 * Interface for the user service, which handles profile management logic.
 */
public interface UserService {

    /**
     * Retrieves the profile of the currently authenticated user.
     *
     * @return UserProfileResponse DTO with safe user data.
     */
    UserProfileResponse getUserProfile();

    /**
     * Updates the profile of the currently authenticated user.
     *
     * @param updateRequest DTO containing the fields to update (e.g., email).
     * @return The updated UserProfileResponse DTO.
     */
    UserProfileResponse updateUserProfile(UserProfileUpdateRequest updateRequest);

    /**
     * Deletes the profile (and all associated data) of the currently authenticated user.
     */
    void deleteUserProfile();
}