package com.hu.cm.repository;

import com.hu.cm.domain.Contract;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Contract entity.
 */
public interface ContractRepository extends JpaRepository<Contract,Long> {
    @Query("SELECT c FROM Contract c LEFT JOIN FETCH c.relatedDepartments LEFT JOIN FETCH c.contractParties " +
        "LEFT JOIN FETCH c.category LEFT JOIN FETCH c.projects WHERE c.id = (:id)")
    public Contract findByIdAndFetchEager(@Param("id") Long id);
}
