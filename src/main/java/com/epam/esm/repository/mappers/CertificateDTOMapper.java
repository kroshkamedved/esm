package com.epam.esm.repository.mappers;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.dto.GiftCertificateDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CertificateDTOMapper {
    public GiftCertificateDTO toDto(GiftCertificate certificate, List<Tag> tags) {
        return new GiftCertificateDTO(certificate, tags);
    }

    public GiftCertificate toGiftCertificate(GiftCertificateDTO giftCertificate) {
        GiftCertificate certificateFromDTO = GiftCertificate.builder().name(
                giftCertificate.getName()).description(
                giftCertificate.getDescription()).price(
                giftCertificate.getPrice()).duration(
                giftCertificate.getDuration())
                .create_date(giftCertificate.getCreate_date())
                .last_update_date(giftCertificate.getLast_update_date())
                .id(giftCertificate.getId()).build();
        return certificateFromDTO;
    }
}
