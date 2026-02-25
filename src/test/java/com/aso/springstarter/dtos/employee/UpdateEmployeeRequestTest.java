package com.aso.springstarter.dtos.employee;

import com.aso.springstarter.ValidationTestBase;
import com.aso.springstarter.entiies.UserRole;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

public class UpdateEmployeeRequestTest extends ValidationTestBase {

    @Test
    void shouldValidateUpdateEmployeeRequest(SoftAssertions softly) {
        final var request = new UpdateEmployeeRequest("Alice Job", "alice@mail.com", UserRole.ADMIN);

        final var violations = validator.validate(request);

        softly.assertThat(violations).isEmpty();
    }

    @Test
    void shouldNotValidateUpdateEmployeeRequest(SoftAssertions softly) {
        final var request = new UpdateEmployeeRequest("", ":", null);

        final var violations = validator.validate(request);

        softly.assertThat(violations).isNotEmpty();
        softly.assertThat(violations).hasSize(3);
        softly.assertThat(violations)
            .anyMatch(v -> v.getMessage().equals("Full name must be between 5-50 characters"))
            .anyMatch(v -> v.getMessage().equals("must be a well-formed email address"))
            .anyMatch(v -> v.getMessage().equals("Role is required"));
    }

}
