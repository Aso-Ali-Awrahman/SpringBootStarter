package com.aso.springstarter.services;

import com.aso.springstarter.repositories.OrderRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

}
