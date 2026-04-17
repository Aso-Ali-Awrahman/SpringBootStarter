package com.aso.springstarter.dtos.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.aso.springstarter.entiies.OrderStatus;
import lombok.Value;

@Value
public class OrderResponse {
    UUID id;
    OrderStatus status;
    Double totalPrice;
    Instant createdAt;
}
