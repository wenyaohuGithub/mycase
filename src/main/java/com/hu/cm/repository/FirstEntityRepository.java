package com.hu.cm.repository;

import com.hu.cm.domain.FirstEntity;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the FirstEntity entity.
 */
public interface FirstEntityRepository extends JpaRepository<FirstEntity,Long> {

}
