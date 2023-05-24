package com.epam.esm.repository.mappers; //TODO Why assembler located in mappers?

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.dto.GiftCertificateDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CertificateDTOAssembler {
    public GiftCertificateDTO mapToDTO(GiftCertificate certificate, List<Tag> tags) {
        return new GiftCertificateDTO(certificate, tags);
    }

    public GiftCertificate mapToCertificate(GiftCertificateDTO giftCertificate) {
        GiftCertificate certificateFromDTO = GiftCertificate.builder() //TODO is this needs a variable?
                .name(giftCertificate.getName())
                .description(giftCertificate.getDescription())
                .price(giftCertificate.getPrice())
                .duration(giftCertificate.getDuration())
                .create_date(giftCertificate.getCreationDate())
                .last_update_date(giftCertificate.getUpdated())
                .id(giftCertificate.getId()).build();
        return certificateFromDTO;
    }
}