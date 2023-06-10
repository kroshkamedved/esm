package com.epam.esm.controller;

import com.epam.esm.domain.Order;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.Error;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("orders")
@RestController
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * return order with {id}
     * @param id
     * @return foud order of 404 if order not exist
     */
    @GetMapping("/{id}")
    public Order fetchById(@PathVariable long id) {
        return orderService.getById(id).orElseThrow(()->new EntityNotFoundException("Requested resource not found (order id =" + id + ")", Error.OrderNotFound));
    }
}
