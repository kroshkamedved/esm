package com.epam.esm.hateoas.assembler;

import com.epam.esm.domain.User;
import com.epam.esm.hateoas.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, UserModel> {
    @Override
    public UserModel toModel(User entity) {
        UserModel model = new UserModel();
        BeanUtils.copyProperties(entity, model);
        return model;
    }
}
