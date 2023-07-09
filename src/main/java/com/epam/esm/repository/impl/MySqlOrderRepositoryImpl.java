package com.epam.esm.repository.impl;

import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.Error;
import com.epam.esm.exception.IrrelevantRequestParameterException;
import com.epam.esm.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class MySqlOrderRepositoryImpl implements OrderRepository {
    @Autowired
    EntityManager entityManager;

    @Override
    public Order fetchById(long id) {
        return entityManager.find(Order.class, id);
    }

    @Transactional
    @Override
    public Order createOrder(Order order) {
        //I don't like these rows, but when I didn't detach this order from session hibernate returns
        // to me cached order only with fields which I've sent through requestBody and
        // doesn't look for data from db except the id in case of new certificate was sent along with new order
        entityManager.persist(order);
        entityManager.flush();
        entityManager.detach(order);
        return entityManager.find(Order.class, order.getId());
    }

    @Override
    public List<Order> fetchUserOrders(long userId) {
        if (entityManager.find(User.class, userId) == null) {
            throw new EntityNotFoundException("user id : " + userId + " is not valid", Error.UserNotFound);
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        query.select(root).where(criteriaBuilder.equal(root.get("userId"), userId));
        TypedQuery<Order> query1 = entityManager.createQuery(query);
        return query1.getResultList();
    }

    @Override
    public Order fetchUserOrder(long userId, long orderId) {
        Order order = fetchById(orderId);
        if (order == null || order.getUserId() != userId) {
            throw new IrrelevantRequestParameterException("order id: " + orderId + " does not correspond to user id:" + userId, Error.IrrelevantParameters);
        }
        return fetchById(orderId);
    }

    @Override
    public int countUserOrders(long userId) {
        try {
            User user = entityManager.createQuery("Select u from User u where u.id = :userId", User.class).setParameter("userId", userId).getSingleResult();
        } catch (Exception e) {
            throw new EntityNotFoundException("user: " + userId + " id is not valid", Error.UserNotFound);
        }
        return ((Number) entityManager.createQuery("Select count(*) from Order o where o.userId = :userId ").setParameter("userId", userId).getSingleResult()).intValue();
    }
}
