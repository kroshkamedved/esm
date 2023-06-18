package com.epam.esm.service;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.GiftCertificatePriceOnly;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.Error;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.assembler.CertificateDTOAssembler;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class CertificateService {
    private final CertificateRepository certificateRepository;
    private final TagRepository tagRepository;
    private final CertificateDTOAssembler certificateDTOMapper;

    public GiftCertificateDTO getCertificate(long id) {
        GiftCertificate certificate = certificateRepository.fetchCertificate(id).orElseThrow(() -> new EntityNotFoundException("can't receive GiftCertificate with id = " + id, Error.GiftCertificateNotFound));
        List<Tag> tags = tagRepository.fetchLinkedTags(id);
        return certificateDTOMapper.mapToDTO(certificate, tags);
    }

    @Transactional
    public GiftCertificateDTO addCertificate(GiftCertificateDTO certificateDTO) {
        GiftCertificate giftCertificate = certificateDTOMapper.mapToCertificate(certificateDTO);
        GiftCertificate certificateFromDb = certificateRepository.createCertificate(giftCertificate);
        tagRepository.saveTagsForCertificate(certificateDTO.getTags(), certificateFromDb.getId());
        certificateFromDb = certificateRepository.fetchCertificate(certificateFromDb.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        List<Tag> tagsFromDb = tagRepository.fetchLinkedTags(certificateFromDb.getId());
        return certificateDTOMapper.mapToDTO(certificateFromDb, tagsFromDb);
    }

    public void deleteCertificate(long id) {
        certificateRepository.deleteCertificate(id);
    }

    @Transactional
    public boolean updateCertificate(GiftCertificateDTO certificateDTO) {
        GiftCertificate certificate = certificateDTOMapper.mapToCertificate(certificateDTO);

        boolean newTagLinked = false;
        if (certificateDTO.getTags() != null) {
            newTagLinked = tagRepository.saveTagsForCertificate(certificateDTO.getTags(), certificate.getId());
        }
        return (newTagLinked | certificateRepository.updateCertificate(certificateDTO));
    }

    public GiftCertificateDTO getCertificateWithTags(long certificateId) {
        GiftCertificate giftCertificate = certificateRepository.fetchCertificate(certificateId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        List<Tag> tags = tagRepository.fetchLinkedTags(certificateId);
        return certificateDTOMapper.mapToDTO(giftCertificate, tags);
    }

    public List<GiftCertificateDTO> getAll() {
        try {
            List<GiftCertificate> certificates = certificateRepository.fetchAll();
            return getGiftCertificateDTOS(certificates);
        } catch (DataAccessException e) {
            throw new EntityNotFoundException("can't receive GiftCertificate list", Error.GiftCertificateNotFound);
        }
    }

    @NotNull
    private List<GiftCertificateDTO> getGiftCertificateDTOS(List<GiftCertificate> certificates) {
        List<GiftCertificateDTO> certificateDTOList = new ArrayList<>();
        for (GiftCertificate current : certificates) {
            certificateDTOList.add(getCertificateWithTags(current.getId()));
        }
        return certificateDTOList;
    }

    public List<GiftCertificateDTO> getAllCertificateWithTagName(String tagName) {
        List<GiftCertificate> list = certificateRepository.fetchAllCertificatesWithTagName(tagName);
        return getGiftCertificateDTOS(list);
    }

    public List<GiftCertificateDTO> getAllCertificateWithName(String name) {
        List<GiftCertificate> list = certificateRepository.fetchAllCertificatesWithName(name);
        return getGiftCertificateDTOS(list);
    }

    public List<GiftCertificateDTO> getAllCertificateWithDescription(String description) {
        List<GiftCertificate> list = certificateRepository.fetchAllCertificatesWithDescription(description);
        return getGiftCertificateDTOS(list);
    }

    public List<GiftCertificateDTO> getCirtificatesParametrized(String tagName,
                                                                String name,
                                                                String description,
                                                                String sortOrder,
                                                                Optional<String> sortByDate,
                                                                Optional<String> sortByName) {
        List<GiftCertificate> list = certificateRepository.fetchAllParametrized(name, description, sortOrder, sortByDate, sortByName);
        List<GiftCertificateDTO> giftCertificateDTOS = getGiftCertificateDTOS(list);
        if (tagName == null) {
            return giftCertificateDTOS;
        }
        return filterByTagName(giftCertificateDTOS, tagName);
    }

    private List<GiftCertificateDTO> filterByTagName(List<GiftCertificateDTO> giftCertificateDTOS, String tagName) {
        return giftCertificateDTOS.stream()
                .filter(
                        e -> e.getTags().stream()
                                .anyMatch(t -> t.getName().contains(tagName))
                ).toList();
    }


    public GiftCertificateDTO updateCertificatePrice(GiftCertificatePriceOnly certificatePriceDto) {
        certificateRepository.updateCertificatePrice(certificatePriceDto);
        return getCertificateWithTags(certificatePriceDto.getId());
    }

    public List<GiftCertificate> getCertificatesWhichContainsTags(Set<Long> tagsIds) {
        List<GiftCertificate> list = certificateRepository.fetchAllCertificatesWithTagId(tagsIds);
        if (list.isEmpty()) {
            throw new EntityNotFoundException("certificates with coresponding requested tags are absent", Error.GiftCertificateNotFound);
        }
        return list;
    }

    public int getTotalRecordsWithTag(Set<Long> tagsIds) {
        return certificateRepository.countAllCertificatesWithRequestdTags(tagsIds);
    }
}
