package com.hu.cm.repository.admin;

import com.hu.cm.domain.admin.Department;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Department entity.
 */
public interface DepartmentRepository extends JpaRepository<Department,Long> {

}
