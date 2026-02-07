package com.aso.springstarter.dtos.product;

import com.aso.springstarter.ValidationTestBase;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

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
        softly.assertThat(violations)
            .anyMatch(v -> v.getMessage().equals("Name is required"))
            .anyMatch(v -> v.getMessage().equals("Description is required"))
            .anyMatch(v -> v.getMessage().equals("Price must be positive"))
            .anyMatch(v -> v.getMessage().equals("Stock quantity must be positive"));
    }

}
