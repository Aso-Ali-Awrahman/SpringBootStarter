package com.aso.springstarter.security;

import javax.crypto.SecretKey;

import java.util.Date;
import java.util.UUID;

import com.aso.springstarter.entiies.UserRole;
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
        this.type = Type.valueOf(claims.get("tokenType").toString());
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

    public boolean isExpired() {
        // if it is expired, it will return true.
        return this.claims.getExpiration().before(new Date());
    }

    public UserPrincipal getUser() {
        return new UserPrincipal(
            UUID.fromString(claims.getSubject()),
            claims.get("email").toString(),
            null,
            UserRole.valueOf(claims.get("role").toString())
        );
    }

    private Claims extractClaims() {
        return Jwts.parser()
            .verifyWith(this.secretKey)
            .build()
            .parseSignedClaims(this.token)
            .getPayload();
    }

    public enum Type {
        ACCESS, REFRESH;

        public boolean isAccessToken() {
            return this == ACCESS;
        }
    }

}
