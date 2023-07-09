package com.epam.esm.hateoas.model;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Order;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
@Data
public class OrderModel extends RepresentationModel<Order> {
    private long id;
    private BigDecimal orderCost;
    private Long userId;
    private Instant createDate;
    private List<GiftCertificate> orderCertificates = new ArrayList<>();
}
