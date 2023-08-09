package com.epam.esm.service;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.GiftCertificatePriceOnly;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.Error;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.assembler.CertificateDTOAssembler;
import com.epam.esm.repository.springdata.SpringDataGiftCertificateRepository;
import com.epam.esm.repository.springdata.SpringDataTagRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificateService {
    private final CertificateRepository certificateRepository;
    private final TagRepository tagRepository;
    private final SpringDataTagRepository springDataTagRepository;
    private final CertificateDTOAssembler certificateDTOMapper;
    private final SpringDataGiftCertificateRepository giftCertificateRepository;
    private final EntityManager entityManager;

    public GiftCertificate getCertificate(long id) {
        return giftCertificateRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("can't receive GiftCertificate with id = " + id, Error.GiftCertificateNotFound));

    }

    @Transactional
    public GiftCertificate addCertificate(GiftCertificate certificate) {
        Set<Tag> tags = certificate.getTags();
        List<Tag> tagsList = springDataTagRepository.saveAll(tags);
        tagsList.stream()
                .forEach(entityManager::refresh);
        certificate.setTags(new HashSet<>(tagsList));
        GiftCertificate saved = giftCertificateRepository.save(certificate);
        entityManager.refresh(saved);
        return saved;
    }

    public void deleteCertificate(long id) {
        certificateRepository.deleteCertificate(id);
    }

    @Transactional
    public void updateCertificate(GiftCertificate certificate) {
        boolean newTagLinked = false;
        if (certificate.getTags() != null) {
            newTagLinked = tagRepository.saveTagsForCertificate(certificate.getTags(), certificate.getId());
        }
        GiftCertificate certificate1 = giftCertificateRepository.findById(certificate.getId()).get();
        certificate.setTags(certificate1.getTags());
        certificate.setCreateDate(certificate1.getCreateDate());
        certificate.setLastUpdateDate(certificate1.getLastUpdateDate());
        if (certificate.getDuration() == null) {
            certificate.setDuration(certificate1.getDuration());
        }
        giftCertificateRepository.save(certificate);
    }

    public GiftCertificate getCertificateWithTags(long certificateId) {
        return certificateRepository.fetchCertificate(certificateId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    public List<GiftCertificate> getAll() {
        try {
            return giftCertificateRepository.findAll();
        } catch (DataAccessException e) {
            throw new EntityNotFoundException("can't receive GiftCertificate list", Error.GiftCertificateNotFound);
        }
    }

    public List<GiftCertificate> getGiftCertificatesParametrized(String tagName,
                                                                 String name,
                                                                 String description,
                                                                 String sortOrder,
                                                                 Optional<String> sortByDate,
                                                                 Optional<String> sortByName) {
        List<GiftCertificate> list = certificateRepository.fetchAllParametrized(name, description, sortOrder, sortByDate, sortByName);
        if (tagName == null) {
            return list;
        }
        return filterByTagName(list, tagName);
    }

    private List<GiftCertificate> filterByTagName(List<GiftCertificate> giftCertificateDTOS, String tagName) {
        return giftCertificateDTOS.stream()
                .filter(
                        e -> e.getTags().stream()
                                .anyMatch(t -> t.getName().contains(tagName))
                ).toList();
    }


    public GiftCertificate updateCertificatePrice(GiftCertificatePriceOnly certificatePriceDto) {
        GiftCertificate certificate = giftCertificateRepository.findById(certificatePriceDto.getId()).orElseThrow(() -> new EntityNotFoundException("certificates with coresponding id are absent", Error.GiftCertificateNotFound));
        certificate.setPrice(certificatePriceDto.getPrice());
        return certificate;
    }

    public List<GiftCertificate> getCertificatesWhichContainsTags(Set<Long> tagsIds) {
        return certificateRepository.fetchAllCertificatesWithTagId(tagsIds);
    }
}
