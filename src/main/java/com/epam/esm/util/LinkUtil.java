package com.epam.esm.util;

import com.epam.esm.controller.TagController;
import com.epam.esm.domain.Tag;
import org.springframework.hateoas.Link;

import java.util.Collection;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class LinkUtil {
    public static String createLinkHeader(String uri, String rel) {
        return "<" + uri + ">; rel=\"" + rel + "\"";
    }

    public static void addSelfLinksToTags(Collection<Tag> tags) {
        tags.stream()
                .forEach(tag ->
                {
                    Link tagSelfLink = linkTo(TagController.class).slash(tag.getId()).withSelfRel();
                    tag.add(tagSelfLink);
                });
    }
}
