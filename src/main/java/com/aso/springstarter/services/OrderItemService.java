package com.aso.springstarter.services;

import java.util.UUID;

public interface OrderItemService {

    void addItemToOrder(UUID orderId, UUID userId, UUID productId, int quantity);

    void removeItemFromOrder(UUID orderId, UUID itemId, UUID userId);

    void updateItemQuantity(UUID orderId, UUID itemId, UUID userId, int quantity);
}
