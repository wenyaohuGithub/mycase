package com.hu.cm.web.rest.mapper;

import com.hu.cm.domain.Contract;
import com.hu.cm.domain.ContractHistory;
import com.hu.cm.domain.configuration.Process;
import com.hu.cm.domain.admin.User;
import com.hu.cm.web.rest.dto.ContractHistoryDTO;

import org.mapstruct.*;


/**
 * Mapper for the entity ContractHistory and its DTO ContractHistoryDTO.
 */

@Mapper(componentModel = "spring", uses = {})
public interface ContractHistoryMapper {

    @Mapping(source = "contract.id", target = "contractId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "process.id", target = "processId")
    ContractHistoryDTO contract_historyToContract_historyDTO(ContractHistory contract_history);

    @Mapping(source = "contractId", target = "contract")
    @Mapping(source = "userId", target = "user")
    @Mapping(source = "processId", target = "process")
    ContractHistory contract_historyDTOToContract_history(ContractHistoryDTO contract_historyDTO);

    default Contract contractFromId(Long id) {
        if (id == null) {
            return null;
        }
        Contract contract = new Contract();
        contract.setId(id);
        return contract;
    }

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }

    default Process processFromId(Long id) {
        if (id == null) {
            return null;
        }
        Process process = new Process();
        process.setId(id);
        return process;
    }
}
