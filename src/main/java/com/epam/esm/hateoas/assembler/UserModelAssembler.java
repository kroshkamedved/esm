package com.epam.esm.hateoas.assembler;

import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.UserController;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
import com.epam.esm.pagination.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {
    @Override
    public EntityModel<User> toModel(User entity) {
        EntityModel<User> model = EntityModel.of(entity);
        model.add(linkTo(methodOn(UserController.class).fetchById(entity.getId())).withSelfRel());
        model.add(linkTo(methodOn(OrderController.class).fetchUserOrders(entity.getId(),
                Optional.of(1),
                Optional.of(2),
                Optional.of(Sort.ASC))).withRel("user_orders"));
        return model;
    }
}
