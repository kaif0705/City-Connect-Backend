package com.cityconnect.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

/**
 * Utility component for handling JWT (JSON Web Token) operations:
 * - Generation
 * - Validation
 * - Parsing
 */
@Component
public class JwtUtil {

    // 1. Read the secret key and expiration from application.properties
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration.ms}")
    private long jwtExpirationMs;

    private SecretKey key;

    // 2. Create the SecretKey object once, after the properties are injected
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // --- Public Methods ---

    /**
     * Generates a new JWT for an authenticated user.
     */
    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(userDetails.getUsername()) // Set the user's username as the token subject
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    /**
     * Validates an incoming JWT.
     * Returns true if the token is valid, false otherwise.
     */
    public boolean validateToken(String token) {
        try {
            // Jwts.parser().verifyWith(key) is a reusable, thread-safe parser
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Log this exception (e.g., e.printStackTrace())
            // We'll just print for the hackathon
            System.out.println("Invalid JWT token: " + e.getMessage());
        }
        return false;
    }

    /**
     * Extracts the username (subject) from a valid JWT.
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // --- Private Helper Methods ---

    /**
     * A generic helper function to extract any "claim" from the token.
     */
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }
}