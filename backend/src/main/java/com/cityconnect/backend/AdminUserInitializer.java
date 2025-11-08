package com.cityconnect.backend;

import com.cityconnect.backend.entity.User;
import com.cityconnect.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // 1. Import @Value
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- 2. INJECT VALUES FROM application.properties ---
    @Value("${admin.bootstrap.username}")
    private String adminUsername;

    @Value("${admin.bootstrap.email}")
    private String adminEmail;

    @Value("${admin.bootstrap.password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.existsByRole("ROLE_ADMIN")) {
            System.out.println("Admin user already exists. Skipping creation.");
        } else {
            // 3. Use the injected variables instead of hardcoded strings
            User adminUser = new User();
            adminUser.setUsername(adminUsername);
            adminUser.setEmail(adminEmail);
            adminUser.setPassword(passwordEncoder.encode(adminPassword));
            adminUser.setRole("ROLE_ADMIN");

            userRepository.save(adminUser);

            System.out.println("Default ADMIN user created successfully.");
            System.out.println("Username: " + adminUsername);
            System.out.println("Password: " + adminPassword);
        }
    }
}