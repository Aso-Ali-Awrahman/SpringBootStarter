package com.aso.springstarter.security;

import java.util.UUID;

import com.aso.springstarter.entiies.UserRole;

public interface User {
    UUID getId();
    String getEmail();
    String getPassword();
    UserRole getRole();
}
