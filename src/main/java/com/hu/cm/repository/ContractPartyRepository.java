package com.hu.cm.repository;

import com.hu.cm.domain.ContractParty;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the ContractParty entity.
 */
public interface ContractPartyRepository extends JpaRepository<ContractParty,Long> {

}
