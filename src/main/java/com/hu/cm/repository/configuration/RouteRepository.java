package com.hu.cm.repository.configuration;

import com.hu.cm.domain.configuration.Route;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Route entity.
 */
public interface RouteRepository extends JpaRepository<Route,Long> {

}
