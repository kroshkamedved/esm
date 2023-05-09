package com.epam.esm.repositories.dto;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
public class GiftCertificateDTO {
    private long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer duration;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Instant create_date;
    private Instant last_update_date;
    private List<Tag> tags;

    public GiftCertificateDTO(GiftCertificate giftCertificate, List<Tag> tags) {
        if (giftCertificate.getCreate_date() != null) {
            this.create_date = giftCertificate.getCreate_date();
        }
        if (giftCertificate.getLast_update_date() != null) {
            this.last_update_date = giftCertificate.getLast_update_date();
        }
        this.id = giftCertificate.getId();
        this.name = giftCertificate.getName();
        this.description = giftCertificate.getDescription();
        this.price = giftCertificate.getPrice();
        this.duration = giftCertificate.getDuration();
        this.tags = tags;
    }
}
