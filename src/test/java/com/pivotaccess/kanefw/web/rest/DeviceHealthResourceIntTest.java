package com.pivotaccess.kanefw.web.rest;

import com.pivotaccess.kanefw.KanefwApp;

import com.pivotaccess.kanefw.domain.DeviceHealth;
import com.pivotaccess.kanefw.domain.Device;
import com.pivotaccess.kanefw.repository.DeviceHealthRepository;
import com.pivotaccess.kanefw.repository.DeviceRepository;
import com.pivotaccess.kanefw.repository.search.DeviceHealthSearchRepository;
import com.pivotaccess.kanefw.service.HardwareFileService;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Test class for the DeviceHealthResource REST controller.
 *
 * @see DeviceHealthResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KanefwApp.class)
public class DeviceHealthResourceIntTest {

    private static final Instant DEFAULT_TIME_STAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME_STAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_RSSI = "AAAAAAAAAA";
    private static final String UPDATED_RSSI = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION_LAT = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_LAT = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION_LONG = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_LONG = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_CARRIER = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_CARRIER = "BBBBBBBBBB";

    private static final String DEFAULT_PRINTER_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_PRINTER_STATUS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_UPDATE_AVAILABLE = false;
    private static final Boolean UPDATED_UPDATE_AVAILABLE = true;

    private static final Boolean DEFAULT_UPDATE_REQUIRED = false;
    private static final Boolean UPDATED_UPDATE_REQUIRED = true;

    private static final String DEFAULT_NEW_APP_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_NEW_APP_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_OTA_SERVER_IP = "AAAAAAAAAA";
    private static final String UPDATED_OTA_SERVER_IP = "BBBBBBBBBB";

    private static final String DEFAULT_NEW_APP_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NEW_APP_FILE_NAME = "BBBBBBBBBB";

    @Autowired
    private DeviceHealthRepository deviceHealthRepository;

    /**
     * This repository is mocked in the com.pivotaccess.kanefw.repository.search test package.
     *
     * @see com.pivotaccess.kanefw.repository.search.DeviceHealthSearchRepositoryMockConfiguration
     */
    @Autowired
    private DeviceHealthSearchRepository mockDeviceHealthSearchRepository;

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

    private MockMvc restDeviceHealthMockMvc;

    private DeviceHealth deviceHealth;
    
    private DeviceRepository deviceRepository;
    
    private HardwareFileService hardwareFileService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DeviceHealthResource deviceHealthResource = new DeviceHealthResource(deviceHealthRepository, mockDeviceHealthSearchRepository, deviceRepository, hardwareFileService);
        this.restDeviceHealthMockMvc = MockMvcBuilders.standaloneSetup(deviceHealthResource)
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
    public static DeviceHealth createEntity(EntityManager em) {
        DeviceHealth deviceHealth = new DeviceHealth()
            .timeStamp(DEFAULT_TIME_STAMP)
            .rssi(DEFAULT_RSSI)
            .locationLat(DEFAULT_LOCATION_LAT)
            .locationLong(DEFAULT_LOCATION_LONG)
            .devicePhoneNumber(DEFAULT_DEVICE_PHONE_NUMBER)
            .deviceCarrier(DEFAULT_DEVICE_CARRIER)
            .printerStatus(DEFAULT_PRINTER_STATUS)
            .updateAvailable(DEFAULT_UPDATE_AVAILABLE)
            .updateRequired(DEFAULT_UPDATE_REQUIRED)
            .newAppVersion(DEFAULT_NEW_APP_VERSION)
            .otaServerIp(DEFAULT_OTA_SERVER_IP)
            .newAppFileName(DEFAULT_NEW_APP_FILE_NAME);
        // Add required entity
        Device device = DeviceResourceIntTest.createEntity(em);
        em.persist(device);
        em.flush();
        deviceHealth.setDevice(device);
        return deviceHealth;
    }

    @Before
    public void initTest() {
        deviceHealth = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeviceHealth() throws Exception {
        int databaseSizeBeforeCreate = deviceHealthRepository.findAll().size();

        // Create the DeviceHealth
        restDeviceHealthMockMvc.perform(post("/api/device-healths")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceHealth)))
            .andExpect(status().isCreated());

        // Validate the DeviceHealth in the database
        List<DeviceHealth> deviceHealthList = deviceHealthRepository.findAll();
        assertThat(deviceHealthList).hasSize(databaseSizeBeforeCreate + 1);
        DeviceHealth testDeviceHealth = deviceHealthList.get(deviceHealthList.size() - 1);
        assertThat(testDeviceHealth.getTimeStamp()).isEqualTo(DEFAULT_TIME_STAMP);
        assertThat(testDeviceHealth.getRssi()).isEqualTo(DEFAULT_RSSI);
        assertThat(testDeviceHealth.getLocationLat()).isEqualTo(DEFAULT_LOCATION_LAT);
        assertThat(testDeviceHealth.getLocationLong()).isEqualTo(DEFAULT_LOCATION_LONG);
        assertThat(testDeviceHealth.getDevicePhoneNumber()).isEqualTo(DEFAULT_DEVICE_PHONE_NUMBER);
        assertThat(testDeviceHealth.getDeviceCarrier()).isEqualTo(DEFAULT_DEVICE_CARRIER);
        assertThat(testDeviceHealth.getPrinterStatus()).isEqualTo(DEFAULT_PRINTER_STATUS);
        assertThat(testDeviceHealth.isUpdateAvailable()).isEqualTo(DEFAULT_UPDATE_AVAILABLE);
        assertThat(testDeviceHealth.isUpdateRequired()).isEqualTo(DEFAULT_UPDATE_REQUIRED);
        assertThat(testDeviceHealth.getNewAppVersion()).isEqualTo(DEFAULT_NEW_APP_VERSION);
        assertThat(testDeviceHealth.getOtaServerIp()).isEqualTo(DEFAULT_OTA_SERVER_IP);
        assertThat(testDeviceHealth.getNewAppFileName()).isEqualTo(DEFAULT_NEW_APP_FILE_NAME);

        // Validate the DeviceHealth in Elasticsearch
        verify(mockDeviceHealthSearchRepository, times(1)).save(testDeviceHealth);
    }

    @Test
    @Transactional
    public void createDeviceHealthWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = deviceHealthRepository.findAll().size();

        // Create the DeviceHealth with an existing ID
        deviceHealth.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceHealthMockMvc.perform(post("/api/device-healths")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceHealth)))
            .andExpect(status().isBadRequest());

        // Validate the DeviceHealth in the database
        List<DeviceHealth> deviceHealthList = deviceHealthRepository.findAll();
        assertThat(deviceHealthList).hasSize(databaseSizeBeforeCreate);

        // Validate the DeviceHealth in Elasticsearch
        verify(mockDeviceHealthSearchRepository, times(0)).save(deviceHealth);
    }

    @Test
    @Transactional
    public void getAllDeviceHealths() throws Exception {
        // Initialize the database
        deviceHealthRepository.saveAndFlush(deviceHealth);

        // Get all the deviceHealthList
        restDeviceHealthMockMvc.perform(get("/api/device-healths?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceHealth.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeStamp").value(hasItem(DEFAULT_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].rssi").value(hasItem(DEFAULT_RSSI.toString())))
            .andExpect(jsonPath("$.[*].locationLat").value(hasItem(DEFAULT_LOCATION_LAT.toString())))
            .andExpect(jsonPath("$.[*].locationLong").value(hasItem(DEFAULT_LOCATION_LONG.toString())))
            .andExpect(jsonPath("$.[*].devicePhoneNumber").value(hasItem(DEFAULT_DEVICE_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].deviceCarrier").value(hasItem(DEFAULT_DEVICE_CARRIER.toString())))
            .andExpect(jsonPath("$.[*].printerStatus").value(hasItem(DEFAULT_PRINTER_STATUS.toString())))
            .andExpect(jsonPath("$.[*].updateAvailable").value(hasItem(DEFAULT_UPDATE_AVAILABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].updateRequired").value(hasItem(DEFAULT_UPDATE_REQUIRED.booleanValue())))
            .andExpect(jsonPath("$.[*].newAppVersion").value(hasItem(DEFAULT_NEW_APP_VERSION.toString())))
            .andExpect(jsonPath("$.[*].otaServerIp").value(hasItem(DEFAULT_OTA_SERVER_IP.toString())))
            .andExpect(jsonPath("$.[*].newAppFileName").value(hasItem(DEFAULT_NEW_APP_FILE_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getDeviceHealth() throws Exception {
        // Initialize the database
        deviceHealthRepository.saveAndFlush(deviceHealth);

        // Get the deviceHealth
        restDeviceHealthMockMvc.perform(get("/api/device-healths/{id}", deviceHealth.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(deviceHealth.getId().intValue()))
            .andExpect(jsonPath("$.timeStamp").value(DEFAULT_TIME_STAMP.toString()))
            .andExpect(jsonPath("$.rssi").value(DEFAULT_RSSI.toString()))
            .andExpect(jsonPath("$.locationLat").value(DEFAULT_LOCATION_LAT.toString()))
            .andExpect(jsonPath("$.locationLong").value(DEFAULT_LOCATION_LONG.toString()))
            .andExpect(jsonPath("$.devicePhoneNumber").value(DEFAULT_DEVICE_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.deviceCarrier").value(DEFAULT_DEVICE_CARRIER.toString()))
            .andExpect(jsonPath("$.printerStatus").value(DEFAULT_PRINTER_STATUS.toString()))
            .andExpect(jsonPath("$.updateAvailable").value(DEFAULT_UPDATE_AVAILABLE.booleanValue()))
            .andExpect(jsonPath("$.updateRequired").value(DEFAULT_UPDATE_REQUIRED.booleanValue()))
            .andExpect(jsonPath("$.newAppVersion").value(DEFAULT_NEW_APP_VERSION.toString()))
            .andExpect(jsonPath("$.otaServerIp").value(DEFAULT_OTA_SERVER_IP.toString()))
            .andExpect(jsonPath("$.newAppFileName").value(DEFAULT_NEW_APP_FILE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDeviceHealth() throws Exception {
        // Get the deviceHealth
        restDeviceHealthMockMvc.perform(get("/api/device-healths/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeviceHealth() throws Exception {
        // Initialize the database
        deviceHealthRepository.saveAndFlush(deviceHealth);

        int databaseSizeBeforeUpdate = deviceHealthRepository.findAll().size();

        // Update the deviceHealth
        DeviceHealth updatedDeviceHealth = deviceHealthRepository.findById(deviceHealth.getId()).get();
        // Disconnect from session so that the updates on updatedDeviceHealth are not directly saved in db
        em.detach(updatedDeviceHealth);
        updatedDeviceHealth
            .timeStamp(UPDATED_TIME_STAMP)
            .rssi(UPDATED_RSSI)
            .locationLat(UPDATED_LOCATION_LAT)
            .locationLong(UPDATED_LOCATION_LONG)
            .devicePhoneNumber(UPDATED_DEVICE_PHONE_NUMBER)
            .deviceCarrier(UPDATED_DEVICE_CARRIER)
            .printerStatus(UPDATED_PRINTER_STATUS)
            .updateAvailable(UPDATED_UPDATE_AVAILABLE)
            .updateRequired(UPDATED_UPDATE_REQUIRED)
            .newAppVersion(UPDATED_NEW_APP_VERSION)
            .otaServerIp(UPDATED_OTA_SERVER_IP)
            .newAppFileName(UPDATED_NEW_APP_FILE_NAME);

        restDeviceHealthMockMvc.perform(put("/api/device-healths")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDeviceHealth)))
            .andExpect(status().isOk());

        // Validate the DeviceHealth in the database
        List<DeviceHealth> deviceHealthList = deviceHealthRepository.findAll();
        assertThat(deviceHealthList).hasSize(databaseSizeBeforeUpdate);
        DeviceHealth testDeviceHealth = deviceHealthList.get(deviceHealthList.size() - 1);
        assertThat(testDeviceHealth.getTimeStamp()).isEqualTo(UPDATED_TIME_STAMP);
        assertThat(testDeviceHealth.getRssi()).isEqualTo(UPDATED_RSSI);
        assertThat(testDeviceHealth.getLocationLat()).isEqualTo(UPDATED_LOCATION_LAT);
        assertThat(testDeviceHealth.getLocationLong()).isEqualTo(UPDATED_LOCATION_LONG);
        assertThat(testDeviceHealth.getDevicePhoneNumber()).isEqualTo(UPDATED_DEVICE_PHONE_NUMBER);
        assertThat(testDeviceHealth.getDeviceCarrier()).isEqualTo(UPDATED_DEVICE_CARRIER);
        assertThat(testDeviceHealth.getPrinterStatus()).isEqualTo(UPDATED_PRINTER_STATUS);
        assertThat(testDeviceHealth.isUpdateAvailable()).isEqualTo(UPDATED_UPDATE_AVAILABLE);
        assertThat(testDeviceHealth.isUpdateRequired()).isEqualTo(UPDATED_UPDATE_REQUIRED);
        assertThat(testDeviceHealth.getNewAppVersion()).isEqualTo(UPDATED_NEW_APP_VERSION);
        assertThat(testDeviceHealth.getOtaServerIp()).isEqualTo(UPDATED_OTA_SERVER_IP);
        assertThat(testDeviceHealth.getNewAppFileName()).isEqualTo(UPDATED_NEW_APP_FILE_NAME);

        // Validate the DeviceHealth in Elasticsearch
        verify(mockDeviceHealthSearchRepository, times(1)).save(testDeviceHealth);
    }

    @Test
    @Transactional
    public void updateNonExistingDeviceHealth() throws Exception {
        int databaseSizeBeforeUpdate = deviceHealthRepository.findAll().size();

        // Create the DeviceHealth

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceHealthMockMvc.perform(put("/api/device-healths")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceHealth)))
            .andExpect(status().isBadRequest());

        // Validate the DeviceHealth in the database
        List<DeviceHealth> deviceHealthList = deviceHealthRepository.findAll();
        assertThat(deviceHealthList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DeviceHealth in Elasticsearch
        verify(mockDeviceHealthSearchRepository, times(0)).save(deviceHealth);
    }

    @Test
    @Transactional
    public void deleteDeviceHealth() throws Exception {
        // Initialize the database
        deviceHealthRepository.saveAndFlush(deviceHealth);

        int databaseSizeBeforeDelete = deviceHealthRepository.findAll().size();

        // Delete the deviceHealth
        restDeviceHealthMockMvc.perform(delete("/api/device-healths/{id}", deviceHealth.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DeviceHealth> deviceHealthList = deviceHealthRepository.findAll();
        assertThat(deviceHealthList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DeviceHealth in Elasticsearch
        verify(mockDeviceHealthSearchRepository, times(1)).deleteById(deviceHealth.getId());
    }

    @Test
    @Transactional
    public void searchDeviceHealth() throws Exception {
        // Initialize the database
        deviceHealthRepository.saveAndFlush(deviceHealth);
        when(mockDeviceHealthSearchRepository.search(queryStringQuery("id:" + deviceHealth.getId())))
            .thenReturn(Collections.singletonList(deviceHealth));
        // Search the deviceHealth
        restDeviceHealthMockMvc.perform(get("/api/_search/device-healths?query=id:" + deviceHealth.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceHealth.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeStamp").value(hasItem(DEFAULT_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].rssi").value(hasItem(DEFAULT_RSSI)))
            .andExpect(jsonPath("$.[*].locationLat").value(hasItem(DEFAULT_LOCATION_LAT)))
            .andExpect(jsonPath("$.[*].locationLong").value(hasItem(DEFAULT_LOCATION_LONG)))
            .andExpect(jsonPath("$.[*].devicePhoneNumber").value(hasItem(DEFAULT_DEVICE_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].deviceCarrier").value(hasItem(DEFAULT_DEVICE_CARRIER)))
            .andExpect(jsonPath("$.[*].printerStatus").value(hasItem(DEFAULT_PRINTER_STATUS)))
            .andExpect(jsonPath("$.[*].updateAvailable").value(hasItem(DEFAULT_UPDATE_AVAILABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].updateRequired").value(hasItem(DEFAULT_UPDATE_REQUIRED.booleanValue())))
            .andExpect(jsonPath("$.[*].newAppVersion").value(hasItem(DEFAULT_NEW_APP_VERSION)))
            .andExpect(jsonPath("$.[*].otaServerIp").value(hasItem(DEFAULT_OTA_SERVER_IP)))
            .andExpect(jsonPath("$.[*].newAppFileName").value(hasItem(DEFAULT_NEW_APP_FILE_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceHealth.class);
        DeviceHealth deviceHealth1 = new DeviceHealth();
        deviceHealth1.setId(1L);
        DeviceHealth deviceHealth2 = new DeviceHealth();
        deviceHealth2.setId(deviceHealth1.getId());
        assertThat(deviceHealth1).isEqualTo(deviceHealth2);
        deviceHealth2.setId(2L);
        assertThat(deviceHealth1).isNotEqualTo(deviceHealth2);
        deviceHealth1.setId(null);
        assertThat(deviceHealth1).isNotEqualTo(deviceHealth2);
    }
}
