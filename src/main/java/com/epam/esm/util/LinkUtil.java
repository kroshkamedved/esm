package com.epam.esm.util;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.dto.GiftCertificateDTO;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class LinkUtil {
    public static String createLinkHeader(String uri, String rel) {
        return "<" + uri + ">; rel=\"" + rel + "\"";
    }

    public static void addSelfLinksToTagAndCertificate(GiftCertificateDTO certificate) {
        certificate.getTags().stream()
                .forEach(tag ->
                {
                    Link tagSelfLink = linkTo(TagController.class).slash(tag.getId()).withSelfRel();
                    tag.add(tagSelfLink);
                });
    }
}
