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

import co.edu.uniandes.xrepo.service.SamplingService;
import co.edu.uniandes.xrepo.service.dto.SamplingDTO;
import co.edu.uniandes.xrepo.service.dto.SensorDTO;
import co.edu.uniandes.xrepo.web.rest.errors.BadRequestAlertException;
import co.edu.uniandes.xrepo.web.rest.util.HeaderUtil;
import co.edu.uniandes.xrepo.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Sampling.
 */
@RestController
@RequestMapping("/api")
public class SamplingResource {

    private final Logger log = LoggerFactory.getLogger(SamplingResource.class);

    private static final String ENTITY_NAME = "sampling";

    private final SamplingService samplingService;

    public SamplingResource(SamplingService samplingService) {
        this.samplingService = samplingService;
    }

    /**
     * POST  /samplings : Create a new sampling.
     *
     * @param samplingDTO the samplingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new samplingDTO, or with status 400 (Bad Request) if the sampling has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/samplings")
    public ResponseEntity<SamplingDTO> createSampling(@Valid @RequestBody SamplingDTO samplingDTO) throws URISyntaxException {
        log.debug("REST request to save Sampling : {}", samplingDTO);
        if (samplingDTO.getId() != null) {
            throw new BadRequestAlertException("A new sampling cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SamplingDTO result = samplingService.save(samplingDTO);
        return ResponseEntity.created(new URI("/api/samplings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /samplings : Updates an existing sampling.
     *
     * @param samplingDTO the samplingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated samplingDTO,
     * or with status 400 (Bad Request) if the samplingDTO is not valid,
     * or with status 500 (Internal Server Error) if the samplingDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/samplings")
    public ResponseEntity<SamplingDTO> updateSampling(@Valid @RequestBody SamplingDTO samplingDTO) throws URISyntaxException {
        log.debug("REST request to update Sampling : {}", samplingDTO);
        if (samplingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SamplingDTO result = samplingService.save(samplingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, samplingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /samplings : get all the samplings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of samplings in body
     */
    @GetMapping("/samplings")
    public ResponseEntity<List<SamplingDTO>> getAllSamplings(Pageable pageable) {
        log.debug("REST request to get a page of Samplings");
        Page<SamplingDTO> page = samplingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/samplings");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /samplings/:id : get the "id" sampling.
     *
     * @param id the id of the samplingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the samplingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/samplings/{id}")
    public ResponseEntity<SamplingDTO> getSampling(@PathVariable String id) {
        log.debug("REST request to get Sampling : {}", id);
        Optional<SamplingDTO> samplingDTO = samplingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(samplingDTO);
    }

    /**
     * DELETE  /samplings/:id : delete the "id" sampling.
     *
     * @param id the id of the samplingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/samplings/{id}")
    public ResponseEntity<Void> deleteSampling(@PathVariable String id) {
        log.debug("REST request to delete Sampling : {}", id);
        samplingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    @GetMapping("/samplings/sensors/{tsId}")
    public ResponseEntity<List<SensorDTO>> getSensorsByTargetSystem(@PathVariable String tsId) {
        log.debug("REST request to get all sensors from target system");
        List<SensorDTO> tags = samplingService.findSensorsByTargetSystem(tsId);
        return ResponseEntity.ok().body(tags);
    }
}
