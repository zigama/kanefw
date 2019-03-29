package com.pivotaccess.kanefw.web.rest;

import com.pivotaccess.kanefw.KanefwApp;

import com.pivotaccess.kanefw.domain.HardwareFile;
import com.pivotaccess.kanefw.domain.Hardware;
import com.pivotaccess.kanefw.repository.HardwareFileRepository;
import com.pivotaccess.kanefw.repository.search.HardwareFileSearchRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static com.pivotaccess.kanefw.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pivotaccess.kanefw.domain.enumeration.FileCategory;
/**
 * Test class for the HardwareFileResource REST controller.
 *
 * @see HardwareFileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KanefwApp.class)
public class HardwareFileResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Long DEFAULT_SIZE = 1L;
    private static final Long UPDATED_SIZE = 2L;

    private static final String DEFAULT_MIME_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MIME_TYPE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_UPLOADED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_UPLOADED = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final FileCategory DEFAULT_CATEGORY = FileCategory.FIRMWARE;
    private static final FileCategory UPDATED_CATEGORY = FileCategory.DATASHEET;

    @Autowired
    private HardwareFileRepository hardwareFileRepository;

    /**
     * This repository is mocked in the com.pivotaccess.kanefw.repository.search test package.
     *
     * @see com.pivotaccess.kanefw.repository.search.HardwareFileSearchRepositoryMockConfiguration
     */
    @Autowired
    private HardwareFileSearchRepository mockHardwareFileSearchRepository;

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

    private MockMvc restHardwareFileMockMvc;

    private HardwareFile hardwareFile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HardwareFileResource hardwareFileResource = new HardwareFileResource(hardwareFileRepository, mockHardwareFileSearchRepository);
        this.restHardwareFileMockMvc = MockMvcBuilders.standaloneSetup(hardwareFileResource)
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
    public static HardwareFile createEntity(EntityManager em) {
        HardwareFile hardwareFile = new HardwareFile()
            .title(DEFAULT_TITLE)
            .size(DEFAULT_SIZE)
            .mimeType(DEFAULT_MIME_TYPE)
            .dateUploaded(DEFAULT_DATE_UPLOADED)
            .version(DEFAULT_VERSION)
            .category(DEFAULT_CATEGORY);
        // Add required entity
        Hardware hardware = HardwareResourceIntTest.createEntity(em);
        em.persist(hardware);
        em.flush();
        hardwareFile.setHardware(hardware);
        return hardwareFile;
    }

    @Before
    public void initTest() {
        hardwareFile = createEntity(em);
    }

    @Test
    @Transactional
    public void createHardwareFile() throws Exception {
        int databaseSizeBeforeCreate = hardwareFileRepository.findAll().size();

        // Create the HardwareFile
        restHardwareFileMockMvc.perform(post("/api/hardware-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hardwareFile)))
            .andExpect(status().isCreated());

        // Validate the HardwareFile in the database
        List<HardwareFile> hardwareFileList = hardwareFileRepository.findAll();
        assertThat(hardwareFileList).hasSize(databaseSizeBeforeCreate + 1);
        HardwareFile testHardwareFile = hardwareFileList.get(hardwareFileList.size() - 1);
        assertThat(testHardwareFile.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testHardwareFile.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testHardwareFile.getMimeType()).isEqualTo(DEFAULT_MIME_TYPE);
        assertThat(testHardwareFile.getDateUploaded()).isEqualTo(DEFAULT_DATE_UPLOADED);
        assertThat(testHardwareFile.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testHardwareFile.getCategory()).isEqualTo(DEFAULT_CATEGORY);

        // Validate the HardwareFile in Elasticsearch
        verify(mockHardwareFileSearchRepository, times(1)).save(testHardwareFile);
    }

    @Test
    @Transactional
    public void createHardwareFileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hardwareFileRepository.findAll().size();

        // Create the HardwareFile with an existing ID
        hardwareFile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHardwareFileMockMvc.perform(post("/api/hardware-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hardwareFile)))
            .andExpect(status().isBadRequest());

        // Validate the HardwareFile in the database
        List<HardwareFile> hardwareFileList = hardwareFileRepository.findAll();
        assertThat(hardwareFileList).hasSize(databaseSizeBeforeCreate);

        // Validate the HardwareFile in Elasticsearch
        verify(mockHardwareFileSearchRepository, times(0)).save(hardwareFile);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = hardwareFileRepository.findAll().size();
        // set the field null
        hardwareFile.setTitle(null);

        // Create the HardwareFile, which fails.

        restHardwareFileMockMvc.perform(post("/api/hardware-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hardwareFile)))
            .andExpect(status().isBadRequest());

        List<HardwareFile> hardwareFileList = hardwareFileRepository.findAll();
        assertThat(hardwareFileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSizeIsRequired() throws Exception {
        int databaseSizeBeforeTest = hardwareFileRepository.findAll().size();
        // set the field null
        hardwareFile.setSize(null);

        // Create the HardwareFile, which fails.

        restHardwareFileMockMvc.perform(post("/api/hardware-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hardwareFile)))
            .andExpect(status().isBadRequest());

        List<HardwareFile> hardwareFileList = hardwareFileRepository.findAll();
        assertThat(hardwareFileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateUploadedIsRequired() throws Exception {
        int databaseSizeBeforeTest = hardwareFileRepository.findAll().size();
        // set the field null
        hardwareFile.setDateUploaded(null);

        // Create the HardwareFile, which fails.

        restHardwareFileMockMvc.perform(post("/api/hardware-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hardwareFile)))
            .andExpect(status().isBadRequest());

        List<HardwareFile> hardwareFileList = hardwareFileRepository.findAll();
        assertThat(hardwareFileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = hardwareFileRepository.findAll().size();
        // set the field null
        hardwareFile.setVersion(null);

        // Create the HardwareFile, which fails.

        restHardwareFileMockMvc.perform(post("/api/hardware-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hardwareFile)))
            .andExpect(status().isBadRequest());

        List<HardwareFile> hardwareFileList = hardwareFileRepository.findAll();
        assertThat(hardwareFileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = hardwareFileRepository.findAll().size();
        // set the field null
        hardwareFile.setCategory(null);

        // Create the HardwareFile, which fails.

        restHardwareFileMockMvc.perform(post("/api/hardware-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hardwareFile)))
            .andExpect(status().isBadRequest());

        List<HardwareFile> hardwareFileList = hardwareFileRepository.findAll();
        assertThat(hardwareFileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHardwareFiles() throws Exception {
        // Initialize the database
        hardwareFileRepository.saveAndFlush(hardwareFile);

        // Get all the hardwareFileList
        restHardwareFileMockMvc.perform(get("/api/hardware-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hardwareFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dateUploaded").value(hasItem(DEFAULT_DATE_UPLOADED.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())));
    }
    
    @Test
    @Transactional
    public void getHardwareFile() throws Exception {
        // Initialize the database
        hardwareFileRepository.saveAndFlush(hardwareFile);

        // Get the hardwareFile
        restHardwareFileMockMvc.perform(get("/api/hardware-files/{id}", hardwareFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hardwareFile.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE.intValue()))
            .andExpect(jsonPath("$.mimeType").value(DEFAULT_MIME_TYPE.toString()))
            .andExpect(jsonPath("$.dateUploaded").value(DEFAULT_DATE_UPLOADED.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHardwareFile() throws Exception {
        // Get the hardwareFile
        restHardwareFileMockMvc.perform(get("/api/hardware-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHardwareFile() throws Exception {
        // Initialize the database
        hardwareFileRepository.saveAndFlush(hardwareFile);

        int databaseSizeBeforeUpdate = hardwareFileRepository.findAll().size();

        // Update the hardwareFile
        HardwareFile updatedHardwareFile = hardwareFileRepository.findById(hardwareFile.getId()).get();
        // Disconnect from session so that the updates on updatedHardwareFile are not directly saved in db
        em.detach(updatedHardwareFile);
        updatedHardwareFile
            .title(UPDATED_TITLE)
            .size(UPDATED_SIZE)
            .mimeType(UPDATED_MIME_TYPE)
            .dateUploaded(UPDATED_DATE_UPLOADED)
            .version(UPDATED_VERSION)
            .category(UPDATED_CATEGORY);

        restHardwareFileMockMvc.perform(put("/api/hardware-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHardwareFile)))
            .andExpect(status().isOk());

        // Validate the HardwareFile in the database
        List<HardwareFile> hardwareFileList = hardwareFileRepository.findAll();
        assertThat(hardwareFileList).hasSize(databaseSizeBeforeUpdate);
        HardwareFile testHardwareFile = hardwareFileList.get(hardwareFileList.size() - 1);
        assertThat(testHardwareFile.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testHardwareFile.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testHardwareFile.getMimeType()).isEqualTo(UPDATED_MIME_TYPE);
        assertThat(testHardwareFile.getDateUploaded()).isEqualTo(UPDATED_DATE_UPLOADED);
        assertThat(testHardwareFile.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testHardwareFile.getCategory()).isEqualTo(UPDATED_CATEGORY);

        // Validate the HardwareFile in Elasticsearch
        verify(mockHardwareFileSearchRepository, times(1)).save(testHardwareFile);
    }

    @Test
    @Transactional
    public void updateNonExistingHardwareFile() throws Exception {
        int databaseSizeBeforeUpdate = hardwareFileRepository.findAll().size();

        // Create the HardwareFile

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHardwareFileMockMvc.perform(put("/api/hardware-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hardwareFile)))
            .andExpect(status().isBadRequest());

        // Validate the HardwareFile in the database
        List<HardwareFile> hardwareFileList = hardwareFileRepository.findAll();
        assertThat(hardwareFileList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HardwareFile in Elasticsearch
        verify(mockHardwareFileSearchRepository, times(0)).save(hardwareFile);
    }

    @Test
    @Transactional
    public void deleteHardwareFile() throws Exception {
        // Initialize the database
        hardwareFileRepository.saveAndFlush(hardwareFile);

        int databaseSizeBeforeDelete = hardwareFileRepository.findAll().size();

        // Delete the hardwareFile
        restHardwareFileMockMvc.perform(delete("/api/hardware-files/{id}", hardwareFile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HardwareFile> hardwareFileList = hardwareFileRepository.findAll();
        assertThat(hardwareFileList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the HardwareFile in Elasticsearch
        verify(mockHardwareFileSearchRepository, times(1)).deleteById(hardwareFile.getId());
    }

    @Test
    @Transactional
    public void searchHardwareFile() throws Exception {
        // Initialize the database
        hardwareFileRepository.saveAndFlush(hardwareFile);
        when(mockHardwareFileSearchRepository.search(queryStringQuery("id:" + hardwareFile.getId())))
            .thenReturn(Collections.singletonList(hardwareFile));
        // Search the hardwareFile
        restHardwareFileMockMvc.perform(get("/api/_search/hardware-files?query=id:" + hardwareFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hardwareFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE)))
            .andExpect(jsonPath("$.[*].dateUploaded").value(hasItem(DEFAULT_DATE_UPLOADED.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HardwareFile.class);
        HardwareFile hardwareFile1 = new HardwareFile();
        hardwareFile1.setId(1L);
        HardwareFile hardwareFile2 = new HardwareFile();
        hardwareFile2.setId(hardwareFile1.getId());
        assertThat(hardwareFile1).isEqualTo(hardwareFile2);
        hardwareFile2.setId(2L);
        assertThat(hardwareFile1).isNotEqualTo(hardwareFile2);
        hardwareFile1.setId(null);
        assertThat(hardwareFile1).isNotEqualTo(hardwareFile2);
    }
}
