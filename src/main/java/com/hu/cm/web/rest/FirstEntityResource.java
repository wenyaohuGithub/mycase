package com.hu.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hu.cm.domain.FirstEntity;
import com.hu.cm.repository.FirstEntityRepository;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FirstEntity.
 */
@RestController
@RequestMapping("/api")
public class FirstEntityResource {

    private final Logger log = LoggerFactory.getLogger(FirstEntityResource.class);

    @Inject
    private FirstEntityRepository firstEntityRepository;

    /**
     * POST  /firstEntitys -> Create a new firstEntity.
     */
    @RequestMapping(value = "/firstEntitys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FirstEntity> create(@RequestBody FirstEntity firstEntity) throws URISyntaxException {
        log.debug("REST request to save FirstEntity : {}", firstEntity);
        if (firstEntity.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new firstEntity cannot already have an ID").body(null);
        }
        FirstEntity result = firstEntityRepository.save(firstEntity);
        return ResponseEntity.created(new URI("/api/firstEntitys/" + firstEntity.getId())).body(result);
    }

    /**
     * PUT  /firstEntitys -> Updates an existing firstEntity.
     */
    @RequestMapping(value = "/firstEntitys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FirstEntity> update(@RequestBody FirstEntity firstEntity) throws URISyntaxException {
        log.debug("REST request to update FirstEntity : {}", firstEntity);
        if (firstEntity.getId() == null) {
            return create(firstEntity);
        }
        FirstEntity result = firstEntityRepository.save(firstEntity);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /firstEntitys -> get all the firstEntitys.
     */
    @RequestMapping(value = "/firstEntitys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<FirstEntity>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<FirstEntity> page = firstEntityRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/firstEntitys", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /firstEntitys/:id -> get the "id" firstEntity.
     */
    @RequestMapping(value = "/firstEntitys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FirstEntity> get(@PathVariable Long id) {
        log.debug("REST request to get FirstEntity : {}", id);
        return Optional.ofNullable(firstEntityRepository.findOne(id))
            .map(firstEntity -> new ResponseEntity<>(
                firstEntity,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /firstEntitys/:id -> delete the "id" firstEntity.
     */
    @RequestMapping(value = "/firstEntitys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete FirstEntity : {}", id);
        firstEntityRepository.delete(id);
    }
}
