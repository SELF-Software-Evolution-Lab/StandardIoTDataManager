package co.edu.uniandes.xrepo.web.rest;

import co.edu.uniandes.xrepo.domain.SampleFiles;
import co.edu.uniandes.xrepo.service.SampleFilesService;
import co.edu.uniandes.xrepo.web.rest.errors.BadRequestAlertException;
import co.edu.uniandes.xrepo.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SampleFiles.
 */
@RestController
@RequestMapping("/api")
public class SampleFilesResource {

    private final Logger log = LoggerFactory.getLogger(SampleFilesResource.class);

    private static final String ENTITY_NAME = "sampleFiles";

    private final SampleFilesService sampleFilesService;

    public SampleFilesResource(SampleFilesService sampleFilesService) {
        this.sampleFilesService = sampleFilesService;
    }

    /**
     * POST /sample-files : Create a new sampleFiles.
     *
     * @param sampleFiles the sampleFiles to create
     * @return the ResponseEntity with status 201 (Created) and with body the new
     *         sampleFiles, or with status 400 (Bad Request) if the sampleFiles has
     *         already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sample-files")
    public ResponseEntity<SampleFiles> createSampleFiles(@RequestBody SampleFiles sampleFiles)
            throws URISyntaxException {
        log.debug("REST request to save SampleFiles : {}", sampleFiles);
        if (sampleFiles.getId() != null) {
            throw new BadRequestAlertException("A new sampleFiles cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
        SampleFiles result = sampleFilesService.save(sampleFiles);
        return ResponseEntity.created(new URI("/api/sample-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sample-files : Updates an existing sampleFiles.
     *
     * @param sampleFiles the sampleFiles to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sampleFiles,
     * or with status 400 (Bad Request) if the sampleFiles is not valid,
     * or with status 500 (Internal Server Error) if the sampleFiles couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sample-files")
    public ResponseEntity<SampleFiles> updateSampleFiles(@RequestBody SampleFiles sampleFiles) throws URISyntaxException {
        log.debug("REST request to update SampleFiles : {}", sampleFiles);
        if (sampleFiles.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SampleFiles result = sampleFilesService.save(sampleFiles);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sampleFiles.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sample-files : get all the sampleFiles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sampleFiles in body
     */
    @GetMapping("/sample-files")
    public List<SampleFiles> getAllSampleFiles() {
        log.debug("REST request to get all SampleFiles");
        return sampleFilesService.findAll();
    }

    /**
     * GET  /sample-files/:id : get the "id" sampleFiles.
     *
     * @param id the id of the sampleFiles to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sampleFiles, or with status 404 (Not Found)
     */
    @GetMapping("/sample-files/{id}")
    public ResponseEntity<SampleFiles> getSampleFiles(@PathVariable String id) {
        log.debug("REST request to get SampleFiles : {}", id);
        Optional<SampleFiles> sampleFiles = sampleFilesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sampleFiles);
    }

    /**
     * DELETE  /sample-files/:id : delete the "id" sampleFiles.
     *
     * @param id the id of the sampleFiles to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sample-files/{id}")
    public ResponseEntity<Void> deleteSampleFiles(@PathVariable String id) {
        log.debug("REST request to delete SampleFiles : {}", id);
        sampleFilesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
