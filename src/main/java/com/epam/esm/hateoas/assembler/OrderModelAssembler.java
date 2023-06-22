package com.epam.esm.hateoas.assembler;

import com.epam.esm.controller.OrderController;
import com.epam.esm.domain.Order;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {

    @Override
    public EntityModel<Order> toModel(Order entity) {
        EntityModel<Order> model = EntityModel.of(entity);
        model.add(linkTo(methodOn(OrderController.class).fetchById(entity.getId())).withSelfRel());
        model.add(linkTo(OrderController.class).withRel("all_orders"));
        return model;
    }
}
