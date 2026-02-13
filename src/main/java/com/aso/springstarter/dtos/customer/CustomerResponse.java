package com.aso.springstarter.dtos.customer;

import java.time.Instant;
import java.util.UUID;

import com.aso.springstarter.entiies.Gender;
import lombok.Value;

@Value
public class CustomerResponse {
    UUID id;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    Gender gender;
    Instant createdAt;
}
