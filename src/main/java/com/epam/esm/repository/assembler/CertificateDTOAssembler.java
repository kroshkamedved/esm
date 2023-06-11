package com.epam.esm.repository.assembler;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.GiftCertificateDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CertificateDTOAssembler {
    public GiftCertificateDTO mapToDTO(GiftCertificate certificate, List<Tag> tags) {
        return new GiftCertificateDTO(certificate, tags);
    }

    public GiftCertificate mapToCertificate(GiftCertificateDTO giftCertificate) {
        return GiftCertificate.builder()
                .name(giftCertificate.getName())
                .description(giftCertificate.getDescription())
                .price(giftCertificate.getPrice())
                .duration(giftCertificate.getDuration())
                .createDate(giftCertificate.getCreationDate())
                .lastUpdateDate(giftCertificate.getUpdated())
                .id(giftCertificate.getId()).build();
    }
}