package com.hu.cm.repository;

import com.hu.cm.domain.TestEntity;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TestEntity entity.
 */
public interface TestEntityRepository extends JpaRepository<TestEntity,Long> {

}
