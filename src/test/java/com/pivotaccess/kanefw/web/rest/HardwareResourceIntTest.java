package com.pivotaccess.kanefw.web.rest;

import com.pivotaccess.kanefw.KanefwApp;

import com.pivotaccess.kanefw.domain.Hardware;
import com.pivotaccess.kanefw.repository.HardwareRepository;
import com.pivotaccess.kanefw.repository.search.HardwareSearchRepository;
import com.pivotaccess.kanefw.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.pivotaccess.kanefw.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the HardwareResource REST controller.
 *
 * @see HardwareResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KanefwApp.class)
public class HardwareResourceIntTest {

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final String DEFAULT_SERIE = "AAAAAAAAAA";
    private static final String UPDATED_SERIE = "BBBBBBBBBB";

    @Autowired
    private HardwareRepository hardwareRepository;

    /**
     * This repository is mocked in the com.pivotaccess.kanefw.repository.search test package.
     *
     * @see com.pivotaccess.kanefw.repository.search.HardwareSearchRepositoryMockConfiguration
     */
    @Autowired
    private HardwareSearchRepository mockHardwareSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restHardwareMockMvc;

    private Hardware hardware;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HardwareResource hardwareResource = new HardwareResource(hardwareRepository, mockHardwareSearchRepository);
        this.restHardwareMockMvc = MockMvcBuilders.standaloneSetup(hardwareResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hardware createEntity(EntityManager em) {
        Hardware hardware = new Hardware()
            .model(DEFAULT_MODEL)
            .serie(DEFAULT_SERIE);
        return hardware;
    }

    @Before
    public void initTest() {
        hardware = createEntity(em);
    }

    @Test
    @Transactional
    public void createHardware() throws Exception {
        int databaseSizeBeforeCreate = hardwareRepository.findAll().size();

        // Create the Hardware
        restHardwareMockMvc.perform(post("/api/hardwares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hardware)))
            .andExpect(status().isCreated());

        // Validate the Hardware in the database
        List<Hardware> hardwareList = hardwareRepository.findAll();
        assertThat(hardwareList).hasSize(databaseSizeBeforeCreate + 1);
        Hardware testHardware = hardwareList.get(hardwareList.size() - 1);
        assertThat(testHardware.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testHardware.getSerie()).isEqualTo(DEFAULT_SERIE);

        // Validate the Hardware in Elasticsearch
        verify(mockHardwareSearchRepository, times(1)).save(testHardware);
    }

    @Test
    @Transactional
    public void createHardwareWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hardwareRepository.findAll().size();

        // Create the Hardware with an existing ID
        hardware.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHardwareMockMvc.perform(post("/api/hardwares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hardware)))
            .andExpect(status().isBadRequest());

        // Validate the Hardware in the database
        List<Hardware> hardwareList = hardwareRepository.findAll();
        assertThat(hardwareList).hasSize(databaseSizeBeforeCreate);

        // Validate the Hardware in Elasticsearch
        verify(mockHardwareSearchRepository, times(0)).save(hardware);
    }

    @Test
    @Transactional
    public void checkModelIsRequired() throws Exception {
        int databaseSizeBeforeTest = hardwareRepository.findAll().size();
        // set the field null
        hardware.setModel(null);

        // Create the Hardware, which fails.

        restHardwareMockMvc.perform(post("/api/hardwares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hardware)))
            .andExpect(status().isBadRequest());

        List<Hardware> hardwareList = hardwareRepository.findAll();
        assertThat(hardwareList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHardwares() throws Exception {
        // Initialize the database
        hardwareRepository.saveAndFlush(hardware);

        // Get all the hardwareList
        restHardwareMockMvc.perform(get("/api/hardwares?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hardware.getId().intValue())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL.toString())))
            .andExpect(jsonPath("$.[*].serie").value(hasItem(DEFAULT_SERIE.toString())));
    }
    
    @Test
    @Transactional
    public void getHardware() throws Exception {
        // Initialize the database
        hardwareRepository.saveAndFlush(hardware);

        // Get the hardware
        restHardwareMockMvc.perform(get("/api/hardwares/{id}", hardware.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hardware.getId().intValue()))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL.toString()))
            .andExpect(jsonPath("$.serie").value(DEFAULT_SERIE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHardware() throws Exception {
        // Get the hardware
        restHardwareMockMvc.perform(get("/api/hardwares/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHardware() throws Exception {
        // Initialize the database
        hardwareRepository.saveAndFlush(hardware);

        int databaseSizeBeforeUpdate = hardwareRepository.findAll().size();

        // Update the hardware
        Hardware updatedHardware = hardwareRepository.findById(hardware.getId()).get();
        // Disconnect from session so that the updates on updatedHardware are not directly saved in db
        em.detach(updatedHardware);
        updatedHardware
            .model(UPDATED_MODEL)
            .serie(UPDATED_SERIE);

        restHardwareMockMvc.perform(put("/api/hardwares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHardware)))
            .andExpect(status().isOk());

        // Validate the Hardware in the database
        List<Hardware> hardwareList = hardwareRepository.findAll();
        assertThat(hardwareList).hasSize(databaseSizeBeforeUpdate);
        Hardware testHardware = hardwareList.get(hardwareList.size() - 1);
        assertThat(testHardware.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testHardware.getSerie()).isEqualTo(UPDATED_SERIE);

        // Validate the Hardware in Elasticsearch
        verify(mockHardwareSearchRepository, times(1)).save(testHardware);
    }

    @Test
    @Transactional
    public void updateNonExistingHardware() throws Exception {
        int databaseSizeBeforeUpdate = hardwareRepository.findAll().size();

        // Create the Hardware

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHardwareMockMvc.perform(put("/api/hardwares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hardware)))
            .andExpect(status().isBadRequest());

        // Validate the Hardware in the database
        List<Hardware> hardwareList = hardwareRepository.findAll();
        assertThat(hardwareList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Hardware in Elasticsearch
        verify(mockHardwareSearchRepository, times(0)).save(hardware);
    }

    @Test
    @Transactional
    public void deleteHardware() throws Exception {
        // Initialize the database
        hardwareRepository.saveAndFlush(hardware);

        int databaseSizeBeforeDelete = hardwareRepository.findAll().size();

        // Delete the hardware
        restHardwareMockMvc.perform(delete("/api/hardwares/{id}", hardware.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Hardware> hardwareList = hardwareRepository.findAll();
        assertThat(hardwareList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Hardware in Elasticsearch
        verify(mockHardwareSearchRepository, times(1)).deleteById(hardware.getId());
    }

    @Test
    @Transactional
    public void searchHardware() throws Exception {
        // Initialize the database
        hardwareRepository.saveAndFlush(hardware);
        when(mockHardwareSearchRepository.search(queryStringQuery("id:" + hardware.getId())))
            .thenReturn(Collections.singletonList(hardware));
        // Search the hardware
        restHardwareMockMvc.perform(get("/api/_search/hardwares?query=id:" + hardware.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hardware.getId().intValue())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].serie").value(hasItem(DEFAULT_SERIE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hardware.class);
        Hardware hardware1 = new Hardware();
        hardware1.setId(1L);
        Hardware hardware2 = new Hardware();
        hardware2.setId(hardware1.getId());
        assertThat(hardware1).isEqualTo(hardware2);
        hardware2.setId(2L);
        assertThat(hardware1).isNotEqualTo(hardware2);
        hardware1.setId(null);
        assertThat(hardware1).isNotEqualTo(hardware2);
    }
}
