package com.aso.springstarter.dtos.employee;

import com.aso.springstarter.ValidationTestBase;
import com.aso.springstarter.entiies.UserRole;
import com.aso.springstarter.entiies.Gender;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

public class CreateEmployeeRequestTest extends ValidationTestBase {

    @Test
    void shouldValidateCreateEmployeeRequest(SoftAssertions softly) {
        final var request = new CreateEmployeeRequest("Alice Bob", "alice.bob@mail.com", "07501234567", "password234", UserRole.DATA_ENTRY, Gender.FEMALE);

        final var violations = validator.validate(request);

        softly.assertThat(violations).isEmpty();
    }

    @Test
    void shouldNotValidateCreateEmployeeRequest(SoftAssertions softly) {
        final var request = new CreateEmployeeRequest("", "alice_mail.com", "123223", "1234", null, null);

        final var violations = validator.validate(request);

        softly.assertThat(violations).isNotEmpty();
        softly.assertThat(violations).hasSize(6);
        softly.assertThat(violations)
            .anyMatch(v -> v.getMessage().equals("Full name must be between 5-50 characters"))
            .anyMatch(v -> v.getMessage().equals("must be a well-formed email address"))
            .anyMatch(v -> v.getMessage().equals("Phone number must be 11 digits"))
            .anyMatch(v -> v.getMessage().equals("Password must be between 8-50 characters"))
            .anyMatch(v -> v.getMessage().equals("Role is required"))
            .anyMatch(v -> v.getMessage().equals("Gender is required"));
    }

}
