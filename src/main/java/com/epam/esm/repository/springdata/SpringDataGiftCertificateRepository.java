package com.epam.esm.repository.springdata;

import com.epam.esm.domain.GiftCertificate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataGiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {
}
