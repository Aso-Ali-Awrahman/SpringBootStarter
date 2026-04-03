package com.aso.springstarter.security;

import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JwtServiceImpl  implements JwtService {

    private final JwtConfig jwtConfig;

    @Override
    public String generateAccessToken(User user) {
        final var jwt = Jwt.of(user, jwtConfig.getAccessTokenExpiration(), Jwt.Type.ACCESS, jwtConfig.getSecretKey());
        return jwt.getToken();
    }

    @Override
    public String generateRefreshToken(User user) {
        final var jwt = Jwt.of(user, jwtConfig.getRefreshTokenExpiration(), Jwt.Type.REFRESH, jwtConfig.getSecretKey());
        return jwt.getToken();
    }

    @Override
    public Jwt checkAndParseToken(String token) {
        try {
            return new Jwt(token, jwtConfig.getSecretKey());
        } catch (JwtException ex) {
            return null;
        }
    }
}
