package com.epam.esm.dto;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Instant creationDate;
    private Instant updated;
    private List<Tag> tags;

    public GiftCertificateDTO(GiftCertificate giftCertificate, List<Tag> tags) {
        if (giftCertificate.getCreateDate() != null) {
            this.creationDate = giftCertificate.getCreateDate();
        }
        if (giftCertificate.getLastUpdateDate() != null) {
            this.updated = giftCertificate.getLastUpdateDate();
        }
        this.id = giftCertificate.getId();
        this.name = giftCertificate.getName();
        this.description = giftCertificate.getDescription();
        this.price = giftCertificate.getPrice();
        this.duration = giftCertificate.getDuration();
        this.tags = tags;
    }
}
