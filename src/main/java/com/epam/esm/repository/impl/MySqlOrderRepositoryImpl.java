package com.epam.esm.repository.impl;

import com.epam.esm.domain.Order;
import com.epam.esm.repository.OrderRepository;
import jakarta.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MySqlOrderRepositoryImpl implements OrderRepository {
    @Autowired
    EntityManager entityManager;

    @Override
    public Order fetchById(long id) {
        return entityManager.find(Order.class, id);
    }
}
