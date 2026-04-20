package com.aso.springstarter.services;

import java.time.Instant;
import java.util.UUID;

import com.aso.springstarter.dtos.order.InitiateOrderResponse;
import com.aso.springstarter.dtos.order.OrderResponse;
import com.aso.springstarter.dtos.order.OrderWithItemsResponse;
import com.aso.springstarter.entiies.OrderEntity;
import com.aso.springstarter.entiies.OrderStatus;
import com.aso.springstarter.repositories.CustomerRepository;
import com.aso.springstarter.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    @Override
    public Page<OrderResponse> getAllOrders(Pageable pageable, UUID userId) {
        return orderRepository.findAllByCustomerId(pageable, userId)
            .map(OrderEntity::toDto);
    }

    @Override
    public OrderWithItemsResponse getOrderWithItems(UUID orderId, UUID userId) {
        return getAndValidateOrder(orderId, userId)
            .toDtoWithItems();
    }

    @Override
    @Transactional
    public InitiateOrderResponse initiateOrder(UUID userId) {
        final var customer = customerRepository.findById(userId);
        // This is impossible to happen, but just in case
        if (customer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }
        final var order = new OrderEntity(
            null,
            customer.get(),
            OrderStatus.INITIATED,
            null,
            Instant.now(),
            Instant.now(),
            null
        );
        orderRepository.save(order);
        return new InitiateOrderResponse(order.getId());
    }

    @Override
    @Transactional
    public void completeOrder(UUID orderId, UUID userId) {
        final var order = getAndValidateOrder(orderId, userId);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order must be in PENDING to complete");
        }
        var totalPrice = 0.0;
        for (var item : order.getOrderItems()) {
            if (item.checkQuantityBeforeCompletingOrder()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock for order item " + item.getId() + "Update quantity");
            }
            totalPrice += item.getPrice();
        }
        order.completeOrder(totalPrice);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void removeAllOrderItems(UUID orderId, UUID userId) {
        final var order = getAndValidateOrder(orderId, userId);
        if (order.isNotAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order must be in INITIATED or PENDING state to remove items");
        }
        order.getOrderItems().clear();
        order.setStatus(OrderStatus.INITIATED);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(UUID orderId, UUID userId) {
        final var order = getAndValidateOrder(orderId, userId);
        if (order.isNotAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order must be in INITIATED or PENDING state to delete");
        }
        orderRepository.delete(order);
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
