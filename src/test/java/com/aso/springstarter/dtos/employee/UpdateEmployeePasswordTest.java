package com.aso.springstarter.dtos.employee;

import com.aso.springstarter.ValidationTestBase;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

public class UpdateEmployeePasswordTest extends ValidationTestBase {

    @Test
    void shouldValidateUpdateEmployeePassword(SoftAssertions softly) {
        final var request = new UpdateEmployeePassword("Spring Boot");

        final var violations = validator.validate(request);

        softly.assertThat(violations).isEmpty();
    }

    @Test
    void shouldNotValidateUpdateEmployeePassword(SoftAssertions softly) {
        final var request = new UpdateEmployeePassword("");

        final var violations = validator.validate(request);

        softly.assertThat(violations).isNotEmpty();
        softly.assertThat(violations).hasSize(1);
        softly.assertThat(violations).anyMatch(v -> v.getMessage().equals("Password must be between 8-50 characters"));
    }

}
