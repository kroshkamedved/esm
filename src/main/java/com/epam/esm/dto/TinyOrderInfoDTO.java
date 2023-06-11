package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
public class TinyOrderInfoDTO {
    @JsonIgnore
    private long id;
    private BigDecimal orderCost;
    private Instant createDate;

}
