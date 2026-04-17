package com.aso.springstarter.dtos.order;

import java.util.UUID;

import lombok.Value;

@Value
public class InitiateOrderResponse {
    UUID orderId;
}
