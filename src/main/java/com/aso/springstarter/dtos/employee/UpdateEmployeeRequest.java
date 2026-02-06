package com.aso.springstarter.dtos.employee;

import com.aso.springstarter.entiies.BackofficeUserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class UpdateEmployeeRequest {
    @Size(min = 5, max = 50, message = "Full name must be between 5-50 characters")
    String fullName;

    @Email
    String email;

    @NotNull(message = "Role is required")
    BackofficeUserRole role;
}
