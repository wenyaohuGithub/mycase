package com.hu.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hu.cm.domain.ContractHistory;
import com.hu.cm.repository.ContractHistoryRepository;
import com.hu.cm.web.rest.dto.ContractHistoryDTO;
import com.hu.cm.web.rest.util.PaginationUtil;
import com.hu.cm.web.rest.mapper.ContractHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing ContractHistory.
 */
@RestController
@RequestMapping("/api")
public class ContractHistoryResource {

    private final Logger log = LoggerFactory.getLogger(ContractHistoryResource.class);

    @Inject
    private ContractHistoryRepository contract_historyRepository;

    @Inject
    private ContractHistoryMapper contract_historyMapper;

    /**
     * POST  /contract_historys -> Create a new contract_history.
     */
    @RequestMapping(value = "/contract_historys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContractHistoryDTO> create(@RequestBody ContractHistoryDTO contract_historyDTO) throws URISyntaxException {
        log.debug("REST request to save ContractHistory : {}", contract_historyDTO);
        if (contract_historyDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new contract_history cannot already have an ID").body(null);
        }
        ContractHistory contract_history = contract_historyMapper.contract_historyDTOToContract_history(contract_historyDTO);
        ContractHistory result = contract_historyRepository.save(contract_history);
        return ResponseEntity.created(new URI("/api/contract_historys/" + contract_historyDTO.getId())).body(contract_historyMapper.contract_historyToContract_historyDTO(result));
    }

    /**
     * PUT  /contract_historys -> Updates an existing contract_history.
     */
    @RequestMapping(value = "/contract_historys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContractHistoryDTO> update(@RequestBody ContractHistoryDTO contract_historyDTO) throws URISyntaxException {
        log.debug("REST request to update ContractHistory : {}", contract_historyDTO);
        if (contract_historyDTO.getId() == null) {
            return create(contract_historyDTO);
        }
        ContractHistory contract_history = contract_historyMapper.contract_historyDTOToContract_history(contract_historyDTO);
        ContractHistory result = contract_historyRepository.save(contract_history);
        return ResponseEntity.ok().body(contract_historyMapper.contract_historyToContract_historyDTO(result));
    }

    /**
     * GET  /contract_historys -> get all the contract_historys.
     */
    @RequestMapping(value = "/contract_historys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ContractHistoryDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                                           @RequestParam(value = "per_page", required = false) Integer limit,
                                                           @RequestParam(value = "contractId", required = false) Long contractId)
        throws URISyntaxException {
        Page<ContractHistory> page = null;
        HttpHeaders headers = null;
        if(contractId == null){
            page = contract_historyRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
            headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contract_historys", offset, limit);
            return new ResponseEntity<>(page.getContent().stream()
                .map(contract_historyMapper::contract_historyToContract_historyDTO)
                .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
        } else {
            List<ContractHistory> list = contract_historyRepository.findAllForContract(contractId);
            return new ResponseEntity<>(list.stream()
                .map(contract_historyMapper::contract_historyToContract_historyDTO)
                .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
        }
    }

    /**
     * GET  /contract_historys/:id -> get the "id" contract_history.
     */
    @RequestMapping(value = "/contract_historys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContractHistoryDTO> get(@PathVariable Long id) {
        log.debug("REST request to get ContractHistory : {}", id);
        return Optional.ofNullable(contract_historyRepository.findOne(id))
            .map(contract_historyMapper::contract_historyToContract_historyDTO)
            .map(contract_historyDTO -> new ResponseEntity<>(
                contract_historyDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /contract_historys/:id -> delete the "id" contract_history.
     */
    @RequestMapping(value = "/contract_historys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ContractHistory : {}", id);
        contract_historyRepository.delete(id);
    }
}
