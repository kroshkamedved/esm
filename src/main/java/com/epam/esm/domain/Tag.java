package com.epam.esm.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Tag {
    long id;
    String name;
}
