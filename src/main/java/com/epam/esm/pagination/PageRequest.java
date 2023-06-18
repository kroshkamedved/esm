package com.epam.esm.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageRequest {
    int pageNumber;
    int pageSize;
    Sort sortingOrder;
}
