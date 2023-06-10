package com.epam.esm.repository;

import com.epam.esm.domain.Order;

public interface OrderRepository {
    Order fetchById(long id);
}
