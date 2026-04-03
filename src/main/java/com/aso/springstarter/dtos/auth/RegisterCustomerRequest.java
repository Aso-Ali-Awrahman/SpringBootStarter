package com.aso.springstarter.dtos.auth;

import com.aso.springstarter.entiies.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class RegisterCustomerRequest {
    @Size(max = 20)
    @NotBlank(message = "First name is required")
    String firstName;

    @Size(max = 20)
    @NotBlank(message = "Last name is required")
    String lastName;

    @Email
    String email;

    @Size(min = 11, max = 11, message = "Phone number must be 11 digits")
    String phoneNumber;

    @Size(min = 8, max = 50, message = "Password must be between 8-50 characters")
    String password;

    @NotNull(message = "Gender is required")
    Gender gender;
}
