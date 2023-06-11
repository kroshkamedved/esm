package com.epam.esm.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GeneratedColumn;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "order_price")
    private BigDecimal orderCost;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "create_time", insertable = false)
    private Instant createDate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "orders_certificates",
            joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "certificate_id")}
    )
    private List<GiftCertificate> orderCertificates = new ArrayList<>();

    public void addCertificate(GiftCertificate certificate) {
        orderCertificates.add(certificate);
    }

    public void removeCertificate(GiftCertificate certificate) {
        orderCertificates.remove(certificate);
    }
}
