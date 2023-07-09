package com.epam.esm.hateoas.assembler;

import com.epam.esm.domain.Order;
import com.epam.esm.hateoas.model.OrderModel;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, OrderModel> {

    @Override
    public OrderModel toModel(Order entity) {
        OrderModel orderModel = new OrderModel();
        BeanUtils.copyProperties(entity,orderModel);
        return orderModel;
    }
}
