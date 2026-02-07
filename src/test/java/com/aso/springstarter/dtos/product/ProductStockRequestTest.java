package com.aso.springstarter.dtos.product;

import com.aso.springstarter.ValidationTestBase;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

public class ProductStockRequestTest extends ValidationTestBase {

    @Test
    void shouldValidateProductStockRequest(SoftAssertions softly) {
        final var request = new ProductStockRequest(10);

        final var violations = validator.validate(request);

        softly.assertThat(violations).isEmpty();
    }

    @Test
    void shouldNotValidateProductStockRequest(SoftAssertions softly) {
        final var request = new ProductStockRequest(-10);

        final var violations = validator.validate(request);

        softly.assertThat(violations).isNotEmpty();
        softly.assertThat(violations).hasSize(1);
        softly.assertThat(violations)
            .anyMatch(v -> v.getMessage().equals("Quantity must be between 1 and 100"));
    }

}
