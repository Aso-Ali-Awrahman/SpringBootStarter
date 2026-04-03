package com.aso.springstarter.dtos.auth;

import java.util.UUID;

import com.aso.springstarter.entiies.UserRole;
import lombok.Value;

@Value
public class MeResponse {
    UUID id;
    String email;
    UserRole role;
}
