package com.aso.springstarter.dtos.employee;

import com.aso.springstarter.entiies.BackofficeUserRole;
import com.aso.springstarter.entiies.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class CreateEmployeeRequest {
    @Size(min = 5, max = 50, message = "Full name must be between 5-50 characters")
    String fullName;

    @Email
    String email;

    @Size(min = 8, max = 50, message = "Password must be between 8-50 characters")
    String password;

    @NotNull(message = "Role is required")
    BackofficeUserRole role;

    @NotNull(message = "Gender is required")
    Gender gender;
}
