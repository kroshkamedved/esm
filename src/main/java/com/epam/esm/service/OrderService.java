package com.epam.esm.service;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Order;
import com.epam.esm.dto.TinyOrderInfoDTO;
import com.epam.esm.exception.Error;
import com.epam.esm.exception.IrrelevantRequestParameterException;
import com.epam.esm.pagination.PageRequest;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.assembler.OrderDTOAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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
        if (order.getOrderCertificates().isEmpty())
            throw new IrrelevantRequestParameterException("request body does not contain any GiftCertificates. Order creation is impossible", Error.IrrelevantParameters);
        order.getOrderCertificates().stream()
                .filter(c -> c.getId() == 0L)
                .forEach(certificateRepository::createCertificate);
        updateOrderCost(order);
        return orderRepository.createOrder(order);
    }

    private void updateOrderCost(Order order) {
        List<GiftCertificate> orderCertificates = new ArrayList<>();
        for (GiftCertificate certificate : order.getOrderCertificates()) {
            Optional<GiftCertificate> optionalGiftCertificate = certificateRepository.fetchCertificate(certificate.getId());
            if (optionalGiftCertificate.isPresent()) {
                orderCertificates.add(optionalGiftCertificate.get());
            }
        }
        order.setOrderCost(orderCertificates.stream()
                .map(c -> c.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    public List<Order> getUserOrders(long userId, PageRequest pageRequest) {
        return orderRepository.fetchUserOrdersPaginated(userId,pageRequest);
    }

    public TinyOrderInfoDTO getUserOrder(long userId, long orderId) {
        return orderDTOAssembler.assambleDTO(orderRepository.fetchUserOrder(userId, orderId));
    }

    public int getTotalRecords(long userId) {
        return orderRepository.countUserOrders(userId);
    }
}
