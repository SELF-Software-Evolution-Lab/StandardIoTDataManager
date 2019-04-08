package co.edu.uniandes.xrepo.web.rest;
import co.edu.uniandes.xrepo.service.SamplesFilesService;
import co.edu.uniandes.xrepo.web.rest.errors.BadRequestAlertException;
import co.edu.uniandes.xrepo.web.rest.util.HeaderUtil;
import co.edu.uniandes.xrepo.service.dto.SamplesFilesDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SamplesFiles.
 */
@RestController
@RequestMapping("/api")
public class SamplesFilesResource {

    private final Logger log = LoggerFactory.getLogger(SamplesFilesResource.class);

    private static final String ENTITY_NAME = "samplesFiles";

    private final SamplesFilesService samplesFilesService;

    private static String UPLOADED_FOLDER = "F://temp//";


    public SamplesFilesResource(SamplesFilesService samplesFilesService) {
        this.samplesFilesService = samplesFilesService;
    }

    /**
     * POST  /samples-files : Create a new samplesFiles.
     *
     * @param samplesFilesDTO the samplesFilesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new samplesFilesDTO, or with status 400 (Bad Request) if the samplesFiles has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/samples-files")
    public ResponseEntity<SamplesFilesDTO> createSamplesFiles(@RequestBody SamplesFilesDTO samplesFilesDTO) throws URISyntaxException {
        log.debug("REST request to save SamplesFiles : {}", samplesFilesDTO);
        if (samplesFilesDTO.getId() != null) {
            throw new BadRequestAlertException("A new samplesFiles cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SamplesFilesDTO result = samplesFilesService.save(samplesFilesDTO);
        return ResponseEntity.created(new URI("/api/samples-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /samples-files : Updates an existing samplesFiles.
     *
     * @param samplesFilesDTO the samplesFilesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated samplesFilesDTO,
     * or with status 400 (Bad Request) if the samplesFilesDTO is not valid,
     * or with status 500 (Internal Server Error) if the samplesFilesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/samples-files")
    public ResponseEntity<SamplesFilesDTO> updateSamplesFiles(@RequestBody SamplesFilesDTO samplesFilesDTO) throws URISyntaxException {
        log.debug("REST request to update SamplesFiles : {}", samplesFilesDTO);
        if (samplesFilesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SamplesFilesDTO result = samplesFilesService.save(samplesFilesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, samplesFilesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /samples-files : get all the samplesFiles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of samplesFiles in body
     */
    @GetMapping("/samples-files")
    public List<SamplesFilesDTO> getAllSamplesFiles() {
        log.debug("REST request to get all SamplesFiles");
        return samplesFilesService.findAll();
    }

    /**
     * GET  /samples-files/:id : get the "id" samplesFiles.
     *
     * @param id the id of the samplesFilesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the samplesFilesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/samples-files/{id}")
    public ResponseEntity<SamplesFilesDTO> getSamplesFiles(@PathVariable String id) {
        log.debug("REST request to get SamplesFiles : {}", id);
        Optional<SamplesFilesDTO> samplesFilesDTO = samplesFilesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(samplesFilesDTO);
    }

    /**
     * DELETE  /samples-files/:id : delete the "id" samplesFiles.
     *
     * @param id the id of the samplesFilesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/samples-files/{id}")
    public ResponseEntity<Void> deleteSamplesFiles(@PathVariable String id) {
        log.debug("REST request to delete SamplesFiles : {}", id);
        samplesFilesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
