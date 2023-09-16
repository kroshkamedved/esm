package com.epam.esm.controller;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.dto.GiftCertificatePriceOnly;
import com.epam.esm.exception.EntityUpdateException;
import com.epam.esm.hateoas.assembler.GiftCertificateModelAssembler;
import com.epam.esm.hateoas.model.CertificateModel;
import com.epam.esm.service.CertificateService;
import com.epam.esm.util.LinkUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * REST Controller for basic operation with GiftCertificate
 */
@RestController
@RequestMapping("certificates")
@RequiredArgsConstructor
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class CertificateController {
    private final CertificateService certificateService;
    private final GiftCertificateModelAssembler modelAssembler;
    private final PagedResourcesAssembler<GiftCertificate> pagedResourcesAssembler;

    /**
     * return certificate with {id}
     *
     * @param id requested certificate id
     *           if it is present in the db
     * @return found certificate or 404 if no certificate found
     */
    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public CertificateModel fetchById(@PathVariable long id) {
        return modelAssembler.toModel(certificateService.getCertificate(id));
    }

    /**
     * Delete GiftCertificate with {id}
     * if it's present in the db
     *
     * @param id requested certificate id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(OK)
    public void deleteById(@PathVariable long id) {
        certificateService.deleteCertificate(id);
    }

    /**
     * Create new GiftCertificate
     * and save it to the db with all tags which was linked to the certificate in certificateDTO
     *
     * @param certificate - certificate and linked tags
     * @return certificateDTO with info from db for newly created certificate
     */
    @PostMapping
    @ResponseStatus(CREATED)
    public CertificateModel createCertificate(@RequestBody GiftCertificate certificate) {
        return modelAssembler.toModel(certificateService.addCertificate(certificate));
    }

    /**
     * Update certificate with info from dto. Certificate id is mandatory for request body.
     *
     * @param certificate - dto with all certificate fields and linked tags.
     * @return CertificateModel for corresponding certificate with all date obtained from db after update
     * @throws EntityUpdateException in case of absence of new relevant date in request body.
     */
    @PutMapping()
    @ResponseStatus(OK)
    public CertificateModel updateCertificate(@RequestBody GiftCertificate certificate) {
        certificateService.updateCertificate(certificate);
        return modelAssembler.toModel(certificateService.getCertificate(certificate.getId()));
    }

    /**
     * Return list of certificates that match the following filters.
     * Filters are optional and can be used in conjunction, but should be passed as request parameters if they needed.
     *
     * @param tagName     return all certificates which have linked tags with tag name that satisfy the rule "like %tagName%"
     * @param name        return all certificates which names satisfy the rule - certificate name "like %name%"
     * @param description return all certificates which description satisfy the rule - description "like %description%"
     * @param sortOrder   indicate the sort order. Possible values : ASC and DESC. Register is unimportant.
     * @param sortByDate  set the sorting algorithm to sort by date
     * @param sortByName  set the sorting algorithm to sort by name. Default sorting algorithm, works when sorting required
     *                    but no sortBy parameters was present in request parameters;
     * @return List of GiftCertificateDTO that match the request.
     */
    @GetMapping
    @ResponseStatus(OK)
    public List<CertificateModel> fetchAllCertificatesParametrized(@RequestParam(required = false) String tagName,
                                                                   @RequestParam(required = false) String name,
                                                                   @RequestParam(required = false) String description,
                                                                   @RequestParam(required = false) String sortOrder,
                                                                   @RequestParam(required = false) Optional<String> sortByDate,
                                                                   @RequestParam(required = false) Optional<String> sortByName) {
        List<GiftCertificate> certificatesParametrized = certificateService.getGiftCertificatesParametrized(
                tagName,
                name,
                description,
                sortOrder,
                sortByDate,
                sortByName
        );
        LinkUtil.addSelfLinksToTags(certificatesParametrized.stream().flatMap(c -> c.getTags().stream()).collect(Collectors.toList()));
        return certificatesParametrized.stream()
                .map(modelAssembler::toModel).collect(Collectors.toList());
    }


    /**
     * Change single field of gift certificate
     *
     * @param certificatePriceDto
     * @param id
     * @return GiftCertificateDTO for corresponding certificate with all date obtained from db after patch
     */
    @PatchMapping("/{id}")
    @ResponseStatus(OK)
    public CertificateModel changeCertificatePrice(@RequestBody GiftCertificatePriceOnly certificatePriceDto,
                                                   @PathVariable long id) {
        if (id != certificatePriceDto.getId()) {
            throw new EntityUpdateException("request body doesn't correspond to id path variable");
        }
        return modelAssembler.toModel(certificateService.updateCertificatePrice(certificatePriceDto));
    }

    /**
     * Search for gift certificates by several tags (
     *
     * @param tagsIds
     * @return found GiftCertificates or 404 if no certificate found
     */

    @GetMapping(params = {"tagId"})
    @ResponseStatus(OK)
    public List<CertificateModel> fetchByTags
    (@RequestParam(required = true, name = "tagId") Set<Long> tagsIds) {
        List<GiftCertificate> certificatesWhichContainsTags = certificateService.getCertificatesWhichContainsTags(tagsIds);
        return certificatesWhichContainsTags.stream()
                .map(modelAssembler::toModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/all")
    public PagedModel<CertificateModel> fetchAll(Pageable pageable){
        return pagedResourcesAssembler.toModel(certificateService.getAll(pageable),modelAssembler);
    }
}