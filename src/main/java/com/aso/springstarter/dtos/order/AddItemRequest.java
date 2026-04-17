package com.aso.springstarter.dtos.order;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;

@Value
public class AddItemRequest {
    @NotNull
    UUID productId;
    @Positive
    Integer quantity;
}
