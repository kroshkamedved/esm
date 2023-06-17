package com.epam.esm.dto;

import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class BestClientDTO {
    long id;
    String login;
    BigDecimal totalCost;

    public BestClientDTO(long id, String login, BigDecimal totalCost) {
        this.id = id;
        this.login = login;
        this.totalCost = totalCost;
    }
}


