package com.epam.esm.service;

import com.epam.esm.domain.Order;
import com.epam.esm.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Optional<Order> getById(long id) {
        return Optional.ofNullable(orderRepository.fetchById(id));
    }
}
