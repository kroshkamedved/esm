package com.epam.esm.repository;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.dto.dto.GiftCertificateDTO;

import java.util.List;
import java.util.Optional;

public interface CertificateRepository {
    GiftCertificate createCertificate(GiftCertificate certificate);

    Optional<GiftCertificate> fetchCertificate(long id);

    void deleteCertificate(long id);

    boolean updateCertificate(GiftCertificateDTO certificate);

    List<GiftCertificate> fetchAll();

    List<GiftCertificate> fetchAllCertificatesWithTagName(String tagName);

    List<GiftCertificate> fetchAllCertificatesWithName(String name);

    List<GiftCertificate> fetchAllCertificatesWithDescription(String description);
}
