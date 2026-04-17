package com.aso.springstarter.dtos.order;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.aso.springstarter.entiies.OrderStatus;
import lombok.Value;

@Value
public class OrderWithItemsResponse {
    UUID id;
    OrderStatus status;
    Double totalPrice;
    Instant createdAt;
    List<OrderItemResponse> items;

    @Value
    public static class OrderItemResponse {
        UUID id;
        String productName;
        int quantity;
        Double price;
        Instant createdAt;
    }
}
