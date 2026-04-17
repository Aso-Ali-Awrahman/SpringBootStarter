package com.aso.springstarter.dtos.order;

import jakarta.validation.constraints.Positive;
import lombok.Value;

@Value
public class UpdateItemQuantityRequest {
    @Positive
    Integer quantity;
}
