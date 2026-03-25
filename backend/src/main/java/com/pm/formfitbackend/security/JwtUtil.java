package com.pm.formfitbackend.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "16a0e39b34f036cb2ed6b0d304e537a534784f0c0bdf4a18c4f9722351296087b60b6b8cdd77ce3d99c03363df033ad8f63d79e2f1aa128f9cc1af86763f46b3";
    private final long EXPIRATION = 1000 * 60 * 60 * 24;

    public String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public Long extractUserId(String token) {
        return Long.parseLong(
                extractClaims(token).getSubject()
        );
    }

    public boolean isValid(String token) {
        try {
            return extractClaims(token).getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}