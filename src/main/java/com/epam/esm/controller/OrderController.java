package com.epam.esm.controller;

import com.epam.esm.domain.Order;
import com.epam.esm.dto.TinyOrderInfoDTO;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.Error;
import com.epam.esm.hateoas.assembler.OrderModelAssembler;
import com.epam.esm.pagination.Page;
import com.epam.esm.pagination.PageRequest;
import com.epam.esm.pagination.Sort;
import com.epam.esm.pagination.assembler.OrderPageAssembler;
import com.epam.esm.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RequestMapping("orders")
@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderModelAssembler modelAssembler;
    private final OrderPageAssembler pageAssembler;

    /**
     * return order with {id}
     *
     * @param id
     * @return found order of 404 if order not exist
     */
    @GetMapping("/{id}")
    public EntityModel<Order> fetchById(@PathVariable long id) {
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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)

    public Page<Order> fetchUserOrders(@RequestParam(name = "user_id", required = true) long userId,
                                       @RequestParam(name = "page") Optional<Integer> page,
                                       @RequestParam(name = "pageSize") Optional<Integer> pageSize,
                                       @RequestParam(name = "sortOrder") Optional<Sort> sortOrder) {

        PageRequest pageRequest = new PageRequest(page.orElse(1), pageSize.orElse(2), sortOrder.orElse(Sort.ASC));
        int totalRecords = orderService.getTotalRecords(userId);
        List<Order> userOrders = orderService.getUserOrders(userId, pageRequest);

        return pageAssembler.pageOf(userOrders, pageRequest, totalRecords, userId);
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
