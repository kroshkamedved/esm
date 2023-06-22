package com.epam.esm.pagination.assembler;

import com.epam.esm.controller.OrderController;
import com.epam.esm.domain.Order;
import com.epam.esm.pagination.Page;
import com.epam.esm.pagination.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class OrderPageAssembler {
    public Page<Order> pageOf(Collection<Order> collection, PageRequest request, int totalRecords, long userID) {
        Page<Order> page = new Page<>(request, totalRecords, collection);
        page.add(linkTo(OrderController.class).withRel("allOrders"));
        addNavigationLinks(page, request.getPageNumber(), request, userID);
        return page;
    }

    private void addNavigationLinks(Page<Order> page, int pageNumber, PageRequest request, long userID) {
        if (pageNumber > 1) {
            addFirstPageLinkForFetchByUser(page, pageNumber, request, userID);
            addPreviousePageLinkForFetchByUser(page, pageNumber, request, userID);
        }
        if (pageNumber < page.getTotalPages()) {
            addLastPageLinkForFetchByUser(page, pageNumber, request, userID);
            addNextPageLinkForFetchByUSer(page, pageNumber, request, userID);
        }
    }

    private void addNextPageLinkForFetchByUSer(Page<Order> page, int pageNumber, PageRequest request, long userID) {
        page.add(linkTo
                (methodOn(OrderController.class).fetchUserOrders(
                        userID,
                        Optional.of(pageNumber + 1),
                        Optional.of(request.getPageSize()),
                        Optional.of(request.getSortingOrder()))).withRel("next_page"));

    }

    private void addLastPageLinkForFetchByUser(Page<Order> page, int pageNumber, PageRequest request, long userID) {
        page.add(linkTo
                (methodOn(OrderController.class).fetchUserOrders(
                        userID,
                        Optional.of(page.getTotalPages()),
                        Optional.of(request.getPageSize()),
                        Optional.of(request.getSortingOrder()))).withRel("last_page"));
    }


    private void addPreviousePageLinkForFetchByUser(Page<Order> page, int pageNumber, PageRequest request, long userID) {
        page.add(linkTo
                (methodOn(OrderController.class).fetchUserOrders(
                        userID,
                        Optional.of(pageNumber + 1),
                        Optional.of(request.getPageSize()),
                        Optional.of(request.getSortingOrder()))).withRel("previous_page"));

    }


    private void addFirstPageLinkForFetchByUser(Page<Order> page, int pageNumber, PageRequest request, long userID) {
        page.add(linkTo
                (methodOn(OrderController.class).fetchUserOrders(
                        userID,
                        Optional.of( 1),
                        Optional.of(request.getPageSize()),
                        Optional.of(request.getSortingOrder()))).withRel("first_page"));

    }
}
