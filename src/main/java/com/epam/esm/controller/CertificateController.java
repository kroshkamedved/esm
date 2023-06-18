package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.GiftCertificatePriceOnly;
import com.epam.esm.event.SingleResourceRetrieved;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.EntityUpdateException;
import com.epam.esm.exception.Error;
import com.epam.esm.hateoas.assembler.GIftCertificateModelAssembler;
import com.epam.esm.service.CertificateService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.epam.esm.util.LinkUtil.addSelfLinksToTagAndCertificate;
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
    private final ApplicationEventPublisher eventPublisher;
    private final CertificateService certificateService;
    private final GIftCertificateModelAssembler modelAssembler;

    /**
     * return certificate with {id}
     *
     * @param id requested certificate id
     *           if it is present in the db
     * @return found certificate or 404 if no certificate found
     */
    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public EntityModel<GiftCertificateDTO> fetchById(@PathVariable long id, HttpServletResponse response) {
        eventPublisher.publishEvent(new SingleResourceRetrieved(response, this));
        GiftCertificateDTO certificateDTO = certificateService.getCertificate(id);
        return modelAssembler.toModel(certificateDTO);
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
     * @param certificateDTO - dto with certificate and linked tags
     * @return certificateDTO with info from db for newly created certificate
     */
    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<GiftCertificateDTO> createCertificate(@RequestBody GiftCertificateDTO certificateDTO) {
        GiftCertificateDTO dto = certificateService.addCertificate(certificateDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity
                .status(CREATED)
                .header(HttpHeaders.LOCATION, uri.toASCIIString())
                .body(dto);
    }

    /**
     * Update certificate with info from dto. Certificate id is mandatory for request body.
     *
     * @param certificateDTO - dto with all certificate fields and linked tags.
     * @return GiftCertificateDTO for corresponding certificate with all date obtained from db after update
     * @throws EntityUpdateException in case of absence of new relevant date in request body.
     */
    @PutMapping()
    @ResponseStatus(OK)
    public GiftCertificateDTO updateCertificate(@RequestBody GiftCertificateDTO certificateDTO) {
        certificateService.updateCertificate(certificateDTO);
        return certificateService.getCertificateWithTags(certificateDTO.getId());
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
    public List<GiftCertificateDTO> fetchAllCertificatesParametrized(@RequestParam(required = false) String
                                                                             tagName,
                                                                     @RequestParam(required = false) String name,
                                                                     @RequestParam(required = false) String description,
                                                                     @RequestParam(required = false) String sortOrder,
                                                                     @RequestParam(required = false) Optional<String> sortByDate,
                                                                     @RequestParam(required = false) Optional<String> sortByName) {
        List<GiftCertificateDTO> cirtificatesParametrized = certificateService.getCirtificatesParametrized(
                tagName,
                name,
                description,
                sortOrder,
                sortByDate,
                sortByName
        );
        for (final GiftCertificateDTO certificate : cirtificatesParametrized) {
            addSelfLinksToTagAndCertificate(certificate);
        }
        return cirtificatesParametrized;
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
    public GiftCertificateDTO changeCertificatePrice(@RequestBody GiftCertificatePriceOnly certificatePriceDto,
                                                     @PathVariable long id) {
        if (id != certificatePriceDto.getId()) {
            throw new EntityUpdateException("request body doesn't correspond to id path variable");
        }
        return certificateService.updateCertificatePrice(certificatePriceDto);
    }

    /**
     * Search for gift certificates by several tags (
     *
     * @param tagsIds
     * @return found GiftCertificates or 404 if no certificate found
     */

    @GetMapping(params = {"tagId"})
    @ResponseStatus(OK)
    public List<GiftCertificateDTO> fetchByTags
    (@RequestParam(required = true, name = "tagId") Set<Long> tagsIds) {
        return certificateService.getCertificatesWhichContainsTags(tagsIds).orElseThrow(() -> new EntityNotFoundException("no certificates with tags : " + tagsIds, Error.GiftCertificateNotFound));
    }
}