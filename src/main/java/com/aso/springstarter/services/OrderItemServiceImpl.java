package com.aso.springstarter.services;

import com.aso.springstarter.repositories.OrderItemRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

}
