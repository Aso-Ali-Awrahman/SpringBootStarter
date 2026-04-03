package com.aso.springstarter.security;

import javax.crypto.SecretKey;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.jwt")
@RequiredArgsConstructor
@Setter
public class JwtConfig {

//    @Getter
    private String secret; // must be more than 64
    @Getter
    private int accessTokenExpiration; // 15 minutes
    @Getter
    private int refreshTokenExpiration;

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

}
