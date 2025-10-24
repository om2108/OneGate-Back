package com.project.society.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    // Minimum recommended key length for HS256 is 32 bytes
    private final Key key = Keys.hmacShaKeyFor("replace_this_with_a_very_long_secure_random_string!".getBytes());

    public String generateToken(String email, String role) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 1000 * 60 * 60 * 24)) // 24h
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
