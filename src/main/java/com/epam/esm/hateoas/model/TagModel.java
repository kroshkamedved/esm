package com.epam.esm.hateoas.model;

import com.epam.esm.domain.Tag;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
@Data
public class TagModel extends RepresentationModel<Tag> {
    long id;
    String name;
}
