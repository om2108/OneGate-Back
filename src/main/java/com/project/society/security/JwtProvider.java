// src/main/java/com/project/society/security/JwtProvider.java
package com.project.society.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms:3600000}")
    private long jwtExpirationMs;

    // optional longer expiration for "remember me" (defaults to 30 days)
    @Value("${app.jwt.remember-expiration-ms:2592000000}")
    private long jwtRememberExpirationMs;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String userId, String email, String role) {
        return generateToken(userId, email, role, false);
    }

    public String generateToken(String userId, String email, String role, boolean rememberMe) {

        Date now = new Date();
        long expMs = rememberMe ? jwtRememberExpirationMs : jwtExpirationMs;
        Date expiry = new Date(now.getTime() + expMs);

        return Jwts.builder()
                .claim("id", userId)            // ⭐ ADD USER ID
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> validateToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
    }
}
