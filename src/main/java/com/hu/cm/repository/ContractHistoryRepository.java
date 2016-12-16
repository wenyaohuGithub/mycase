package com.hu.cm.repository;

import com.hu.cm.domain.ContractHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the ContractHistory entity.
 */
public interface ContractHistoryRepository extends JpaRepository<ContractHistory,Long> {

    @Query("select contract_history from ContractHistory contract_history where contract_history.user.login = ?#{principal.username}")
    List<ContractHistory> findAllForCurrentUser();

    @Query("select contract_history from ContractHistory contract_history where contract_history.contract.id = (:contractId)")
    List<ContractHistory> findAllForContract(@Param("contractId") Long contractId);

}
