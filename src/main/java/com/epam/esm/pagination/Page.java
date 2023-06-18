package com.epam.esm.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Page<T> {
    int totalPages;
    long totalElements;
    List<T> content;
}
