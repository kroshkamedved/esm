package com.epam.esm.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GeneratedColumn;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@DynamicInsert
public class Order extends RepresentationModel<Order> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "order_price")
    private BigDecimal orderCost;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "create_time", insertable = false)
    private Instant createDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "orders_certificates",
            joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "certificate_id")}
    )
    private List<GiftCertificate> orderCertificates = new ArrayList<>();
}
