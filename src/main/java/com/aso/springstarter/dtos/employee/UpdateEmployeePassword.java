package com.aso.springstarter.dtos.employee;

import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class UpdateEmployeePassword {
    @Size(min = 8, max = 50, message = "Password must be between 8-50 characters")
    String newPassword;
}
