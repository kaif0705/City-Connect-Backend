package com.cityconnect.backend.service;

import com.cityconnect.backend.dto.AuthResponse;
import com.cityconnect.backend.dto.LoginRequest;
import com.cityconnect.backend.dto.RegisterRequest;
import com.cityconnect.backend.entity.User;
import com.cityconnect.backend.exception.DuplicateResourceException;
import com.cityconnect.backend.repository.UserRepository;
import com.cityconnect.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public AuthResponse registerUser(RegisterRequest registerRequest) {

        // 1. Check if username already exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new DuplicateResourceException("Username is already taken: " + registerRequest.getUsername());
        }

        // 2. Check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new DuplicateResourceException("Email is already registered: " + registerRequest.getEmail());
        }

        // 3. Create new user object
        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // 4. Set default role (as we discussed)
        newUser.setRole("ROLE_CITIZEN");

        // 5. Save user to database
        userRepository.save(newUser);

        // 6. Generate JWT and return response (auto-login after register)
        String token = jwtUtil.generateToken(newUser);
        return new AuthResponse(token, newUser.getUsername(), newUser.getRole());
    }

    @Override
    public AuthResponse loginUser(LoginRequest loginRequest) {

        // 1. Authenticate the user
        // This will throw BadCredentialsException if login is incorrect
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // 2. If authentication is successful, set it in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Get the authenticated user principal
        User user = (User) authentication.getPrincipal();

        // 4. Generate JWT
        String token = jwtUtil.generateToken(user);

        // 5. Return the response
        return new AuthResponse(token, user.getUsername(), user.getRole());
    }
}