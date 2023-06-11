package com.epam.esm.service;

import com.epam.esm.domain.Order;
import com.epam.esm.dto.TinyOrderInfoDTO;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.assembler.OrderDTOAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CertificateRepository certificateRepository;
    private final OrderDTOAssembler orderDTOAssembler;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        CertificateRepository certificateRepository,
                        OrderDTOAssembler orderDTOAssembler) {
        this.orderRepository = orderRepository;
        this.certificateRepository = certificateRepository;
        this.orderDTOAssembler = orderDTOAssembler;
    }

    public Optional<Order> getById(long id) {
        return Optional.ofNullable(orderRepository.fetchById(id));
    }

    public Order addOrder(Order order) {
        order.getOrderCertificates().stream()
                .filter(c -> c.getId() == 0l)
                .forEach(certificate -> certificateRepository.createCertificate(certificate));
        return orderRepository.createOrder(order);
    }

    public List<Order> getUserOrders(long userId) {
        return orderRepository.fetchUserOrders(userId);
    }

    public TinyOrderInfoDTO getUserOrder(long userId, long orderId) {
        return orderDTOAssembler.assambleDTO(orderRepository.fetchUserOrder(userId, orderId));
    }
}
