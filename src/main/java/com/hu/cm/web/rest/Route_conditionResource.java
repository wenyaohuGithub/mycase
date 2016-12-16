package com.hu.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hu.cm.domain.Route_condition;
import com.hu.cm.repository.Route_conditionRepository;
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
 * REST controller for managing Route_condition.
 */
@RestController
@RequestMapping("/api")
public class Route_conditionResource {

    private final Logger log = LoggerFactory.getLogger(Route_conditionResource.class);

    @Inject
    private Route_conditionRepository route_conditionRepository;

    /**
     * POST  /route_conditions -> Create a new route_condition.
     */
    @RequestMapping(value = "/route_conditions",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Route_condition> create(@RequestBody Route_condition route_condition) throws URISyntaxException {
        log.debug("REST request to save Route_condition : {}", route_condition);
        if (route_condition.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new route_condition cannot already have an ID").body(null);
        }
        Route_condition result = route_conditionRepository.save(route_condition);
        return ResponseEntity.created(new URI("/api/route_conditions/" + route_condition.getId())).body(result);
    }

    /**
     * PUT  /route_conditions -> Updates an existing route_condition.
     */
    @RequestMapping(value = "/route_conditions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Route_condition> update(@RequestBody Route_condition route_condition) throws URISyntaxException {
        log.debug("REST request to update Route_condition : {}", route_condition);
        if (route_condition.getId() == null) {
            return create(route_condition);
        }
        Route_condition result = route_conditionRepository.save(route_condition);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /route_conditions -> get all the route_conditions.
     */
    @RequestMapping(value = "/route_conditions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Route_condition>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Route_condition> page = route_conditionRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/route_conditions", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /route_conditions/:id -> get the "id" route_condition.
     */
    @RequestMapping(value = "/route_conditions/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Route_condition> get(@PathVariable Long id) {
        log.debug("REST request to get Route_condition : {}", id);
        return Optional.ofNullable(route_conditionRepository.findOne(id))
            .map(route_condition -> new ResponseEntity<>(
                route_condition,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /route_conditions/:id -> delete the "id" route_condition.
     */
    @RequestMapping(value = "/route_conditions/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Route_condition : {}", id);
        route_conditionRepository.delete(id);
    }
}
