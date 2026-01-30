package com.aso.springstarter.dtos;

import com.aso.springstarter.ValidationTestBase;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SoftAssertionsExtension.class)
class ProductRequestTest extends ValidationTestBase {

    @Test
    void shouldValidateProductRequest(SoftAssertions softly) {
        final var request = new ProductRequest("Laptop", "high end laptop", 100.0, 50);

        final var violations = validator.validate(request);

        softly.assertThat(violations).isEmpty();
    }

    @Test
    void shouldNotValidateProductRequest(SoftAssertions softly) {
        final var request = new ProductRequest("", "", -10.0, -10);

        final var violations = validator.validate(request);

        softly.assertThat(violations).isNotEmpty();
        softly.assertThat(violations).hasSize(4);
    }

}
