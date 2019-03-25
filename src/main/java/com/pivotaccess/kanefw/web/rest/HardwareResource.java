package com.pivotaccess.kanefw.web.rest;
import com.pivotaccess.kanefw.domain.Hardware;
import com.pivotaccess.kanefw.repository.HardwareRepository;
import com.pivotaccess.kanefw.repository.search.HardwareSearchRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Hardware.
 */
@RestController
@RequestMapping("/api")
public class HardwareResource {

    private final Logger log = LoggerFactory.getLogger(HardwareResource.class);

    private static final String ENTITY_NAME = "hardware";

    private final HardwareRepository hardwareRepository;

    private final HardwareSearchRepository hardwareSearchRepository;

    public HardwareResource(HardwareRepository hardwareRepository, HardwareSearchRepository hardwareSearchRepository) {
        this.hardwareRepository = hardwareRepository;
        this.hardwareSearchRepository = hardwareSearchRepository;
    }

    /**
     * POST  /hardwares : Create a new hardware.
     *
     * @param hardware the hardware to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hardware, or with status 400 (Bad Request) if the hardware has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/hardwares")
    public ResponseEntity<Hardware> createHardware(@Valid @RequestBody Hardware hardware) throws URISyntaxException {
        log.debug("REST request to save Hardware : {}", hardware);
        if (hardware.getId() != null) {
            throw new BadRequestAlertException("A new hardware cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Hardware result = hardwareRepository.save(hardware);
        hardwareSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/hardwares/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hardwares : Updates an existing hardware.
     *
     * @param hardware the hardware to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hardware,
     * or with status 400 (Bad Request) if the hardware is not valid,
     * or with status 500 (Internal Server Error) if the hardware couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/hardwares")
    public ResponseEntity<Hardware> updateHardware(@Valid @RequestBody Hardware hardware) throws URISyntaxException {
        log.debug("REST request to update Hardware : {}", hardware);
        if (hardware.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Hardware result = hardwareRepository.save(hardware);
        hardwareSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, hardware.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hardwares : get all the hardwares.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of hardwares in body
     */
    @GetMapping("/hardwares")
    public List<Hardware> getAllHardwares() {
        log.debug("REST request to get all Hardwares");
        return hardwareRepository.findAll();
    }

    /**
     * GET  /hardwares/:id : get the "id" hardware.
     *
     * @param id the id of the hardware to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hardware, or with status 404 (Not Found)
     */
    @GetMapping("/hardwares/{id}")
    public ResponseEntity<Hardware> getHardware(@PathVariable Long id) {
        log.debug("REST request to get Hardware : {}", id);
        Optional<Hardware> hardware = hardwareRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(hardware);
    }

    /**
     * DELETE  /hardwares/:id : delete the "id" hardware.
     *
     * @param id the id of the hardware to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/hardwares/{id}")
    public ResponseEntity<Void> deleteHardware(@PathVariable Long id) {
        log.debug("REST request to delete Hardware : {}", id);
        hardwareRepository.deleteById(id);
        hardwareSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/hardwares?query=:query : search for the hardware corresponding
     * to the query.
     *
     * @param query the query of the hardware search
     * @return the result of the search
     */
    @GetMapping("/_search/hardwares")
    public List<Hardware> searchHardwares(@RequestParam String query) {
        log.debug("REST request to search Hardwares for query {}", query);
        return StreamSupport
            .stream(hardwareSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
