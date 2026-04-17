package com.aso.springstarter.services;

import java.util.UUID;

import com.aso.springstarter.dtos.order.InitiateOrderResponse;
import com.aso.springstarter.dtos.order.OrderResponse;
import com.aso.springstarter.dtos.order.OrderWithItemsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    Page<OrderResponse> getAllOrders(Pageable pageable, UUID userId);

    OrderWithItemsResponse getOrderWithItems(UUID orderId, UUID userId);

    InitiateOrderResponse initiateOrder(UUID userId);

    void completeOrder(UUID orderId, UUID userId);

    void removeAllOrderItems(UUID orderId, UUID userId);

    void deleteOrder(UUID orderId, UUID userId);
}
