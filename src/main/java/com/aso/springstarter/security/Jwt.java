package com.aso.springstarter.security;

import javax.crypto.SecretKey;

import java.util.Date;
import java.util.UUID;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Getter;

public class Jwt {

    @Getter
    private final String token;
    private final Claims claims;
    @Getter
    private final Type type;
    private final SecretKey secretKey;

    public Jwt(String token, SecretKey secretKey) {
        this.token = token;
        this.secretKey = secretKey;
        this.claims = extractClaims();
        this.type = claims.get("tokenType", Type.class);
    }

    public static Jwt of(User user, int expiration, Type tokenType, SecretKey secretKey) {
        final var token = Jwts.builder()
            .subject(user.getId().toString())
            .claim("jwtId", UUID.randomUUID().toString())
            .claim("email", user.getEmail())
            .claim("role", user.getRole())
            .claim("tokenType", tokenType)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000L * expiration))
            .signWith(secretKey)
            .compact();

        return new Jwt(token, secretKey);
    }

    private Claims extractClaims() {
        return Jwts.parser()
            .verifyWith(this.secretKey)
            .build()
            .parseSignedClaims(this.token)
            .getPayload();
    }

    public enum Type {
        ACCESS, REFRESH
    }

}
