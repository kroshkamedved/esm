package com.epam.esm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@Builder
@Entity
@Table(name = "certificates")
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    @Column(name = "create_time")
    private Instant create_date;
    @Column(name = "update_time")
    private Instant last_update_date;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "orderCertificates")
    private Set<Order> orders;
}
