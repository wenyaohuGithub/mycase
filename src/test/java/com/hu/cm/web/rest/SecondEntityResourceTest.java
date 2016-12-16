package com.hu.cm.web.rest;

import com.hu.cm.Application;
import com.hu.cm.domain.SecondEntity;
import com.hu.cm.repository.SecondEntityRepository;

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
 * Test class for the SecondEntityResource REST controller.
 *
 * @see SecondEntityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SecondEntityResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private SecondEntityRepository secondEntityRepository;

    private MockMvc restSecondEntityMockMvc;

    private SecondEntity secondEntity;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SecondEntityResource secondEntityResource = new SecondEntityResource();
        ReflectionTestUtils.setField(secondEntityResource, "secondEntityRepository", secondEntityRepository);
        this.restSecondEntityMockMvc = MockMvcBuilders.standaloneSetup(secondEntityResource).build();
    }

    @Before
    public void initTest() {
        secondEntity = new SecondEntity();
        secondEntity.setName(DEFAULT_NAME);
        secondEntity.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createSecondEntity() throws Exception {
        int databaseSizeBeforeCreate = secondEntityRepository.findAll().size();

        // Create the SecondEntity
        restSecondEntityMockMvc.perform(post("/api/secondEntitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(secondEntity)))
                .andExpect(status().isCreated());

        // Validate the SecondEntity in the database
        List<SecondEntity> secondEntitys = secondEntityRepository.findAll();
        assertThat(secondEntitys).hasSize(databaseSizeBeforeCreate + 1);
        SecondEntity testSecondEntity = secondEntitys.get(secondEntitys.size() - 1);
        assertThat(testSecondEntity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSecondEntity.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSecondEntitys() throws Exception {
        // Initialize the database
        secondEntityRepository.saveAndFlush(secondEntity);

        // Get all the secondEntitys
        restSecondEntityMockMvc.perform(get("/api/secondEntitys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(secondEntity.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getSecondEntity() throws Exception {
        // Initialize the database
        secondEntityRepository.saveAndFlush(secondEntity);

        // Get the secondEntity
        restSecondEntityMockMvc.perform(get("/api/secondEntitys/{id}", secondEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(secondEntity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSecondEntity() throws Exception {
        // Get the secondEntity
        restSecondEntityMockMvc.perform(get("/api/secondEntitys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSecondEntity() throws Exception {
        // Initialize the database
        secondEntityRepository.saveAndFlush(secondEntity);

		int databaseSizeBeforeUpdate = secondEntityRepository.findAll().size();

        // Update the secondEntity
        secondEntity.setName(UPDATED_NAME);
        secondEntity.setDescription(UPDATED_DESCRIPTION);
        restSecondEntityMockMvc.perform(put("/api/secondEntitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(secondEntity)))
                .andExpect(status().isOk());

        // Validate the SecondEntity in the database
        List<SecondEntity> secondEntitys = secondEntityRepository.findAll();
        assertThat(secondEntitys).hasSize(databaseSizeBeforeUpdate);
        SecondEntity testSecondEntity = secondEntitys.get(secondEntitys.size() - 1);
        assertThat(testSecondEntity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSecondEntity.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteSecondEntity() throws Exception {
        // Initialize the database
        secondEntityRepository.saveAndFlush(secondEntity);

		int databaseSizeBeforeDelete = secondEntityRepository.findAll().size();

        // Get the secondEntity
        restSecondEntityMockMvc.perform(delete("/api/secondEntitys/{id}", secondEntity.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SecondEntity> secondEntitys = secondEntityRepository.findAll();
        assertThat(secondEntitys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
