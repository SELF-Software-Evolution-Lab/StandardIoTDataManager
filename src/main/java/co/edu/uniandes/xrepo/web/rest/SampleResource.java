package co.edu.uniandes.xrepo.web.rest;

import java.io.IOException;
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

import co.edu.uniandes.xrepo.service.SampleService;
import co.edu.uniandes.xrepo.service.SamplingService;
import co.edu.uniandes.xrepo.service.SearchEngineService;
import co.edu.uniandes.xrepo.service.SearchEngineService.SearchResponse;
import co.edu.uniandes.xrepo.service.dto.SampleDTO;
import co.edu.uniandes.xrepo.service.dto.SampleSearchParametersDTO;
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

    private final SamplingService samplingService;

    private final SearchEngineService searchEngineService;

    public SampleResource(SampleService sampleService, SearchEngineService searchEngineService
                            , SamplingService samplingService) {
        this.sampleService = sampleService;
        this.searchEngineService = searchEngineService;
        this.samplingService = samplingService;
    }

    /**
     * POST  /samples : Create a new sample.
     *
     * @param sampleDTO the sampleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sampleDTO, or with status 400 (Bad Request) if the sample has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/samples")
    public ResponseEntity<SampleDTO> createSample(@Valid @RequestBody SampleDTO sampleDTO) throws URISyntaxException {
        log.debug("REST request to save Sample : {}", sampleDTO);
        if (sampleDTO.getId() != null) {
            throw new BadRequestAlertException("A new sample cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SampleDTO result = sampleService.save(sampleDTO);
        return ResponseEntity.created(new URI("/api/samples/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/samples/data")
    public ResponseEntity<SearchResponse> preSearchSample(@RequestBody SampleSearchParametersDTO searchParametersDTO) throws IOException {
//        SearchResponse l = searchEngineService.preSearchSamples(searchParametersDTO);
        //to avoid messing with the response types,
        //im just implementing HDFS search on top of existing functionality
        SearchResponse l = searchEngineService.hdfsFindTask(searchParametersDTO);
        return ResponseEntity.ok().body(l);
    }

    /**
     * PUT  /samples : Updates an existing sample.
     *
     * @param sampleDTO the sampleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sampleDTO,
     * or with status 400 (Bad Request) if the sampleDTO is not valid,
     * or with status 500 (Internal Server Error) if the sampleDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/samples")
    public ResponseEntity<SampleDTO> updateSample(@Valid @RequestBody SampleDTO sampleDTO) throws URISyntaxException {
        log.debug("REST request to update Sample : {}", sampleDTO);
        if (sampleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SampleDTO result = sampleService.save(sampleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sampleDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /samples : get all the samples.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of samples in body
     */
    @GetMapping("/samples")
    public ResponseEntity<List<SampleDTO>> getAllSamples(Pageable pageable) {
        log.debug("REST request to get a page of Samples");
        Page<SampleDTO> page = sampleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/samples");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /samples/:id : get the "id" sample.
     *
     * @param id the id of the sampleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sampleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/samples/{id}")
    public ResponseEntity<SampleDTO> getSample(@PathVariable String id) {
        log.debug("REST request to get Sample : {}", id);
        Optional<SampleDTO> sampleDTO = sampleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sampleDTO);
    }

    /**
     * DELETE  /samples/:id : delete the "id" sample.
     *
     * @param id the id of the sampleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/samples/{id}")
    public ResponseEntity<Void> deleteSample(@PathVariable String id) {
        log.debug("REST request to delete Sample : {}", id);
        sampleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
