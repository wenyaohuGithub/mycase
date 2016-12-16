package com.hu.cm.web.rest;

import com.hu.cm.Application;
import com.hu.cm.domain.Route_condition;
import com.hu.cm.repository.Route_conditionRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the Route_conditionResource REST controller.
 *
 * @see Route_conditionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class Route_conditionResourceTest {


    private static final Boolean DEFAULT_PREVIOUS_STEP_RESULT = false;
    private static final Boolean UPDATED_PREVIOUS_STEP_RESULT = true;

    @Inject
    private Route_conditionRepository route_conditionRepository;

    private MockMvc restRoute_conditionMockMvc;

    private Route_condition route_condition;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Route_conditionResource route_conditionResource = new Route_conditionResource();
        ReflectionTestUtils.setField(route_conditionResource, "route_conditionRepository", route_conditionRepository);
        this.restRoute_conditionMockMvc = MockMvcBuilders.standaloneSetup(route_conditionResource).build();
    }

    @Before
    public void initTest() {
        route_condition = new Route_condition();
        route_condition.setPrevious_step_result(DEFAULT_PREVIOUS_STEP_RESULT);
    }

    @Test
    @Transactional
    public void createRoute_condition() throws Exception {
        int databaseSizeBeforeCreate = route_conditionRepository.findAll().size();

        // Create the Route_condition
        restRoute_conditionMockMvc.perform(post("/api/route_conditions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(route_condition)))
                .andExpect(status().isCreated());

        // Validate the Route_condition in the database
        List<Route_condition> route_conditions = route_conditionRepository.findAll();
        assertThat(route_conditions).hasSize(databaseSizeBeforeCreate + 1);
        Route_condition testRoute_condition = route_conditions.get(route_conditions.size() - 1);
        assertThat(testRoute_condition.getPrevious_step_result()).isEqualTo(DEFAULT_PREVIOUS_STEP_RESULT);
    }

    @Test
    @Transactional
    public void getAllRoute_conditions() throws Exception {
        // Initialize the database
        route_conditionRepository.saveAndFlush(route_condition);

        // Get all the route_conditions
        restRoute_conditionMockMvc.perform(get("/api/route_conditions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(route_condition.getId().intValue())))
                .andExpect(jsonPath("$.[*].previous_step_result").value(hasItem(DEFAULT_PREVIOUS_STEP_RESULT.booleanValue())));
    }

    @Test
    @Transactional
    public void getRoute_condition() throws Exception {
        // Initialize the database
        route_conditionRepository.saveAndFlush(route_condition);

        // Get the route_condition
        restRoute_conditionMockMvc.perform(get("/api/route_conditions/{id}", route_condition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(route_condition.getId().intValue()))
            .andExpect(jsonPath("$.previous_step_result").value(DEFAULT_PREVIOUS_STEP_RESULT.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRoute_condition() throws Exception {
        // Get the route_condition
        restRoute_conditionMockMvc.perform(get("/api/route_conditions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoute_condition() throws Exception {
        // Initialize the database
        route_conditionRepository.saveAndFlush(route_condition);

		int databaseSizeBeforeUpdate = route_conditionRepository.findAll().size();

        // Update the route_condition
        route_condition.setPrevious_step_result(UPDATED_PREVIOUS_STEP_RESULT);
        restRoute_conditionMockMvc.perform(put("/api/route_conditions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(route_condition)))
                .andExpect(status().isOk());

        // Validate the Route_condition in the database
        List<Route_condition> route_conditions = route_conditionRepository.findAll();
        assertThat(route_conditions).hasSize(databaseSizeBeforeUpdate);
        Route_condition testRoute_condition = route_conditions.get(route_conditions.size() - 1);
        assertThat(testRoute_condition.getPrevious_step_result()).isEqualTo(UPDATED_PREVIOUS_STEP_RESULT);
    }

    @Test
    @Transactional
    public void deleteRoute_condition() throws Exception {
        // Initialize the database
        route_conditionRepository.saveAndFlush(route_condition);

		int databaseSizeBeforeDelete = route_conditionRepository.findAll().size();

        // Get the route_condition
        restRoute_conditionMockMvc.perform(delete("/api/route_conditions/{id}", route_condition.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Route_condition> route_conditions = route_conditionRepository.findAll();
        assertThat(route_conditions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
