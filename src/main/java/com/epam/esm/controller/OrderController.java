package com.epam.esm.controller;

import com.epam.esm.domain.Order;
import com.epam.esm.dto.TinyOrderInfoDTO;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.Error;
import com.epam.esm.hateoas.assembler.OrderModelAssembler;
import com.epam.esm.hateoas.model.OrderModel;
import com.epam.esm.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("orders")
@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderModelAssembler modelAssembler;
    private final PagedResourcesAssembler<Order> pagedResourcesAssembler;

    /**
     * return order with {id}
     *
     * @param id
     * @return found order of 404 if order not exist
     */
    @GetMapping("/{id}")
    public OrderModel fetchById(@PathVariable long id) {
        return modelAssembler.toModel(orderService.getById(id).orElseThrow(() -> new EntityNotFoundException("Requested resource not found (order id =" + id + ")", Error.OrderNotFound)));
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

    @GetMapping(params = {"user_id"})
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<OrderModel> fetchUserOrders(@RequestParam(name = "user_id") long id, Pageable pageable) {
        Page<Order> userOrders = orderService.getUserOrders(id, pageable);
        return pagedResourcesAssembler.toModel(userOrders, modelAssembler);
    }

    /**
     * Create new Order
     *
     * @param {order} - order with certificates and user_id
     * @return created Order
     */
    @PostMapping
    public OrderModel createOrder(@RequestBody(required = true) Order order) {
        Order persistedOrder = orderService.addOrder(order);
        return modelAssembler.toModel(persistedOrder);
    }
}
