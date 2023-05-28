package com.epam.esm.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@Entity(name = "certificates")
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String name;
    String description;
    BigDecimal price;
    Integer duration;
    @Column(name = "create_time")
    Instant create_date;
    @Column(name = "update_time")
    Instant last_update_date;
}
