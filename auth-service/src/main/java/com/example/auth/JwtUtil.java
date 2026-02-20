package com.example.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    String secret;

    @Value("${jwt.expiration}")
    long expiration;

    public String generateToken(String username) {
        int roleNum = (int) (Math.random() * 2);
        String role = roleNum == 0 ? "USER" : "ADMIN";
        
        return Jwts.builder() // Create a new JWT builder
            .setSubject(username) // Set the username as the subject claim
            .claim("role", role) // Add the role as a custom claim
            .setIssuedAt(new Date()) // Set the token issuance time to now
            .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Set expiration time based on configured duration
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256) // Sign the token with HMAC-SHA256 using the secret key
            .compact(); // Build and serialize the token to a compact string
    }
}
