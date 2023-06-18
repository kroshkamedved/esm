package com.epam.esm.repository;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.GiftCertificatePriceOnly;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CertificateRepository {
    GiftCertificate createCertificate(GiftCertificate certificate);

    Optional<GiftCertificate> fetchCertificate(long id);

    void deleteCertificate(long id);

    boolean updateCertificate(GiftCertificateDTO certificate);

    List<GiftCertificate> fetchAll();

    List<GiftCertificate> fetchAllCertificatesWithTagName(String tagName);

    List<GiftCertificate> fetchAllCertificatesWithName(String name);

    List<GiftCertificate> fetchAllCertificatesWithDescription(String description);

    List<GiftCertificate> fetchAllParametrized(String name, String description, String sortOrder, Optional<String> sortByDate, Optional<String> sortByName);

    void updateCertificatePrice(GiftCertificatePriceOnly certificatePriceDto);

    List<GiftCertificate> fetchAllCertificatesWithTagId(Set<Long> tagsIds);

    int countAllCertificatesWithRequestdTags(Set<Long> tagsIds);
}
