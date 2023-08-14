package com.epam.esm.service;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Order;
import com.epam.esm.dto.TinyOrderInfoDTO;
import com.epam.esm.exception.Error;
import com.epam.esm.exception.IrrelevantRequestParameterException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.assembler.OrderDTOAssembler;
import com.epam.esm.repository.springdata.SpringDataGiftCertificateRepository;
import com.epam.esm.repository.springdata.SpringDataOrderRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CertificateRepository certificateRepository;
    private final OrderDTOAssembler orderDTOAssembler;
    private final SpringDataOrderRepository springDataOrderRepository;
    private final SpringDataGiftCertificateRepository giftCertificateRepository;
    private final EntityManager entityManager;


    public Optional<Order> getById(long id) {
        return Optional.ofNullable(orderRepository.fetchById(id));
    }

    @Transactional
    public Order addOrder(Order order) {
        List<GiftCertificate> orderCertificates = order.getOrderCertificates();
        if (orderCertificates.isEmpty())
            throw new IrrelevantRequestParameterException("request body does not contain any GiftCertificates. Order creation is impossible", Error.IrrelevantParameters);
        orderCertificates = orderCertificates.stream()
                .map(certificate -> giftCertificateRepository.findById(certificate.getId()).orElseThrow(() -> new IrrelevantRequestParameterException("not valid certificate id", Error.GiftCertificateNotFound)))
                .collect(Collectors.toList());
        order.setOrderCertificates(orderCertificates);
        updateOrderCost(order);
        Order saved = springDataOrderRepository.saveAndFlush(order);
        entityManager.refresh(saved);
        return saved;
    }

    private void updateOrderCost(Order order) {
        List<GiftCertificate> orderCertificates = new ArrayList<>();
        for (GiftCertificate certificate : order.getOrderCertificates()) {
            Optional<GiftCertificate> optionalGiftCertificate = certificateRepository.fetchCertificate(certificate.getId());
            optionalGiftCertificate.ifPresent(orderCertificates::add);
        }
        order.setOrderCost(orderCertificates.stream()
                .map(GiftCertificate::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    public Page<Order> getUserOrders(long userId, Pageable pageRequest) {
        return springDataOrderRepository.findOrdersByUserId(userId, pageRequest);
    }

    public TinyOrderInfoDTO getUserOrder(long userId, long orderId) {
        return orderDTOAssembler.assambleDTO(orderRepository.fetchUserOrder(userId, orderId));
    }
}
