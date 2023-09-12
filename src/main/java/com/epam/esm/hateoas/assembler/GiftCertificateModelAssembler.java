package com.epam.esm.hateoas.assembler;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.hateoas.model.CertificateModel;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateModelAssembler implements RepresentationModelAssembler<GiftCertificate, CertificateModel> {


    @Override
    public CertificateModel toModel(GiftCertificate entity) {
        CertificateModel model = new CertificateModel();
        BeanUtils.copyProperties(entity, model);
        model.add(linkTo(methodOn(CertificateController.class).fetchById(entity.getId())).withSelfRel());
        return model;
    }
}
