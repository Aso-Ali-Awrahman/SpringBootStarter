package com.aso.springstarter.dtos;

import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class ProductStockRequest {
    @Size(min = 1, max = 100, message = "Quantity must be between 1 and 100")
    Integer quantity;
}
