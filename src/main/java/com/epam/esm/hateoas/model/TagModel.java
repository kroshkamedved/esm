package com.epam.esm.hateoas.model;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.security.cert.Certificate;
import java.util.List;

@Data
public class TagModel extends RepresentationModel<Tag> {
    long id;
    String name;
    List<GiftCertificate> certificates;
}
