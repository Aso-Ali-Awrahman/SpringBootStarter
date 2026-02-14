package com.aso.springstarter.dtos.employee;

import java.time.Instant;
import java.util.UUID;

import com.aso.springstarter.entiies.BackofficeUserRole;
import com.aso.springstarter.entiies.Gender;
import com.aso.springstarter.entiies.UserStatus;
import lombok.Value;

@Value
public class EmployeeResponse {
    UUID id;
    String fullName;
    String phoneNumber;
    String email;
    BackofficeUserRole role;
    UserStatus status;
    Gender gender;
    Instant createdAt;
}
