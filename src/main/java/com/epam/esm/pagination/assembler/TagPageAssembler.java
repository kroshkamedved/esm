package com.epam.esm.pagination.assembler;

import com.epam.esm.controller.TagController;
import com.epam.esm.domain.Tag;
import com.epam.esm.pagination.Page;
import com.epam.esm.pagination.PageRequest;
import com.epam.esm.util.LinkUtil;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagPageAssembler{
    public Page<Tag> pageOf(Collection<Tag> collection, PageRequest request, int totalRecords) {
        Page<Tag> page = new Page<>(request, totalRecords, collection);
        LinkUtil.addSelfLinksToTags(collection);
        page.add(linkTo(TagController.class).withRel("allTags"));
        addNavigationLinks(page, request.getPageNumber(), request);
        return page;
    }

    private void addNavigationLinks(Page<Tag> page, int pageNumber, PageRequest request) {
        if (pageNumber > 1) {
            addFirstPageLink(page, request);
            addPreviousePageLink(page, pageNumber, request);
        }
        if (pageNumber < page.getTotalPages()) {
            addLastPageLink(page, request);
            addNextPageLink(page, pageNumber, request);
        }
    }

    private static void addNextPageLink(Page<Tag> page, int pageNumber, PageRequest request) {
        page.add(linkTo
                (methodOn(TagController.class).fetchAll(
                        "" + (pageNumber + 1),
                        String.valueOf(request.getPageSize()),
                        request.getSortingOrder())).withRel("next_page"));
    }

    private static void addLastPageLink(Page<Tag> page, PageRequest request) {
        page.add(linkTo
                (methodOn(TagController.class).fetchAll(
                        "" + page.getTotalPages(),
                        String.valueOf(request.getPageSize()),
                        request.getSortingOrder())).withRel("last_page"));
    }

    private static void addPreviousePageLink(Page<Tag> page, int pageNumber, PageRequest request) {
        page.add(linkTo
                (methodOn(TagController.class).fetchAll(
                        "" + (pageNumber - 1),
                        String.valueOf(request.getPageSize()),
                        request.getSortingOrder())).withRel("previous_page"));
    }

    private static void addFirstPageLink(Page<Tag> page, PageRequest request) {
        page.add(linkTo
                (methodOn(TagController.class).fetchAll(
                        "1",
                        String.valueOf(request.getPageSize()),
                        request.getSortingOrder())).withRel("firs_page"));
    }
}

