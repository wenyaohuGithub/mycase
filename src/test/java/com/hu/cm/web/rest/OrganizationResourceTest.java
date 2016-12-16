package com.hu.cm.web.rest;

import com.hu.cm.Application;
import com.hu.cm.domain.Organization;
import com.hu.cm.repository.OrganizationRepository;

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
 * Test class for the OrganizationResource REST controller.
 *
 * @see OrganizationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OrganizationResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Integer DEFAULT_SESSION_TIMEOUT = 0;
    private static final Integer UPDATED_SESSION_TIMEOUT = 1;

    private static final Boolean DEFAULT_SMTP_ENABLED = false;
    private static final Boolean UPDATED_SMTP_ENABLED = true;
    private static final String DEFAULT_SMTP_SERVER_ADDRESS = "SAMPLE_TEXT";
    private static final String UPDATED_SMTP_SERVER_ADDRESS = "UPDATED_TEXT";
    private static final String DEFAULT_SMTP_USERNAME = "SAMPLE_TEXT";
    private static final String UPDATED_SMTP_USERNAME = "UPDATED_TEXT";
    private static final String DEFAULT_SMTP_PASSWORD = "SAMPLE_TEXT";
    private static final String UPDATED_SMTP_PASSWORD = "UPDATED_TEXT";

    private static final Integer DEFAULT_SMTP_PORT = 0;
    private static final Integer UPDATED_SMTP_PORT = 1;

    @Inject
    private OrganizationRepository organizationRepository;

    private MockMvc restOrganizationMockMvc;

    private Organization organization;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrganizationResource organizationResource = new OrganizationResource();
        ReflectionTestUtils.setField(organizationResource, "organizationRepository", organizationRepository);
        this.restOrganizationMockMvc = MockMvcBuilders.standaloneSetup(organizationResource).build();
    }

    @Before
    public void initTest() {
        organization = new Organization();
        organization.setName(DEFAULT_NAME);
        organization.setDescription(DEFAULT_DESCRIPTION);
        organization.setActive(DEFAULT_ACTIVE);
        organization.setSession_timeout(DEFAULT_SESSION_TIMEOUT);
        organization.setSmtp_enabled(DEFAULT_SMTP_ENABLED);
        organization.setSmtp_server_address(DEFAULT_SMTP_SERVER_ADDRESS);
        organization.setSmtp_username(DEFAULT_SMTP_USERNAME);
        organization.setSmtp_password(DEFAULT_SMTP_PASSWORD);
        organization.setSmtp_port(DEFAULT_SMTP_PORT);
    }

    @Test
    @Transactional
    public void createOrganization() throws Exception {
        int databaseSizeBeforeCreate = organizationRepository.findAll().size();

        // Create the Organization
        restOrganizationMockMvc.perform(post("/api/organizations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(organization)))
                .andExpect(status().isCreated());

        // Validate the Organization in the database
        List<Organization> organizations = organizationRepository.findAll();
        assertThat(organizations).hasSize(databaseSizeBeforeCreate + 1);
        Organization testOrganization = organizations.get(organizations.size() - 1);
        assertThat(testOrganization.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrganization.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOrganization.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testOrganization.getSession_timeout()).isEqualTo(DEFAULT_SESSION_TIMEOUT);
        assertThat(testOrganization.getSmtp_enabled()).isEqualTo(DEFAULT_SMTP_ENABLED);
        assertThat(testOrganization.getSmtp_server_address()).isEqualTo(DEFAULT_SMTP_SERVER_ADDRESS);
        assertThat(testOrganization.getSmtp_username()).isEqualTo(DEFAULT_SMTP_USERNAME);
        assertThat(testOrganization.getSmtp_password()).isEqualTo(DEFAULT_SMTP_PASSWORD);
        assertThat(testOrganization.getSmtp_port()).isEqualTo(DEFAULT_SMTP_PORT);
    }

    @Test
    @Transactional
    public void getAllOrganizations() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizations
        restOrganizationMockMvc.perform(get("/api/organizations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(organization.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
                .andExpect(jsonPath("$.[*].session_timeout").value(hasItem(DEFAULT_SESSION_TIMEOUT)))
                .andExpect(jsonPath("$.[*].smtp_enabled").value(hasItem(DEFAULT_SMTP_ENABLED.booleanValue())))
                .andExpect(jsonPath("$.[*].smtp_server_address").value(hasItem(DEFAULT_SMTP_SERVER_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].smtp_username").value(hasItem(DEFAULT_SMTP_USERNAME.toString())))
                .andExpect(jsonPath("$.[*].smtp_password").value(hasItem(DEFAULT_SMTP_PASSWORD.toString())))
                .andExpect(jsonPath("$.[*].smtp_port").value(hasItem(DEFAULT_SMTP_PORT)));
    }

    @Test
    @Transactional
    public void getOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get the organization
        restOrganizationMockMvc.perform(get("/api/organizations/{id}", organization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(organization.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.session_timeout").value(DEFAULT_SESSION_TIMEOUT))
            .andExpect(jsonPath("$.smtp_enabled").value(DEFAULT_SMTP_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.smtp_server_address").value(DEFAULT_SMTP_SERVER_ADDRESS.toString()))
            .andExpect(jsonPath("$.smtp_username").value(DEFAULT_SMTP_USERNAME.toString()))
            .andExpect(jsonPath("$.smtp_password").value(DEFAULT_SMTP_PASSWORD.toString()))
            .andExpect(jsonPath("$.smtp_port").value(DEFAULT_SMTP_PORT));
    }

    @Test
    @Transactional
    public void getNonExistingOrganization() throws Exception {
        // Get the organization
        restOrganizationMockMvc.perform(get("/api/organizations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

		int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

        // Update the organization
        organization.setName(UPDATED_NAME);
        organization.setDescription(UPDATED_DESCRIPTION);
        organization.setActive(UPDATED_ACTIVE);
        organization.setSession_timeout(UPDATED_SESSION_TIMEOUT);
        organization.setSmtp_enabled(UPDATED_SMTP_ENABLED);
        organization.setSmtp_server_address(UPDATED_SMTP_SERVER_ADDRESS);
        organization.setSmtp_username(UPDATED_SMTP_USERNAME);
        organization.setSmtp_password(UPDATED_SMTP_PASSWORD);
        organization.setSmtp_port(UPDATED_SMTP_PORT);
        restOrganizationMockMvc.perform(put("/api/organizations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(organization)))
                .andExpect(status().isOk());

        // Validate the Organization in the database
        List<Organization> organizations = organizationRepository.findAll();
        assertThat(organizations).hasSize(databaseSizeBeforeUpdate);
        Organization testOrganization = organizations.get(organizations.size() - 1);
        assertThat(testOrganization.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganization.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrganization.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testOrganization.getSession_timeout()).isEqualTo(UPDATED_SESSION_TIMEOUT);
        assertThat(testOrganization.getSmtp_enabled()).isEqualTo(UPDATED_SMTP_ENABLED);
        assertThat(testOrganization.getSmtp_server_address()).isEqualTo(UPDATED_SMTP_SERVER_ADDRESS);
        assertThat(testOrganization.getSmtp_username()).isEqualTo(UPDATED_SMTP_USERNAME);
        assertThat(testOrganization.getSmtp_password()).isEqualTo(UPDATED_SMTP_PASSWORD);
        assertThat(testOrganization.getSmtp_port()).isEqualTo(UPDATED_SMTP_PORT);
    }

    @Test
    @Transactional
    public void deleteOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

		int databaseSizeBeforeDelete = organizationRepository.findAll().size();

        // Get the organization
        restOrganizationMockMvc.perform(delete("/api/organizations/{id}", organization.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Organization> organizations = organizationRepository.findAll();
        assertThat(organizations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
