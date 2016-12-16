package com.hu.cm.service;

import com.hu.cm.domain.Contract;
import com.hu.cm.domain.ContractHistory;
import com.hu.cm.domain.WorkflowProcess;
import com.hu.cm.domain.admin.Role;
import com.hu.cm.domain.admin.User;
import com.hu.cm.domain.configuration.Process;
import com.hu.cm.domain.enumeration.UserAction;
import com.hu.cm.repository.ContractHistoryRepository;
import com.hu.cm.repository.ContractRepository;
import com.hu.cm.repository.WorkflowProcessRepository;
import com.hu.cm.repository.WorkflowRepository;
import com.hu.cm.repository.admin.DepartmentRepository;
import com.hu.cm.repository.admin.UserRepository;
import com.hu.cm.repository.configuration.ProcessRepository;
import com.hu.cm.security.SecurityUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.*;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class ContractService {

    private final Logger log = LoggerFactory.getLogger(ContractService.class);
    @Inject
    private ProcessRepository processRepository;

    @Inject
    private ContractRepository contractRepository;

    @Inject
    private ProcessService processService;

    @Inject
    private WorkflowRepository workflowRepository;

    @Inject
    private WorkflowProcessRepository wpRepository;

    @Inject
    private DepartmentRepository departmentRepository;

    @Inject
    private ContractHistoryRepository contractHistoryRepository;

    @Inject
    private UserRepository userRepository;

    public Contract createContract(Contract contract){
        User currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get();

        contract.setStatus(processRepository.findOneByName("CONTRACT_DRAFT"));
        contract.setAdministrator(currentUser);
        contract.setAuthor(currentUser);
        if(currentUser.getDepartmentId() != null){
            contract.setAdministrativeDepartment(departmentRepository.findOne(currentUser.getDepartmentId()));
        }
        contract.setSubmitDate(new DateTime());
        Contract result = contractRepository.save(contract);

        result.setContractIdentifier("CONTRACT-"+ Calendar.YEAR+"-"+ Calendar.MONTH+1+"-"+result.getId());
        result.setReviewIdentifier("REVIEW-"+Calendar.YEAR+"-"+ Calendar.MONTH+1+"-"+result.getId());

        result = contractRepository.save(result);

        ContractHistory history = new ContractHistory();
        history.setContract(result);
        history.setUser(currentUser);
        history.setProcess(result.getStatus());
        history.setAction(UserAction.CREATE);
        history.setAction_datetime(new DateTime());
        contractHistoryRepository.save(history);

        return result;
    }

    public Contract approveContract(Contract contract){
        Process currentProcess = contract.getStatus();
        contract.setStatus(processService.getNextProcess(contract.getCategory().getWorkflow().getId(), currentProcess.getId()));


        /* TODO
        *
        *  need to find the user who is responsible for next process, and set it as assignee
        *  send message to assignee
        *  add task for assignee
        * */

        contract.setModifiedDate(new DateTime());
        contract.setModifiedBy(userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get());
        Contract result = contractRepository.save(contract);

        ContractHistory history = new ContractHistory();
        history.setContract(result);
        history.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get());
        history.setProcess(currentProcess);
        history.setAction(UserAction.APPROVE);

        history.setAction_datetime(new DateTime());
        contractHistoryRepository.save(history);

        return result;
    }

    public Contract rejectContract(Contract contract){
        Process currentProcess = contract.getStatus();

        contract.setStatus(processRepository.findOneByName("CONTRACT_DRAFT"));
        contract.setAssignee(contract.getAuthor());
        contract.setModifiedDate(new DateTime());
        contract.setModifiedBy(userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get());

        /* TODO
        *
        *  need to find the user who is responsible for next process, and set it as assignee
        *  send message to assignee
        *  add task for assignee
        * */

        Contract result = contractRepository.save(contract);

        ContractHistory history = new ContractHistory();
        history.setContract(result);
        history.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get());
        history.setProcess(currentProcess);
        history.setAction(UserAction.REJECT);
        history.setAction_datetime(new DateTime());
        contractHistoryRepository.save(history);

        return result;
    }

    public Contract submitForReview(Contract contract){
        Process currentProcess = contract.getStatus();
        if(currentProcess.getName().equals("CONTRACT_DRAFT") && contract.getNextProcess() != null){
            contract.setStatus(contract.getNextProcess());
        } else {
            contract.setStatus(processService.getNextProcess(contract.getCategory().getWorkflow().getId(), currentProcess.getId()));
        }
        contract.setModifiedDate(new DateTime());
        contract.setModifiedBy(userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get());

        /* TODO
        *
        *  need to find the user who is responsible for next process, and set it as assignee
        *  send message to assignee
        *  add task for assignee
        * */

        Contract result = contractRepository.save(contract);

        ContractHistory history = new ContractHistory();
        history.setContract(result);
        history.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get());
        history.setProcess(currentProcess);
        history.setAction(UserAction.SUBMIT);
        history.setAction_datetime(new DateTime());
        contractHistoryRepository.save(history);

        return result;
    }

    public ContractHistory addComment(Contract contract, ContractHistory contractHistory){
        contractHistory.setAction(UserAction.COMMENT);
        contractHistory.setAction_datetime(new DateTime());
        contractHistory.setContract(contract);
        contractHistory.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get());
        contractHistory.setProcess(contract.getStatus());

        ContractHistory result = contractHistoryRepository.save(contractHistory);
        return result;
    }
}
