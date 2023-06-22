package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class BestClientDTO {
    long id;
    String login;
    BigDecimal totalCost;
}


