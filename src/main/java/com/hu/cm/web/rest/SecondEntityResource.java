package com.hu.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hu.cm.domain.SecondEntity;
import com.hu.cm.repository.SecondEntityRepository;
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
 * REST controller for managing SecondEntity.
 */
@RestController
@RequestMapping("/api")
public class SecondEntityResource {

    private final Logger log = LoggerFactory.getLogger(SecondEntityResource.class);

    @Inject
    private SecondEntityRepository secondEntityRepository;

    /**
     * POST  /secondEntitys -> Create a new secondEntity.
     */
    @RequestMapping(value = "/secondEntitys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecondEntity> create(@RequestBody SecondEntity secondEntity) throws URISyntaxException {
        log.debug("REST request to save SecondEntity : {}", secondEntity);
        if (secondEntity.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new secondEntity cannot already have an ID").body(null);
        }
        SecondEntity result = secondEntityRepository.save(secondEntity);
        return ResponseEntity.created(new URI("/api/secondEntitys/" + secondEntity.getId())).body(result);
    }

    /**
     * PUT  /secondEntitys -> Updates an existing secondEntity.
     */
    @RequestMapping(value = "/secondEntitys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecondEntity> update(@RequestBody SecondEntity secondEntity) throws URISyntaxException {
        log.debug("REST request to update SecondEntity : {}", secondEntity);
        if (secondEntity.getId() == null) {
            return create(secondEntity);
        }
        SecondEntity result = secondEntityRepository.save(secondEntity);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /secondEntitys -> get all the secondEntitys.
     */
    @RequestMapping(value = "/secondEntitys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SecondEntity>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<SecondEntity> page = secondEntityRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/secondEntitys", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /secondEntitys/:id -> get the "id" secondEntity.
     */
    @RequestMapping(value = "/secondEntitys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecondEntity> get(@PathVariable Long id) {
        log.debug("REST request to get SecondEntity : {}", id);
        return Optional.ofNullable(secondEntityRepository.findOne(id))
            .map(secondEntity -> new ResponseEntity<>(
                secondEntity,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /secondEntitys/:id -> delete the "id" secondEntity.
     */
    @RequestMapping(value = "/secondEntitys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete SecondEntity : {}", id);
        secondEntityRepository.delete(id);
    }
}
