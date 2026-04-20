package com.aso.springstarter.services;

import java.time.Instant;
import java.util.UUID;

import com.aso.springstarter.entiies.OrderEntity;
import com.aso.springstarter.entiies.OrderItemEntity;
import com.aso.springstarter.entiies.OrderStatus;
import com.aso.springstarter.entiies.ProductStatus;
import com.aso.springstarter.repositories.OrderItemRepository;
import com.aso.springstarter.repositories.OrderRepository;
import com.aso.springstarter.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;


    @Override
    @Transactional
    public void addItemToOrder(UUID orderId, UUID userId, UUID productId, int quantity) {
        final var order = getAndValidateOrder(orderId, userId);
        if (order.isNotAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order must be in INITIATED or PENDING state to add items");
        }
        final var product = productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        if (product.get().getStatus() != ProductStatus.AVAILABLE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product is not available");
        }
        if (product.get().getStockQuantity() < quantity) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock");
        }
        if (order.getOrderItems().isEmpty()) {
            order.setStatus(OrderStatus.PENDING);
        }
        final var orderItem = new OrderItemEntity(
            null,
            order,
            product.get(),
            quantity,
            quantity * product.get().getPrice(),
            Instant.now(),
            Instant.now()
        );
        orderItemRepository.save(orderItem);
    }

    @Override
    @Transactional
    public void removeItemFromOrder(UUID orderId, UUID itemId, UUID userId) {
        final var order = orderRepository.findByIdAndCustomerId(orderId, userId);
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        if (order.isNotAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order must be in INITIATED or PENDING state to remove items");
        }
        final var item = orderItemRepository.findByIdAndOrderId(itemId, orderId);
        if (item.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order item not found");
        }
        order.getOrderItems().remove(item.get());
        if (order.getOrderItems().isEmpty()) {
            order.setStatus(OrderStatus.INITIATED);
        }
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void updateItemQuantity(UUID orderId, UUID itemId, UUID userId, int quantity) {
        final var order = getAndValidateOrder(orderId, userId);
        if (order.isNotAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order must be in INITIATED or PENDING state to update items");
        }
        orderItemRepository.findByIdAndOrderId(itemId, order.getId())
            .map(orderItem ->
                productRepository.findById(orderItem.getProduct().getId())
                    .map(product -> {
                        if (product.getStockQuantity() < quantity) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock");
                        }
                        orderItem.setQuantity(quantity);
                        orderItem.setUpdatedAt(Instant.now());
                        orderItem.setPrice(quantity * product.getPrice());
                        return orderItemRepository.save(orderItem);
                    })
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"))
            )
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order item not found"));
    }


    private OrderEntity getAndValidateOrder(UUID orderId, UUID userId) {
        return orderRepository.findById(orderId)
            .map(order -> {
                if (order.getCustomer().getId() == userId) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to view this order");
                }
                return order;
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }
}
