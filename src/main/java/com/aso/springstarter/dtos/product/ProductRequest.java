package com.aso.springstarter.dtos.product;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class ProductRequest {
    @NotBlank(message = "Name is required")
    String name;
    @NotBlank(message = "Description is required")
    String description;
    @Min(value = 1, message = "Price must be positive")
    Double price;
    @Min(value = 1, message = "Stock quantity must be positive")
    @Max(value = 100, message = "Stock quantity must be less than 100")
    Integer stockQuantity;
}
