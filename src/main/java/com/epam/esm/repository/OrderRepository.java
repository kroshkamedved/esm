package com.epam.esm.repository;

import com.epam.esm.domain.Order;
import com.epam.esm.pagination.PageRequest;

import java.util.List;

public interface OrderRepository {
    Order fetchById(long id);

    Order createOrder(Order order);

    List<Order> fetchUserOrders(long userId);

    Order fetchUserOrder(long userId, long orderId);

    int countUserOrders(long userId);

    List<Order> fetchUserOrdersPaginated(long userId, PageRequest pageRequest);
}
