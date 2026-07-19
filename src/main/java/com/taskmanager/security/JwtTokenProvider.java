package com.taskmanager.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Creates, reads, and validates JWT values used by the application.
 * The signing secret and expiration settings come from application properties.
 */
@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long expirationMs;

    /**
     * Creates the token provider from configured security properties.
     * Input: the JWT secret string and token expiration time in milliseconds.
     * Output: a provider ready to sign and validate tokens.
     */
    public JwtTokenProvider(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long expirationMs) {
        // The configured secret is converted into the HMAC key used for signing.
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    /**
     * Builds a signed JWT for one username.
     * Input: the username that becomes the token subject.
     * Output: a compact JWT string with issue and expiration times.
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    /**
        * Reads the username stored inside a signed token.
        * Input: a JWT string that should already be valid.
        * Output: the username stored in the token subject.
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    /**
     * Checks whether a token is signed correctly and still valid.
     * Input: the JWT string to inspect.
     * Output: true when parsing succeeds, otherwise false.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
