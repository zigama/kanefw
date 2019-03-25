package com.pivotaccess.kanefw.web.rest;
import com.pivotaccess.kanefw.domain.HardwareFile;
import com.pivotaccess.kanefw.repository.HardwareFileRepository;
import com.pivotaccess.kanefw.repository.search.HardwareFileSearchRepository;
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
 * REST controller for managing HardwareFile.
 */
@RestController
@RequestMapping("/api")
public class HardwareFileResource {

    private final Logger log = LoggerFactory.getLogger(HardwareFileResource.class);

    private static final String ENTITY_NAME = "hardwareFile";

    private final HardwareFileRepository hardwareFileRepository;

    private final HardwareFileSearchRepository hardwareFileSearchRepository;

    public HardwareFileResource(HardwareFileRepository hardwareFileRepository, HardwareFileSearchRepository hardwareFileSearchRepository) {
        this.hardwareFileRepository = hardwareFileRepository;
        this.hardwareFileSearchRepository = hardwareFileSearchRepository;
    }

    /**
     * POST  /hardware-files : Create a new hardwareFile.
     *
     * @param hardwareFile the hardwareFile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hardwareFile, or with status 400 (Bad Request) if the hardwareFile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/hardware-files")
    public ResponseEntity<HardwareFile> createHardwareFile(@Valid @RequestBody HardwareFile hardwareFile) throws URISyntaxException {
        log.debug("REST request to save HardwareFile : {}", hardwareFile);
        if (hardwareFile.getId() != null) {
            throw new BadRequestAlertException("A new hardwareFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HardwareFile result = hardwareFileRepository.save(hardwareFile);
        hardwareFileSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/hardware-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hardware-files : Updates an existing hardwareFile.
     *
     * @param hardwareFile the hardwareFile to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hardwareFile,
     * or with status 400 (Bad Request) if the hardwareFile is not valid,
     * or with status 500 (Internal Server Error) if the hardwareFile couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/hardware-files")
    public ResponseEntity<HardwareFile> updateHardwareFile(@Valid @RequestBody HardwareFile hardwareFile) throws URISyntaxException {
        log.debug("REST request to update HardwareFile : {}", hardwareFile);
        if (hardwareFile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HardwareFile result = hardwareFileRepository.save(hardwareFile);
        hardwareFileSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, hardwareFile.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hardware-files : get all the hardwareFiles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of hardwareFiles in body
     */
    @GetMapping("/hardware-files")
    public List<HardwareFile> getAllHardwareFiles() {
        log.debug("REST request to get all HardwareFiles");
        return hardwareFileRepository.findAll();
    }

    /**
     * GET  /hardware-files/:id : get the "id" hardwareFile.
     *
     * @param id the id of the hardwareFile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hardwareFile, or with status 404 (Not Found)
     */
    @GetMapping("/hardware-files/{id}")
    public ResponseEntity<HardwareFile> getHardwareFile(@PathVariable Long id) {
        log.debug("REST request to get HardwareFile : {}", id);
        Optional<HardwareFile> hardwareFile = hardwareFileRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(hardwareFile);
    }

    /**
     * DELETE  /hardware-files/:id : delete the "id" hardwareFile.
     *
     * @param id the id of the hardwareFile to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/hardware-files/{id}")
    public ResponseEntity<Void> deleteHardwareFile(@PathVariable Long id) {
        log.debug("REST request to delete HardwareFile : {}", id);
        hardwareFileRepository.deleteById(id);
        hardwareFileSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/hardware-files?query=:query : search for the hardwareFile corresponding
     * to the query.
     *
     * @param query the query of the hardwareFile search
     * @return the result of the search
     */
    @GetMapping("/_search/hardware-files")
    public List<HardwareFile> searchHardwareFiles(@RequestParam String query) {
        log.debug("REST request to search HardwareFiles for query {}", query);
        return StreamSupport
            .stream(hardwareFileSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
