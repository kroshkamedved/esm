package com.epam.esm.pagination.assembler;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.pagination.Page;
import com.epam.esm.pagination.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CertificatePageAssambler{

    public Page<GiftCertificate> pageOfLinkedWithTags(Collection<GiftCertificate> collection, PageRequest request, int totalRecords, Set<Long> tagsIds) {
        Page<GiftCertificate> page = new Page<>(request, totalRecords, collection);
        page.add(linkTo(CertificateController.class).withRel("allOrders"));
        addNavigationLinks(page, request.getPageNumber(), request, tagsIds);
        return page;
    }

    private void addNavigationLinks(Page<GiftCertificate> page, int pageNumber, PageRequest request, Set<Long> tagsIds) {
        if (pageNumber > 1) {
            addFirstPageLinkForFetchByTag(page, pageNumber, request, tagsIds);
            addPreviousePageLinkForFetchByTag(page, pageNumber, request, tagsIds);
        }
        if (pageNumber < page.getTotalPages()) {
            addLastPageLinkForFetchByTag(page, pageNumber, request, tagsIds);
            addNextPageLinkForFetchByTag(page, pageNumber, request, tagsIds);
        }
    }

    private static void addNextPageLinkForFetchByTag(Page<GiftCertificate> page, int pageNumber, PageRequest request, Set<Long> tagsIds) {
        page.add(linkTo
                (methodOn(CertificateController.class).fetchByTags(
                        tagsIds,
                        "" + (pageNumber + 1),
                        String.valueOf(request.getPageSize()),
                        request.getSortingOrder())).withRel("next_page"));
    }

    private static void addLastPageLinkForFetchByTag(Page<GiftCertificate> page, int pageNumber, PageRequest request, Set<Long> tagsIds) {
        page.add(linkTo
                (methodOn(CertificateController.class).fetchByTags(
                        tagsIds,
                        "" + page.getTotalPages(),
                        String.valueOf(request.getPageSize()),
                        request.getSortingOrder())).withRel("last_page"));
    }

    private static void addPreviousePageLinkForFetchByTag(Page<GiftCertificate> page, int pageNumber, PageRequest request, Set<Long> tagsIds) {
        page.add(linkTo
                (methodOn(CertificateController.class).fetchByTags(
                        tagsIds,
                        "" + (pageNumber - 1),
                        String.valueOf(request.getPageSize()),
                        request.getSortingOrder())).withRel("previous_page"));
    }

    private static void addFirstPageLinkForFetchByTag(Page<GiftCertificate> page, int pageNumber, PageRequest request, Set<Long> tagsIds) {
        page.add(linkTo
                (methodOn(CertificateController.class).fetchByTags(
                        tagsIds,
                        "1",
                        String.valueOf(request.getPageSize()),
                        request.getSortingOrder())).withRel("firs_page"));
    }
}
