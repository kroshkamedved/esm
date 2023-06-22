package com.epam.esm.hateoas.assembler;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.dto.GiftCertificateDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static com.epam.esm.util.LinkUtil.addSelfLinksToTags;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class GIftCertificateModelAssembler implements RepresentationModelAssembler<GiftCertificateDTO, EntityModel<GiftCertificateDTO>> {

    @Override
    public EntityModel<GiftCertificateDTO> toModel(GiftCertificateDTO entity) {
        EntityModel<GiftCertificateDTO> entityModel = EntityModel.of(entity);
        Link collectionLink = linkTo(CertificateController.class).withRel("GiftCertificates");
        entityModel.add(linkTo(CertificateController.class).slash(entity.getId()).withSelfRel());
        addSelfLinksToTags(entity.getTags());
        entityModel.add(collectionLink);
        return entityModel;
    }
}
