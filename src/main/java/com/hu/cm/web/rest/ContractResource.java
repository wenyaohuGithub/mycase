package com.hu.cm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hu.cm.domain.*;
import com.hu.cm.domain.admin.Department;
import com.hu.cm.domain.admin.User;
import com.hu.cm.domain.configuration.Process;
import com.hu.cm.domain.enumeration.UserAction;
import com.hu.cm.repository.*;
import com.hu.cm.repository.admin.DepartmentRepository;
import com.hu.cm.repository.admin.UserRepository;
import com.hu.cm.repository.configuration.ProcessRepository;
import com.hu.cm.security.SecurityUtils;
import com.hu.cm.service.ContractService;
import com.hu.cm.service.ProcessService;
import com.hu.cm.web.rest.dto.*;
import com.hu.cm.web.rest.util.PaginationUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.ClassArrayEditor;
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
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Contract.
 */
@RestController
@RequestMapping("/api")
public class ContractResource {

    private final Logger log = LoggerFactory.getLogger(ContractResource.class);

    @Inject
    private ContractRepository contractRepository;

    @Inject
    private DepartmentRepository departmentRepository;

    @Inject
    private ContractPartyRepository contractPartyRepository;

    @Inject
    private CategoryRepository categoryRepository;

    @Inject
    private ProcessRepository processRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private ProjectRepository projectRepository;

    @Inject
    ContractHistoryRepository contractHistoryRepository;

    @Inject
    private ProcessService processService;

    @Inject
    private ContractService contractService;

    /**
     * POST  /contracts -> Create a new contract.
     */
    @RequestMapping(value = "/contracts",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Contract> create(@RequestBody ContractDTO dto) throws URISyntaxException {
        log.debug("REST request to save Contract : {}", dto);
        if (dto.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new contract cannot already have an ID").body(null);
        }
        Contract contract = mapFromDTO(dto);
        Contract result = contractService.createContract(contract);
        return ResponseEntity.created(new URI("/api/contracts/" + contract.getId())).body(result);
    }

    /**
     * PUT  /contracts -> Updates an existing contract.
     */
    @RequestMapping(value = "/contracts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Contract> update(@Valid @RequestBody Contract contract) throws URISyntaxException {
        log.debug("REST request to update Contract : {}", contract);
        if (contract.getId() == null) {
            return null;//create(contract);
        }
        Contract result = contractRepository.save(contract);
        return ResponseEntity.ok().body(result);
    }

    /**
     * PUT  /contracts -> Updates an existing contract.
     */
    @RequestMapping(value = "/contracts/{id}/nextProcess",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Contract> nextProcess(@PathVariable Long id, @RequestParam(value = "id" , required = true) Long processId)
        throws URISyntaxException {
        log.debug("REST request to update Contract : {}", id);
        Contract contract = contractRepository.findOne(id);
        ContractHistory history = new ContractHistory();
        history.setContract(contract);
        history.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get());
        history.setProcess(contract.getStatus());

        contract.setStatus(processRepository.findOne(processId));
        Contract result = contractRepository.save(contract);

        if(processId == 1) {
            history.setAction(UserAction.REJECT);
        }else {
            history.setAction(UserAction.APPROVE);
        }
        history.setAction_datetime(new DateTime());
        contractHistoryRepository.save(history);

        return ResponseEntity.ok().body(result);
    }

    /**
     * PUT  /contracts -> Updates an existing contract.
     */
    @RequestMapping(value = "/contracts/{id}/submit",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContractDTO> submitForReview(@PathVariable Long id, @RequestParam(value = "id" , required = true) Long processId)
        throws URISyntaxException {
        log.debug("REST request to submitForReview Contract : {}", id);
        Contract contract = contractRepository.findByIdAndFetchEager(id);
        if(contract != null){
            contract = contractService.submitForReview(contract);
            ContractDTO dto = map(contract);
            return ResponseEntity.ok().body(dto);
        }else {
            log.info("Contract with id "+ id + " not found!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * PUT  /contracts -> Reject an existing contract.
     */
    @RequestMapping(value = "/contracts/{id}/reject",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContractDTO> reject(@PathVariable Long id)
        throws URISyntaxException {
        log.debug("REST request to approve Contract : {}", id);
        Contract contract = contractRepository.findByIdAndFetchEager(id);
        if(contract != null){
            contract = contractService.rejectContract(contract);
            ContractDTO dto = map(contract);
            return ResponseEntity.ok().body(dto);
        }else {
            log.info("Contract with id "+ id + " not found!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * PUT  /contracts -> Approve an existing contract.
     */
    @RequestMapping(value = "/contracts/{id}/approve",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContractDTO> approve(@PathVariable Long id)
        throws URISyntaxException {
        log.debug("REST request to approve Contract : {}", id);
        Contract contract = contractRepository.findByIdAndFetchEager(id);
        if(contract != null){
            contract = contractService.approveContract(contract);
            ContractDTO dto = map(contract);
            return ResponseEntity.ok().body(dto);
        }else {
            log.info("Contract with id "+ id + " not found!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * GET  /contracts -> get all the contracts.
     */
    @RequestMapping(value = "/contracts",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Contract>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Contract> page = contractRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contracts", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /contracts/:id -> get the "id" contract.
     */
    @RequestMapping(value = "/contracts/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContractDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Contract : {}", id);

        Contract c = contractRepository.findByIdAndFetchEager(id);
        if (c != null){
            if(c.getStatus().getName().equals("CONTRACT_DRAFT") && c.getNextProcess() == null){
                c.setNextProcess(processService.getNextProcess(c.getCategory().getWorkflow().getId(),c.getStatus().getId()));
            }
            ContractDTO dto = map(c);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * DELETE  /contracts/:id -> delete the "id" contract.
     */
    @RequestMapping(value = "/contracts/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Contract : {}", id);
        contractRepository.delete(id);
    }

    /**
     * POST  /contracts/:id -> add comment to the "id" contract.
     */
    @RequestMapping(value = "/contracts/{id}/comment",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContractHistory> addComment(@RequestBody ContractHistory contractHistory, @PathVariable Long id) {
        log.debug("REST request to add comment to Contract : {}", id);

        if(contractRepository.findOne(id) == null){
            log.info("Add contract history to contract with id " + id + " not found!");
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            contractService.addComment(contractRepository.findOne(id), contractHistory);
            return  ResponseEntity.status(HttpStatus.OK).body(contractHistory);
        }
    }


    /* below are internal methods to map data object */
    private ContractDTO map(Contract contract){
        ContractDTO dto = new ContractDTO();
        dto.setId(contract.getId());
        dto.setName(contract.getName());
        dto.setDescription(contract.getDescription());
        dto.setStatus(new ProcessDTO(contract.getStatus().getName()));

        for(ContractParty cp : contract.getContractParties()){
            ContractPartyDTO pDto = new ContractPartyDTO(cp.getName());
            pDto.setId(cp.getId());
            dto.getContractParties().add(pDto);
        }
        for(Department dept : contract.getRelatedDepartments()){
            DepartmentDTO d = new DepartmentDTO(dept.getId());
            d.setName(dept.getName());
            dto.getRelatedDepartments().add(d);
        }
        for(Project project : contract.getProjects()){
            ProjectDTO pDto = new ProjectDTO(project.getId());
            pDto.setName(project.getName());
            pDto.setManager(project.getManager());
            dto.getProjects().add(pDto);
        }

        CategoryDTO catDto = new CategoryDTO(contract.getCategory().getId());
        catDto.setName(contract.getCategory().getName());
        dto.setCategory(catDto);
        dto.setAdministrator(contract.getAdministrator());
        DepartmentDTO deptDTO = new DepartmentDTO(contract.getAdministrativeDepartment().getId());
        deptDTO.setName(contract.getAdministrativeDepartment().getName());
        dto.setAdministrativeDepartment(deptDTO);
        dto.setAuthor(contract.getAuthor());
        dto.setAmount(contract.getAmount());
        dto.setAmountCurrentYear(contract.getAmountCurrentYear());
        dto.setAmountWritten(contract.getAmountWritten());
        dto.setApproveDate(contract.getApproveDate() == null ? null : contract.getApproveDate().toString());
        dto.setArchiveDate(contract.getArchiveDate() == null ? null : contract.getArchiveDate().toString());
        dto.setContractIdentifier(contract.getContractIdentifier());
        dto.setContractingMethod(contract.getContractingMethod());
        dto.setCurrency(contract.getCurrency());
        dto.setContractSample(contract.getContractSample());
        dto.setEndDate(contract.getEndDate() == null ? null : contract.getEndDate().toString());
        dto.setExpireDate(contract.getExpireDate() == null ? null : contract.getExpireDate().toString());
        dto.setFundSource(contract.getFundSource());
        dto.setReviewIdentifier(contract.getReviewIdentifier());
        dto.setSignDate(contract.getSignDate() == null ? null : contract.getSignDate().toString());
        dto.setStartDate(contract.getStartDate() == null ? null : contract.getStartDate().toString());
        dto.setSubmitDate(contract.getSubmitDate() == null ? null : contract.getSubmitDate().toString());
        dto.setAssignee(contract.getAssignee());
        dto.setModifiedBy(contract.getModifiedBy());

        if(contract.getNextProcess() != null){
            dto.setNextProcess(contract.getNextProcess());
        }

        if(contract.getStatus().getName().equals("CONTRACT_DRAFT")){
            dto.setIsDraft(true);
        } else {
            dto.setIsDraft(false);
        }
        return dto;
    }

    private Contract mapFromDTO(ContractDTO dto){
        Contract contract = new Contract();
        contract.setName(dto.getName());
        contract.setDescription(dto.getDescription());
        contract.setAmount(dto.getAmount());
        contract.setAmountCurrentYear(dto.getAmountCurrentYear());
        contract.setAmountWritten(dto.getAmountWritten());
        contract.setContractingMethod(dto.getContractingMethod());
        contract.setCurrency(dto.getCurrency());
        contract.setEndDate(new DateTime(dto.getEndDate()));
        contract.setExpireDate(new DateTime(dto.getExpireDate()));
        contract.setFundSource(dto.getFundSource());
        contract.setIsMultiYear(dto.getIsMultiYear());
        contract.setStartDate(new DateTime(dto.getStartDate()));

        if(dto.getNextProcess() != null){
            contract.setNextProcess(dto.getNextProcess());
        }

        for(ContractPartyDTO cpDto : dto.getContractParties()){
            if(cpDto.getId() != null){
                ContractParty cp = contractPartyRepository.findOne(cpDto.getId());
                if(cp != null){
                    contract.getContractParties().add(cp);
                }
            }
        }

        for(DepartmentDTO deptDto : dto.getRelatedDepartments()){
            if(deptDto.getId() != null){
                Department dept = departmentRepository.findOne(deptDto.getId());
                if(dept != null){
                    contract.getRelatedDepartments().add(dept);
                }
            }
        }

        for(ProjectDTO pDto : dto.getProjects()){
            if(pDto.getId() != null){
                Project p = projectRepository.findOne(pDto.getId());
                if(p != null){
                    contract.getProjects().add(p);
                }
            }
        }

        if(dto.getCategory() != null){
            Category cat = categoryRepository.findOne(dto.getCategory().getId());
            if(cat != null){
                contract.setCategory(cat);
            }
        }


        return contract;
    }
}
