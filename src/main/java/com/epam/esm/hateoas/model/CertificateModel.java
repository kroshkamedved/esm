package com.epam.esm.hateoas.model;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.Tag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
public class CertificateModel extends RepresentationModel<GiftCertificate> {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private Instant createDate;
    private Instant lastUpdateDate;
    @JsonIgnore
    private Set<Order> orders;
    Set<Tag> tags;
}
