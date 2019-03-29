package com.pivotaccess.kanefw.web.rest;
import com.pivotaccess.kanefw.domain.Device;
import com.pivotaccess.kanefw.domain.DeviceHealth;
import com.pivotaccess.kanefw.repository.DeviceHealthRepository;
import com.pivotaccess.kanefw.repository.DeviceRepository;
import com.pivotaccess.kanefw.repository.search.DeviceHealthSearchRepository;
import com.pivotaccess.kanefw.service.dto.DeviceHealthDTO;
import com.pivotaccess.kanefw.web.rest.errors.BadRequestAlertException;
import com.pivotaccess.kanefw.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing DeviceHealth.
 */
@RestController
@RequestMapping("/api")
public class DeviceHealthResource {

    private final Logger log = LoggerFactory.getLogger(DeviceHealthResource.class);

    private static final String ENTITY_NAME = "deviceHealth";

    private final DeviceHealthRepository deviceHealthRepository;

    private final DeviceHealthSearchRepository deviceHealthSearchRepository;
    
    private final DeviceRepository deviceRepository;

    public DeviceHealthResource(DeviceHealthRepository deviceHealthRepository,
    							DeviceHealthSearchRepository deviceHealthSearchRepository,
    							DeviceRepository deviceRepository) {
        this.deviceHealthRepository = deviceHealthRepository;
        this.deviceHealthSearchRepository = deviceHealthSearchRepository;
        this.deviceRepository = deviceRepository;
    }

    /**
     * POST  /device-healths : Create a new deviceHealth.
     *
     * @param deviceHealth the deviceHealth to create
     * @return the ResponseEntity with status 201 (Created) and with body the new deviceHealth, or with status 400 (Bad Request) if the deviceHealth has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/device-healths")
    public ResponseEntity<DeviceHealth> createDeviceHealth(@Valid @RequestBody DeviceHealth deviceHealth) throws URISyntaxException {
        log.debug("REST request to save DeviceHealth : {}", deviceHealth);
        if (deviceHealth.getId() != null) {
            throw new BadRequestAlertException("A new deviceHealth cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeviceHealth result = deviceHealthRepository.save(deviceHealth);
        deviceHealthSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/device-healths/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    @PostMapping("/device-healths")
    public ResponseEntity<DeviceHealthDTO> createDeviceHealth(
    				@RequestParam("deviceId") String deviceId,
    				@RequestParam("timeStamp") String timeStamp,
    				@RequestParam("rssi") String rssi,
    				@RequestParam("locationLat") String locationLat,
    				@RequestParam("locationLong") String locationLong,
    				@RequestParam("devicePhoneNumber") String devicePhoneNumber,
    				@RequestParam("deviceCarrier") String deviceCarrier,
    				@RequestParam("printerStatus") String printerStatus
    						) throws URISyntaxException {
    	
    	DeviceHealth deviceHealth = new DeviceHealth();
    	Optional<Device> device = deviceRepository.findById(Long.parseLong(deviceId));
    	
        
    	if (device.get() == null || device.get().getId() == null) {
            throw new BadRequestAlertException("Invalid device ID", "Device", "notfound");
        }
        
        
        try {
			deviceHealth.setDevice(device.get());
			deviceHealth.setTimeStamp(Instant.ofEpochSecond(Long.parseLong(timeStamp)));
			deviceHealth.setRssi(rssi);
			deviceHealth.setLocationLat(locationLat);
			deviceHealth.setLocationLong(locationLong);
			deviceHealth.setDevicePhoneNumber(devicePhoneNumber);
			deviceHealth.setDeviceCarrier(deviceCarrier);
			deviceHealth.setPrinterStatus(printerStatus);
			
        	log.debug("REST request to save DeviceHealth : {}", deviceHealth);
		} catch (Exception e) {
			// TODO: handle exception
		}
        
        DeviceHealth result = deviceHealthRepository.save(deviceHealth);
        deviceHealthSearchRepository.save(result);
        
        DeviceHealthDTO deviceHealthDTO = new DeviceHealthDTO(result);
        return ResponseEntity.created(new URI("/api/device-healths/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(deviceHealthDTO);
    }

    /**
     * PUT  /device-healths : Updates an existing deviceHealth.
     *
     * @param deviceHealth the deviceHealth to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated deviceHealth,
     * or with status 400 (Bad Request) if the deviceHealth is not valid,
     * or with status 500 (Internal Server Error) if the deviceHealth couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/device-healths")
    public ResponseEntity<DeviceHealth> updateDeviceHealth(@Valid @RequestBody DeviceHealth deviceHealth) throws URISyntaxException {
        log.debug("REST request to update DeviceHealth : {}", deviceHealth);
        if (deviceHealth.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DeviceHealth result = deviceHealthRepository.save(deviceHealth);
        deviceHealthSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, deviceHealth.getId().toString()))
            .body(result);
    }

    /**
     * GET  /device-healths : get all the deviceHealths.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of deviceHealths in body
     */
    @GetMapping("/device-healths")
    public List<DeviceHealth> getAllDeviceHealths() {
        log.debug("REST request to get all DeviceHealths");
        return deviceHealthRepository.findAll();
    }

    /**
     * GET  /device-healths/:id : get the "id" deviceHealth.
     *
     * @param id the id of the deviceHealth to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the deviceHealth, or with status 404 (Not Found)
     */
    @GetMapping("/device-healths/{id}")
    public ResponseEntity<DeviceHealth> getDeviceHealth(@PathVariable Long id) {
        log.debug("REST request to get DeviceHealth : {}", id);
        Optional<DeviceHealth> deviceHealth = deviceHealthRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(deviceHealth);
    }

    /**
     * DELETE  /device-healths/:id : delete the "id" deviceHealth.
     *
     * @param id the id of the deviceHealth to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/device-healths/{id}")
    public ResponseEntity<Void> deleteDeviceHealth(@PathVariable Long id) {
        log.debug("REST request to delete DeviceHealth : {}", id);
        deviceHealthRepository.deleteById(id);
        deviceHealthSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/device-healths?query=:query : search for the deviceHealth corresponding
     * to the query.
     *
     * @param query the query of the deviceHealth search
     * @return the result of the search
     */
    @GetMapping("/_search/device-healths")
    public List<DeviceHealth> searchDeviceHealths(@RequestParam String query) {
        log.debug("REST request to search DeviceHealths for query {}", query);
        return StreamSupport
            .stream(deviceHealthSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
