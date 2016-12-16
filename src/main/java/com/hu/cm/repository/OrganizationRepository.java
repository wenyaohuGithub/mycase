package com.hu.cm.repository;

import com.hu.cm.domain.Organization;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Organization entity.
 */
public interface OrganizationRepository extends JpaRepository<Organization,Long> {

}
