package com.epam.esm.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GiftCertificatePriceOnly {
    private long id;
    private BigDecimal price;
}
