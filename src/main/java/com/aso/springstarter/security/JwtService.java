package com.aso.springstarter.security;

public interface JwtService {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    Jwt checkAndParseToken(String token);
}
