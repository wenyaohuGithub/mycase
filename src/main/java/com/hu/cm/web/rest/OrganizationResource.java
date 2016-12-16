package com.hu.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hu.cm.domain.Organization;
import com.hu.cm.repository.OrganizationRepository;
import com.hu.cm.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Organization.
 */
@RestController
@RequestMapping("/api")
public class OrganizationResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationResource.class);

    @Inject
    private OrganizationRepository organizationRepository;

    /**
     * POST  /organizations -> Create a new organization.
     */
    @RequestMapping(value = "/organizations",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Organization> create(@Valid @RequestBody Organization organization) throws URISyntaxException {
        log.debug("REST request to save Organization : {}", organization);
        if (organization.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new organization cannot already have an ID").body(null);
        }
        Organization result = organizationRepository.save(organization);
        return ResponseEntity.created(new URI("/api/organizations/" + organization.getId())).body(result);
    }

    /**
     * PUT  /organizations -> Updates an existing organization.
     */
    @RequestMapping(value = "/organizations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Organization> update(@Valid @RequestBody Organization organization) throws URISyntaxException {
        log.debug("REST request to update Organization : {}", organization);
        if (organization.getId() == null) {
            return create(organization);
        }
        Organization result = organizationRepository.save(organization);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /organizations -> get all the organizations.
     */
    @RequestMapping(value = "/organizations",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Organization>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Organization> page = organizationRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organizations", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /organizations/:id -> get the "id" organization.
     */
    @RequestMapping(value = "/organizations/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Organization> get(@PathVariable Long id) {
        log.debug("REST request to get Organization : {}", id);
        return Optional.ofNullable(organizationRepository.findOne(id))
            .map(organization -> new ResponseEntity<>(
                organization,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /organizations/:id -> delete the "id" organization.
     */
    @RequestMapping(value = "/organizations/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Organization : {}", id);
        organizationRepository.delete(id);
    }
}
