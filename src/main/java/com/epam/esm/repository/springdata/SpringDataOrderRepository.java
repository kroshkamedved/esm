package com.epam.esm.repository.springdata;

import com.epam.esm.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataOrderRepository extends JpaRepository<Order, Long> {
    public Page<Order> findOrdersByUserId(Long id, Pageable pageable);
}
