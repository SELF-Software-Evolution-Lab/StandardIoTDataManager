package co.edu.uniandes.xrepo.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.xrepo.service.ExperimentService;
import co.edu.uniandes.xrepo.service.dto.ExperimentDTO;
import co.edu.uniandes.xrepo.web.rest.errors.BadRequestAlertException;
import co.edu.uniandes.xrepo.web.rest.util.HeaderUtil;
import co.edu.uniandes.xrepo.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Experiment.
 */
@RestController
@RequestMapping("/api")
public class ExperimentResource {

    private final Logger log = LoggerFactory.getLogger(ExperimentResource.class);

    private static final String ENTITY_NAME = "experiment";

    private final ExperimentService experimentService;

    public ExperimentResource(ExperimentService experimentService) {
        this.experimentService = experimentService;
    }

    /**
     * POST  /experiments : Create a new experiment.
     *
     * @param experimentDTO the experimentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new experimentDTO, or with status 400 (Bad Request) if the experiment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/experiments")
    public ResponseEntity<ExperimentDTO> createExperiment(@Valid @RequestBody ExperimentDTO experimentDTO) throws URISyntaxException {
        log.debug("REST request to save Experiment : {}", experimentDTO);
        if (experimentDTO.getId() != null) {
            throw new BadRequestAlertException("A new experiment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExperimentDTO result = experimentService.save(experimentDTO);
        return ResponseEntity.created(new URI("/api/experiments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /experiments : Updates an existing experiment.
     *
     * @param experimentDTO the experimentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated experimentDTO,
     * or with status 400 (Bad Request) if the experimentDTO is not valid,
     * or with status 500 (Internal Server Error) if the experimentDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/experiments")
    public ResponseEntity<ExperimentDTO> updateExperiment(@Valid @RequestBody ExperimentDTO experimentDTO) throws URISyntaxException {
        log.debug("REST request to update Experiment : {}", experimentDTO);
        if (experimentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExperimentDTO result = experimentService.save(experimentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, experimentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /experiments : get all the experiments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of experiments in body
     */
    @GetMapping("/experiments")
    public ResponseEntity<List<ExperimentDTO>> getAllExperiments(Pageable pageable) {
        log.debug("REST request to get a page of Experiments");
        Page<ExperimentDTO> page = experimentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/experiments");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /experiments/:id : get the "id" experiment.
     *
     * @param id the id of the experimentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the experimentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/experiments/{id}")
    public ResponseEntity<ExperimentDTO> getExperiment(@PathVariable String id) {
        log.debug("REST request to get Experiment : {}", id);
        Optional<ExperimentDTO> experimentDTO = experimentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(experimentDTO);
    }

    /**
     * DELETE  /experiments/:id : delete the "id" experiment.
     *
     * @param id the id of the experimentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/experiments/{id}")
    public ResponseEntity<Void> deleteExperiment(@PathVariable String id) {
        log.debug("REST request to delete Experiment : {}", id);
        experimentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
