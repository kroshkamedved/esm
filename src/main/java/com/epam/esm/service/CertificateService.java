package com.epam.esm.service;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.dto.dto.GiftCertificateDTO;
import com.epam.esm.repository.mappers.CertificateDTOAssembler;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CertificateService {
    private final CertificateRepository certificateRepository;
    private final TagRepository tagRepository;
    private final CertificateDTOAssembler certificateDTOMapper;

    @Autowired
    public CertificateService(@Qualifier("MYSQL") CertificateRepository certificateRepository, CertificateDTOAssembler mapper, TagRepository tagRepository) {
        this.certificateRepository = certificateRepository;
        this.certificateDTOMapper = mapper;
        this.tagRepository = tagRepository;
    }

    public GiftCertificate getCertificate(long id) {
        return certificateRepository.fetchCertificate(id).orElseThrow(() -> new EntityNotFoundException("can't receive GiftCertificate with id = " + id));
    }

    @Transactional
    public GiftCertificateDTO addCertificate(GiftCertificateDTO certificateDTO) {
        GiftCertificate giftCertificate = certificateDTOMapper.mapToCertificate(certificateDTO);
        GiftCertificate certificateFromDb = certificateRepository.createCertificate(giftCertificate);
        tagRepository.saveTagsForCertificate(certificateDTO.getTags(), certificateFromDb.getId());
        certificateFromDb = certificateRepository.fetchCertificate(certificateFromDb.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        List<Tag> tagsFromDb = tagRepository.fetchLinkedTags(certificateFromDb.getId());
        GiftCertificateDTO dto = certificateDTOMapper.mapToDTO(certificateFromDb, tagsFromDb);
        return dto;
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
        boolean updated = (newTagLinked | certificateRepository.updateCertificate(certificateDTO));
        return updated;
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
            throw new EntityNotFoundException("can't receive GiftCertificate list");
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
}
