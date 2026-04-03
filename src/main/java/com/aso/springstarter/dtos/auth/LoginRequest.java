package com.aso.springstarter.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class LoginRequest {
    @Email(message = "Invalid email format")
    String email;
    @NotBlank(message = "Password is required")
    String password;
}
