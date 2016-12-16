package com.hu.cm.web.rest;

import com.hu.cm.Application;
import com.hu.cm.domain.TestEntity;
import com.hu.cm.repository.TestEntityRepository;

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
import org.joda.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TestEntityResource REST controller.
 *
 * @see TestEntityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TestEntityResourceTest {


    private static final LocalDate DEFAULT_TEST_FIELD = new LocalDate(0L);
    private static final LocalDate UPDATED_TEST_FIELD = new LocalDate();

    @Inject
    private TestEntityRepository testEntityRepository;

    private MockMvc restTestEntityMockMvc;

    private TestEntity testEntity;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TestEntityResource testEntityResource = new TestEntityResource();
        ReflectionTestUtils.setField(testEntityResource, "testEntityRepository", testEntityRepository);
        this.restTestEntityMockMvc = MockMvcBuilders.standaloneSetup(testEntityResource).build();
    }

    @Before
    public void initTest() {
        testEntity = new TestEntity();
        testEntity.setTestField(DEFAULT_TEST_FIELD);
    }

    @Test
    @Transactional
    public void createTestEntity() throws Exception {
        int databaseSizeBeforeCreate = testEntityRepository.findAll().size();

        // Create the TestEntity
        restTestEntityMockMvc.perform(post("/api/testEntitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(testEntity)))
                .andExpect(status().isCreated());

        // Validate the TestEntity in the database
        List<TestEntity> testEntitys = testEntityRepository.findAll();
        assertThat(testEntitys).hasSize(databaseSizeBeforeCreate + 1);
        TestEntity testTestEntity = testEntitys.get(testEntitys.size() - 1);
        assertThat(testTestEntity.getTestField()).isEqualTo(DEFAULT_TEST_FIELD);
    }

    @Test
    @Transactional
    public void getAllTestEntitys() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get all the testEntitys
        restTestEntityMockMvc.perform(get("/api/testEntitys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(testEntity.getId().intValue())))
                .andExpect(jsonPath("$.[*].testField").value(hasItem(DEFAULT_TEST_FIELD.toString())));
    }

    @Test
    @Transactional
    public void getTestEntity() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

        // Get the testEntity
        restTestEntityMockMvc.perform(get("/api/testEntitys/{id}", testEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(testEntity.getId().intValue()))
            .andExpect(jsonPath("$.testField").value(DEFAULT_TEST_FIELD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTestEntity() throws Exception {
        // Get the testEntity
        restTestEntityMockMvc.perform(get("/api/testEntitys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTestEntity() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

		int databaseSizeBeforeUpdate = testEntityRepository.findAll().size();

        // Update the testEntity
        testEntity.setTestField(UPDATED_TEST_FIELD);
        restTestEntityMockMvc.perform(put("/api/testEntitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(testEntity)))
                .andExpect(status().isOk());

        // Validate the TestEntity in the database
        List<TestEntity> testEntitys = testEntityRepository.findAll();
        assertThat(testEntitys).hasSize(databaseSizeBeforeUpdate);
        TestEntity testTestEntity = testEntitys.get(testEntitys.size() - 1);
        assertThat(testTestEntity.getTestField()).isEqualTo(UPDATED_TEST_FIELD);
    }

    @Test
    @Transactional
    public void deleteTestEntity() throws Exception {
        // Initialize the database
        testEntityRepository.saveAndFlush(testEntity);

		int databaseSizeBeforeDelete = testEntityRepository.findAll().size();

        // Get the testEntity
        restTestEntityMockMvc.perform(delete("/api/testEntitys/{id}", testEntity.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TestEntity> testEntitys = testEntityRepository.findAll();
        assertThat(testEntitys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
