package com.epam.esm.controller;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.dto.dto.GiftCertificateDTO;
import com.epam.esm.exception.EntityUpdateException;
import com.epam.esm.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * REST Controller for basic operation with GiftCertificate
 */
@RestController
@RequestMapping("certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    /**
     * return certificate with {id}
     *
     * @param id requested certificate id
     *           if it is present in the db
     * @return found certificate or 404 if no certificate found
     */
    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public GiftCertificate fetchById(@PathVariable long id) {
        return certificateService.getCertificate(id);
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
        ResponseEntity<GiftCertificateDTO> responseEntity = ResponseEntity
                .status(CREATED)
                .header(HttpHeaders.LOCATION, uri.toString())
                .body(dto);

        return responseEntity;
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
        if (certificateService.updateCertificate(certificateDTO)) {
            return certificateService.getCertificateWithTags(certificateDTO.getId());
        } else throw new EntityUpdateException("no new data for updating");
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
    public List<GiftCertificateDTO> fetchAllCertificatesParametrized(@RequestParam(required = false) String tagName,
                                                                     @RequestParam(required = false) String name,
                                                                     @RequestParam(required = false) String description,
                                                                     @RequestParam(required = false) String sortOrder,
                                                                     @RequestParam(required = false) String sortByDate,
                                                                     @RequestParam(required = false) String sortByName) {
        List<GiftCertificateDTO> list = null;
        if (tagName != null) {
            list = certificateService.getAllCertificateWithTagName(tagName);
        }
        if (name != null) {
            list = (list == null) ? certificateService.getAllCertificateWithName(name) : list.stream()
                    .filter(dto -> dto.getName().contains(name))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        if (description != null) {
            list = (list == null) ? certificateService.getAllCertificateWithDescription(description) : list.stream()
                    .filter(dto -> dto.getDescription().contains(description))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        if (list == null) {
            return certificateService.getAll();
        }
        sortOrder = (sortOrder == null) ? "ASC" : sortOrder;

        Comparator<GiftCertificateDTO> comparatorByName = (a, b) -> a.getName().compareToIgnoreCase(b.getName());
        Comparator<GiftCertificateDTO> comparatorByDate = Comparator.comparing(GiftCertificateDTO::getCreationDate);
        Comparator<GiftCertificateDTO> doubleComparator = comparatorByName.thenComparing(comparatorByDate);

        if (sortOrder.equalsIgnoreCase("DESC")) {
            comparatorByName = comparatorByName.reversed();
            comparatorByDate = comparatorByDate.reversed();
            doubleComparator = doubleComparator.reversed();
        }
        if (sortByName != null) {
            if (sortByDate != null) {
                list = list.stream()
                        .sorted(doubleComparator)
                        .collect(Collectors.toCollection(ArrayList::new));
            } else list = list.stream()
                    .sorted(comparatorByName)
                    .collect(Collectors.toCollection(ArrayList::new));
        } else if (sortByDate != null) {
            list = list.stream()
                    .sorted(comparatorByDate)
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        return list;
    }
}
