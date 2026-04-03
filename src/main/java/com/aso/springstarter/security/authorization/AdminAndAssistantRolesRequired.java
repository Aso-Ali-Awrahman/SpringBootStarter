package com.aso.springstarter.security.authorization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("""
    hasAnyRole(
        T(com.aso.springstarter.entiies.UserRole).ADMIN.name(),
        T(com.aso.springstarter.entiies.UserRole).ASSISTANT.name()
    )
""")
public @interface AdminAndAssistantRolesRequired {
}
