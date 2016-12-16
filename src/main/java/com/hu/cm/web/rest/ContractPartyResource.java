package com.hu.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hu.cm.domain.ContractParty;
import com.hu.cm.repository.ContractPartyRepository;
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
 * REST controller for managing ContractParty.
 */
@RestController
@RequestMapping("/api")
public class ContractPartyResource {

    private final Logger log = LoggerFactory.getLogger(ContractPartyResource.class);

    @Inject
    private ContractPartyRepository contract_partyRepository;

    /**
     * POST  /contract_partys -> Create a new contract_party.
     */
    @RequestMapping(value = "/contract_partys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContractParty> create(@Valid @RequestBody ContractParty contract_party) throws URISyntaxException {
        log.debug("REST request to save ContractParty : {}", contract_party);
        if (contract_party.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new contract_party cannot already have an ID").body(null);
        }
        ContractParty result = contract_partyRepository.save(contract_party);
        return ResponseEntity.created(new URI("/api/contract_partys/" + contract_party.getId())).body(result);
    }

    /**
     * PUT  /contract_partys -> Updates an existing contract_party.
     */
    @RequestMapping(value = "/contract_partys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContractParty> update(@Valid @RequestBody ContractParty contract_party) throws URISyntaxException {
        log.debug("REST request to update ContractParty : {}", contract_party);
        if (contract_party.getId() == null) {
            return create(contract_party);
        }
        ContractParty result = contract_partyRepository.save(contract_party);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /contract_partys -> get all the contract_partys.
     */
    @RequestMapping(value = "/contract_partys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ContractParty>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                                      @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<ContractParty> page = contract_partyRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contract_partys", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /contract_partys/:id -> get the "id" contract_party.
     */
    @RequestMapping(value = "/contract_partys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContractParty> get(@PathVariable Long id) {
        log.debug("REST request to get ContractParty : {}", id);
        return Optional.ofNullable(contract_partyRepository.findOne(id))
            .map(contract_party -> new ResponseEntity<>(
                contract_party,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /contract_partys/:id -> delete the "id" contract_party.
     */
    @RequestMapping(value = "/contract_partys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ContractParty : {}", id);
        contract_partyRepository.delete(id);
    }
}
