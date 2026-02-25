package com.aso.springstarter.dtos.employee;

import java.time.Instant;
import java.util.UUID;

import com.aso.springstarter.entiies.UserRole;
import com.aso.springstarter.entiies.Gender;
import com.aso.springstarter.entiies.UserStatus;
import lombok.Value;

@Value
public class EmployeeResponse {
    UUID id;
    String fullName;
    String phoneNumber;
    String email;
    UserRole role;
    UserStatus status;
    Gender gender;
    Instant createdAt;
}
