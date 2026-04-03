package com.aso.springstarter.controllers;

import java.time.Instant;

import com.aso.springstarter.entiies.EmployeeEntity;
import com.aso.springstarter.entiies.Gender;
import com.aso.springstarter.entiies.UserRole;
import com.aso.springstarter.entiies.UserStatus;
import com.aso.springstarter.repositories.EmployeeRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication Controller")
@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("public/login")
    @ResponseStatus(HttpStatus.OK)
    public void login() {

    }

    @PostMapping("public/register/customer")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerCustomer() {
        employeeRepository.save(new EmployeeEntity(
            null,
            "Aso Ali",
            "07501234567",
            "aso@mail.com",
            passwordEncoder.encode("app1"),
            UserRole.ADMIN,
            UserStatus.ACTIVE,
            Gender.MALE,
            Instant.now()
        ));
    }

    @GetMapping("protected/me")
    @ResponseStatus(HttpStatus.OK)
    public void getMe() {

    }

}
