package com.epam.esm.pagination;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.Collection;

@Data
public class Page<T> extends RepresentationModel<Page<T>> {
    int totalPages;
    long totalElements;
    Collection<T> content;

    public Page(PageRequest pageRequest, long totalElements, Collection<T> content) {
        this.totalPages = (int) Math.ceil((double) totalElements / pageRequest.getPageSize());
        this.totalElements = totalElements;
        this.content = content;
    }
}
