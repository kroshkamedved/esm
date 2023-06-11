package com.epam.esm.controller;

import com.epam.esm.domain.Order;
import com.epam.esm.dto.TinyOrderInfoDTO;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.Error;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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
     *
     * @param id
     * @return foud order of 404 if order not exist
     */
    @GetMapping("/{id}")
    public Order fetchById(@PathVariable long id) {
        return orderService.getById(id).orElseThrow(() -> new EntityNotFoundException("Requested resource not found (order id =" + id + ")", Error.OrderNotFound));
    }

    /**
     * returns user Orders
     *
     * @param userId
     * @return found orders for corresponding user
     */
    @GetMapping(params = {"user_id", "order_id"})
    @ResponseStatus(HttpStatus.OK)
    public TinyOrderInfoDTO fetchUserOrder(@RequestParam(name = "user_id", required = true) long userId,
                                           @RequestParam(name = "order_id", required = true) long orderId) {
        return orderService.getUserOrder(userId, orderId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Order> fetchUserOrders(@RequestParam(name = "user_id", required = true) long userId) {
        return orderService.getUserOrders(userId);
    }

    /**
     * Create new Order
     *
     * @param {order} - order with certificates and user_id
     * @return created Order
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody(required = true) Order order) {
        Order persistedOrder = orderService.addOrder(order);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(persistedOrder.getId()).toUri();
        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, uri.toASCIIString()).body(persistedOrder);
    }
}
