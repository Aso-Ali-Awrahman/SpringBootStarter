package com.aso.springstarter.controllers;

import java.time.Instant;

import com.aso.springstarter.dtos.auth.LoginRequest;
import com.aso.springstarter.dtos.auth.MeResponse;
import com.aso.springstarter.dtos.auth.RegisterCustomerRequest;
import com.aso.springstarter.dtos.auth.TokenResponse;
import com.aso.springstarter.entiies.EmployeeEntity;
import com.aso.springstarter.entiies.Gender;
import com.aso.springstarter.entiies.UserRole;
import com.aso.springstarter.entiies.UserStatus;
import com.aso.springstarter.repositories.EmployeeRepository;
import com.aso.springstarter.security.JwtConfig;
import com.aso.springstarter.security.JwtService;
import com.aso.springstarter.security.User;
import com.aso.springstarter.security.UserPrincipal;
import com.aso.springstarter.services.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication Controller")
@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final CustomerService customerService;


    @PostMapping("public/auth/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        final var authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(), // principal
                request.getPassword() // credentials
            )
        );

        final var user = (UserPrincipal) authentication.getPrincipal();
        final var accessToken = jwtService.generateAccessToken(user);
        final var refreshToken = jwtService.generateRefreshToken(user);

        final var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setPath("public/auth/refresh");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);

        response.addCookie(cookie);

        return new TokenResponse(accessToken);
    }

    @PostMapping("public/auth/register/customer")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerCustomer(@Valid @RequestBody RegisterCustomerRequest request) {
        customerService.createCustomer(request);
    }

    @PostMapping("public/auth/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@CookieValue("refreshToken") String refreshToken) {
        final var jwt = jwtService.checkAndParseToken(refreshToken);
        if (jwt == null || (!jwt.getType().isRefreshToken() || jwt.isExpired())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        final var accessToken = jwtService.generateAccessToken(jwt.getUser());
        return ResponseEntity.ok(new TokenResponse(accessToken));
    }

    @GetMapping("protected/auth/me")
    @ResponseStatus(HttpStatus.OK)
    public MeResponse getMe(@AuthenticationPrincipal User userPrincipal) {
        return new MeResponse(
            userPrincipal.getId(),
            userPrincipal.getEmail(),
            userPrincipal.getRole()
        );
    }

}
