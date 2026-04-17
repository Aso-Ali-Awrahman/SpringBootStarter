package com.aso.springstarter.controllers;

import java.util.List;
import java.util.UUID;

import com.aso.springstarter.dtos.PagingRequest;
import com.aso.springstarter.dtos.order.AddItemRequest;
import com.aso.springstarter.dtos.order.InitiateOrderResponse;
import com.aso.springstarter.dtos.order.OrderResponse;
import com.aso.springstarter.dtos.order.OrderWithItemsResponse;
import com.aso.springstarter.dtos.order.UpdateItemQuantityRequest;
import com.aso.springstarter.security.User;
import com.aso.springstarter.security.authorization.CustomerRoleRequired;
import com.aso.springstarter.services.OrderItemService;
import com.aso.springstarter.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order Controller (Customer)")
@RestController
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @PostMapping(value = "protected/customers/orders")
    @Operation(summary = "Initiate order [1]")
    @ResponseStatus(HttpStatus.OK)
    @CustomerRoleRequired
    public InitiateOrderResponse initiateOrder(@AuthenticationPrincipal User user) {
        return orderService.initiateOrder(user.getId());
    }

    @PostMapping(value = "protected/customers/orders/{id}")
    @Operation(summary = "Complete order [3]")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CustomerRoleRequired
    public void completeOrder(@AuthenticationPrincipal User user, @PathVariable UUID id) {
        orderService.completeOrder(id, user.getId());
    }

    @GetMapping(value = "protected/customers/orders")
    @Operation(summary = "Get orders")
    @ResponseStatus(HttpStatus.OK)
    @CustomerRoleRequired
    public Page<OrderResponse> getOrders(@AuthenticationPrincipal User user,
                                         @RequestParam(name = "page", defaultValue = "0") int page,
                                         @RequestParam(name = "size", defaultValue = "10") int size,
                                         @RequestParam(name = "sort", defaultValue = "id") String sortBy,
                                         @RequestParam(name = "direction", defaultValue = "asc") String direction) {
        final var allowedSorts = List.of("id", "totalPrice", "createdAt");
        final var pagingRequest = new PagingRequest(page, size, sortBy, direction);
        return orderService.getAllOrders(pagingRequest.toPageable(allowedSorts), user.getId());
    }

    @GetMapping(value = "protected/customers/orders/{id}")
    @Operation(summary = "Get order by id along with item responses")
    @ResponseStatus(HttpStatus.OK)
    @CustomerRoleRequired
    public OrderWithItemsResponse getOrderById(@AuthenticationPrincipal User user, @PathVariable UUID id) {
        return orderService.getOrderWithItems(id, user.getId());
    }

    @PostMapping(value = "protected/customers/orders/{id}/items")
    @Operation(summary = "Add item to order [2.1]")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CustomerRoleRequired
    public void addItemToOrder(@AuthenticationPrincipal User user, @PathVariable UUID id, @Valid @RequestBody AddItemRequest request) {
        orderItemService.addItemToOrder(id, user.getId(), request.getProductId(), request.getQuantity());
    }

    @PatchMapping("protected/customers/orders/{id}/items/{itemId}/quantity")
    @Operation(summary = "Update quantity of a item in an order [2.2]")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CustomerRoleRequired
    public void updateOrderItemQuantity(@AuthenticationPrincipal User user, @PathVariable UUID id,
                                        @PathVariable UUID itemId, @RequestBody UpdateItemQuantityRequest request) {
        orderItemService.updateItemQuantity(id, itemId, user.getId(), request.getQuantity());
    }

    @DeleteMapping(value = "protected/customers/orders/{id}/items/{itemId}")
    @Operation(summary = "Delete item in an order [2.3]")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CustomerRoleRequired
    public void deleteOrderItem(@AuthenticationPrincipal User user, @PathVariable UUID id, @PathVariable UUID itemId) {
        orderItemService.removeItemFromOrder(id, itemId, user.getId());
    }

    @DeleteMapping(value = "protected/customers/orders/{id}/items")
    @Operation(summary = "Delete all items in an order [2.4]")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CustomerRoleRequired
    public void deleteAllOrderItems(@AuthenticationPrincipal User user, @PathVariable UUID id) {
        orderService.removeAllOrderItems(id, user.getId());
    }

    @DeleteMapping(value = "protected/customers/orders/{id}")
    @Operation(summary = "Delete order along with all items in it [4]")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CustomerRoleRequired
    public void deleteOrder(@AuthenticationPrincipal User user, @PathVariable UUID id) {
        orderService.deleteOrder(id, user.getId());
    }

}
