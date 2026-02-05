package com.aso.springstarter.dtos;

import lombok.Value;
import org.hibernate.validator.constraints.Range;

@Value
public class ProductStockRequest {
    @Range(min = 1, max = 100, message = "Quantity must be between 1 and 100")
    Integer quantity;
}
