package com.epam.esm.repository;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.dto.dto.GiftCertificateDTO;

import java.util.List;
import java.util.Optional;

public interface CertificateRepository {
    GiftCertificate storeCertificate(GiftCertificate certificate);

    Optional<GiftCertificate> getCertificate(long id);

    void deleteCertificate(long id);

    boolean updateCertificate(GiftCertificateDTO certificate);

    List<GiftCertificate> getAll();

    List<GiftCertificate> getAllCertificatesWithTagName(String tagName);

    List<GiftCertificate> getAllCertificatesWithName(String name);

    List<GiftCertificate> getAllCertificatesWithDescription(String description);
}
