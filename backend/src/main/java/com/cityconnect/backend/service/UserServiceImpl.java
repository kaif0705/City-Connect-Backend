package com.cityconnect.backend.service;

import com.cityconnect.backend.dto.UserProfileUpdateRequest;
import com.cityconnect.backend.dto.UserProfileResponse;
import com.cityconnect.backend.entity.Issue;
import com.cityconnect.backend.entity.User;
import com.cityconnect.backend.exception.DuplicateResourceException;
import com.cityconnect.backend.repository.IssueRepository;
import com.cityconnect.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the UserService interface.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueService issueService; // We inject IssueService to use its delete logic

    /**
     * Helper method to get the currently authenticated user.
     */
    private User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        // This should be unreachable if endpoints are secured, but it's a good safeguard.
        throw new IllegalStateException("User not authenticated.");
    }

    /**
     * Helper method to map a User entity to a safe response DTO.
     */
    private UserProfileResponse mapToResponse(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    /**
     * Retrieves the profile of the currently authenticated user.
     */
    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile() {
        User user = getAuthenticatedUser();
        return mapToResponse(user);
    }

    /**
     * Updates the profile of the currently authenticated user.
     * Currently only supports updating the email.
     */
    @Override
    @Transactional
    public UserProfileResponse updateUserProfile(UserProfileUpdateRequest updateRequest) {
        User user = getAuthenticatedUser();
        String newEmail = updateRequest.getEmail();

        // Check if the new email is already taken by ANOTHER user
        if (userRepository.existsByEmailAndIdNot(newEmail, user.getId())) {
            throw new DuplicateResourceException("Email is already taken: " + newEmail);
        }

        // Update the email
        user.setEmail(newEmail);

        // Save and return the updated user
        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    /**
     * Deletes the currently authenticated user and ALL their associated data.
     * This includes issues, comments, and uploaded files.
     */
    @Override
    @Transactional
    public void deleteUserProfile() {
        User user = getAuthenticatedUser();

        // 1. Find all issues created by this user.
        //    We MUST use the new findByUser method from IssueRepository.
        List<Issue> issuesToDelete = issueRepository.findByUser(user);

        // 2. Loop through each issue and call our existing IssueService.deleteIssue().
        //    This is CRITICAL. This is what triggers the cascade delete
        //    of Comments (from Issue entity) and image files (from FileStorageService).
        for (Issue issue : issuesToDelete) {
            issueService.deleteIssue(issue.getId());
        }

        // 3. After all associated issues/comments/files are deleted,
        //    we can safely delete the user.
        userRepository.delete(user);
    }
}