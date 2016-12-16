package com.hu.cm.web.rest;

import com.hu.cm.Application;
import com.hu.cm.domain.FirstEntity;
import com.hu.cm.repository.FirstEntityRepository;

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
 * Test class for the FirstEntityResource REST controller.
 *
 * @see FirstEntityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FirstEntityResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private FirstEntityRepository firstEntityRepository;

    private MockMvc restFirstEntityMockMvc;

    private FirstEntity firstEntity;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FirstEntityResource firstEntityResource = new FirstEntityResource();
        ReflectionTestUtils.setField(firstEntityResource, "firstEntityRepository", firstEntityRepository);
        this.restFirstEntityMockMvc = MockMvcBuilders.standaloneSetup(firstEntityResource).build();
    }

    @Before
    public void initTest() {
        firstEntity = new FirstEntity();
        firstEntity.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createFirstEntity() throws Exception {
        int databaseSizeBeforeCreate = firstEntityRepository.findAll().size();

        // Create the FirstEntity
        restFirstEntityMockMvc.perform(post("/api/firstEntitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(firstEntity)))
                .andExpect(status().isCreated());

        // Validate the FirstEntity in the database
        List<FirstEntity> firstEntitys = firstEntityRepository.findAll();
        assertThat(firstEntitys).hasSize(databaseSizeBeforeCreate + 1);
        FirstEntity testFirstEntity = firstEntitys.get(firstEntitys.size() - 1);
        assertThat(testFirstEntity.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllFirstEntitys() throws Exception {
        // Initialize the database
        firstEntityRepository.saveAndFlush(firstEntity);

        // Get all the firstEntitys
        restFirstEntityMockMvc.perform(get("/api/firstEntitys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(firstEntity.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getFirstEntity() throws Exception {
        // Initialize the database
        firstEntityRepository.saveAndFlush(firstEntity);

        // Get the firstEntity
        restFirstEntityMockMvc.perform(get("/api/firstEntitys/{id}", firstEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(firstEntity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFirstEntity() throws Exception {
        // Get the firstEntity
        restFirstEntityMockMvc.perform(get("/api/firstEntitys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFirstEntity() throws Exception {
        // Initialize the database
        firstEntityRepository.saveAndFlush(firstEntity);

		int databaseSizeBeforeUpdate = firstEntityRepository.findAll().size();

        // Update the firstEntity
        firstEntity.setName(UPDATED_NAME);
        restFirstEntityMockMvc.perform(put("/api/firstEntitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(firstEntity)))
                .andExpect(status().isOk());

        // Validate the FirstEntity in the database
        List<FirstEntity> firstEntitys = firstEntityRepository.findAll();
        assertThat(firstEntitys).hasSize(databaseSizeBeforeUpdate);
        FirstEntity testFirstEntity = firstEntitys.get(firstEntitys.size() - 1);
        assertThat(testFirstEntity.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteFirstEntity() throws Exception {
        // Initialize the database
        firstEntityRepository.saveAndFlush(firstEntity);

		int databaseSizeBeforeDelete = firstEntityRepository.findAll().size();

        // Get the firstEntity
        restFirstEntityMockMvc.perform(delete("/api/firstEntitys/{id}", firstEntity.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FirstEntity> firstEntitys = firstEntityRepository.findAll();
        assertThat(firstEntitys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
