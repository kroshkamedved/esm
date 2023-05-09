package com.epam.esm.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class GiftCertificate {
    long id;
    String name;
    String description;
    BigDecimal price;
    Integer duration;
    Instant create_date;
    Instant last_update_date;
}
