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

import co.edu.uniandes.xrepo.domain.Sample;
import co.edu.uniandes.xrepo.service.SampleService;
import co.edu.uniandes.xrepo.web.rest.errors.BadRequestAlertException;
import co.edu.uniandes.xrepo.web.rest.util.HeaderUtil;
import co.edu.uniandes.xrepo.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Sample.
 */
@RestController
@RequestMapping("/api")
public class SampleResource {

    private final Logger log = LoggerFactory.getLogger(SampleResource.class);

    private static final String ENTITY_NAME = "sample";

    private final SampleService sampleService;

    public SampleResource(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    /**
     * POST  /samples : Create a new sample.
     *
     * @param sample the sample to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sample, or with status 400 (Bad Request) if the sample has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/samples")
    public ResponseEntity<Sample> createSample(@Valid @RequestBody Sample sample) throws URISyntaxException {
        log.debug("REST request to save Sample : {}", sample);
        if (sample.getId() != null) {
            throw new BadRequestAlertException("A new sample cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sample result = sampleService.save(sample);
        return ResponseEntity.created(new URI("/api/samples/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /samples : Updates an existing sample.
     *
     * @param sample the sample to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sample,
     * or with status 400 (Bad Request) if the sample is not valid,
     * or with status 500 (Internal Server Error) if the sample couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/samples")
    public ResponseEntity<Sample> updateSample(@Valid @RequestBody Sample sample) throws URISyntaxException {
        log.debug("REST request to update Sample : {}", sample);
        if (sample.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Sample result = sampleService.save(sample);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sample.getId().toString()))
            .body(result);
    }

    /**
     * GET  /samples : get all the samples.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of samples in body
     */
    @GetMapping("/samples")
    public ResponseEntity<List<Sample>> getAllSamples(Pageable pageable) {
        log.debug("REST request to get a page of Samples");
        Page<Sample> page = sampleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/samples");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /samples/:id : get the "id" sample.
     *
     * @param id the id of the sample to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sample, or with status 404 (Not Found)
     */
    @GetMapping("/samples/{id}")
    public ResponseEntity<Sample> getSample(@PathVariable String id) {
        log.debug("REST request to get Sample : {}", id);
        Optional<Sample> sample = sampleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sample);
    }

    /**
     * DELETE  /samples/:id : delete the "id" sample.
     *
     * @param id the id of the sample to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/samples/{id}")
    public ResponseEntity<Void> deleteSample(@PathVariable String id) {
        log.debug("REST request to delete Sample : {}", id);
        sampleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
