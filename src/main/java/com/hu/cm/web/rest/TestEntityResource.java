package com.hu.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hu.cm.domain.TestEntity;
import com.hu.cm.repository.TestEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing TestEntity.
 */
@RestController
@RequestMapping("/api")
public class TestEntityResource {

    private final Logger log = LoggerFactory.getLogger(TestEntityResource.class);

    @Inject
    private TestEntityRepository testEntityRepository;

    /**
     * POST  /testEntitys -> Create a new testEntity.
     */
    @RequestMapping(value = "/testEntitys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TestEntity> create(@RequestBody TestEntity testEntity) throws URISyntaxException {
        log.debug("REST request to save TestEntity : {}", testEntity);
        if (testEntity.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new testEntity cannot already have an ID").body(null);
        }
        TestEntity result = testEntityRepository.save(testEntity);
        return ResponseEntity.created(new URI("/api/testEntitys/" + testEntity.getId())).body(result);
    }

    /**
     * PUT  /testEntitys -> Updates an existing testEntity.
     */
    @RequestMapping(value = "/testEntitys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TestEntity> update(@RequestBody TestEntity testEntity) throws URISyntaxException {
        log.debug("REST request to update TestEntity : {}", testEntity);
        if (testEntity.getId() == null) {
            return create(testEntity);
        }
        TestEntity result = testEntityRepository.save(testEntity);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /testEntitys -> get all the testEntitys.
     */
    @RequestMapping(value = "/testEntitys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TestEntity> getAll() {
        log.debug("REST request to get all TestEntitys");
        return testEntityRepository.findAll();
    }

    /**
     * GET  /testEntitys/:id -> get the "id" testEntity.
     */
    @RequestMapping(value = "/testEntitys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TestEntity> get(@PathVariable Long id) {
        log.debug("REST request to get TestEntity : {}", id);
        return Optional.ofNullable(testEntityRepository.findOne(id))
            .map(testEntity -> new ResponseEntity<>(
                testEntity,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /testEntitys/:id -> delete the "id" testEntity.
     */
    @RequestMapping(value = "/testEntitys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete TestEntity : {}", id);
        testEntityRepository.delete(id);
    }
}
