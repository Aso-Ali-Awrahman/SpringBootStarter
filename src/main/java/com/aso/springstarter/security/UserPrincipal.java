package com.aso.springstarter.security;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.aso.springstarter.entiies.UserRole;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Value
public class UserPrincipal implements UserDetails, User {
    UUID id;
    String email;
    String password;
    UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+role.name()));
    }

    @Override
    public String getUsername() {
        return "";
    }
}
