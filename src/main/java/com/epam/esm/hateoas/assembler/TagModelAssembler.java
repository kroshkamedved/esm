package com.epam.esm.hateoas.assembler;

import com.epam.esm.controller.TagController;
import com.epam.esm.domain.Tag;
import com.epam.esm.hateoas.model.TagModel;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagModelAssembler implements RepresentationModelAssembler<Tag, TagModel> {
    @Override
    public TagModel toModel(Tag entity) {
        TagModel tagModel = new TagModel();
        BeanUtils.copyProperties(entity, tagModel);
        tagModel.add(linkTo(methodOn(TagController.class).fetchById(entity.getId())).withSelfRel());
        return tagModel;
    }
}
