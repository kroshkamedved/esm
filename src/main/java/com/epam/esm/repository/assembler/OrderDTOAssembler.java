package com.epam.esm.repository.assembler;

import com.epam.esm.domain.Order;
import com.epam.esm.dto.TinyOrderInfoDTO;
import org.springframework.stereotype.Component;

@Component
public class OrderDTOAssembler {
    public TinyOrderInfoDTO assambleDTO(Order order) {
        return new TinyOrderInfoDTO(order.getId(), order.getOrderCost(), order.getCreateDate());
    }
}
