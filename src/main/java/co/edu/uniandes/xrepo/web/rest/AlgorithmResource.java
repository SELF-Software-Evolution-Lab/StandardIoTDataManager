package co.edu.uniandes.xrepo.web.rest;
import co.edu.uniandes.xrepo.service.AlgorithmService;
import co.edu.uniandes.xrepo.service.SearchEngineService;
import co.edu.uniandes.xrepo.service.dto.SampleSearchParametersDTO;
import co.edu.uniandes.xrepo.web.rest.errors.BadRequestAlertException;
import co.edu.uniandes.xrepo.web.rest.util.HeaderUtil;
import co.edu.uniandes.xrepo.web.rest.util.PaginationUtil;
import co.edu.uniandes.xrepo.service.dto.AlgorithmDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Algorithm.
 */
@RestController
@RequestMapping("/api")
public class AlgorithmResource {

    private final Logger log = LoggerFactory.getLogger(AlgorithmResource.class);

    private static final String ENTITY_NAME = "algorithm";

    private final AlgorithmService algorithmService;
    private final SearchEngineService searchEngineService;

    public AlgorithmResource(AlgorithmService algorithmService, SearchEngineService searchEngineService) {
        this.algorithmService = algorithmService;
        this.searchEngineService = searchEngineService;
    }

    /**
     * POST  /algorithms : Create a new algorithm.
     *
     * @param algorithmDTO the algorithmDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new algorithmDTO, or with status 400 (Bad Request) if the algorithm has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/algorithms")
    public ResponseEntity<AlgorithmDTO> createAlgorithm(@Valid @RequestBody AlgorithmDTO algorithmDTO) throws URISyntaxException {
        log.debug("REST request to save Algorithm : {}", algorithmDTO);
        if (algorithmDTO.getId() != null) {
            throw new BadRequestAlertException("A new algorithm cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AlgorithmDTO result = algorithmService.save(algorithmDTO);
        return ResponseEntity.created(new URI("/api/algorithms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /algorithms : Updates an existing algorithm.
     *
     * @param algorithmDTO the algorithmDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated algorithmDTO,
     * or with status 400 (Bad Request) if the algorithmDTO is not valid,
     * or with status 500 (Internal Server Error) if the algorithmDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/algorithms")
    public ResponseEntity<AlgorithmDTO> updateAlgorithm(@Valid @RequestBody AlgorithmDTO algorithmDTO) throws URISyntaxException {
        log.debug("REST request to update Algorithm : {}", algorithmDTO);
        if (algorithmDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AlgorithmDTO result = algorithmService.save(algorithmDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, algorithmDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /algorithms : get all the algorithms.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of algorithms in body
     */
    @GetMapping("/algorithms")
    public ResponseEntity<List<AlgorithmDTO>> getAllAlgorithms(Pageable pageable) {
        log.debug("REST request to get a page of Algorithms");
        Page<AlgorithmDTO> page = algorithmService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/algorithms");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /algorithms/:id : get the "id" algorithm.
     *
     * @param id the id of the algorithmDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the algorithmDTO, or with status 404 (Not Found)
     */
    @GetMapping("/algorithms/{id}")
    public ResponseEntity<AlgorithmDTO> getAlgorithm(@PathVariable String id) {
        log.debug("REST request to get Algorithm : {}", id);
        Optional<AlgorithmDTO> algorithmDTO = algorithmService.findOne(id);
        return ResponseUtil.wrapOrNotFound(algorithmDTO);
    }

    @GetMapping("/algorithms/runhdfs/{id}")
    public ResponseEntity<AlgorithmDTO> runHdfsAlgorithm(@PathVariable String id) throws IOException {
        Optional<AlgorithmDTO> algorithmDTO = algorithmService.findOne(id);
        SearchEngineService.SearchResponse l = null;
        if (algorithmDTO.isPresent()) {
            l = searchEngineService.hdfsRunMapReduceTask(algorithmDTO.get());
        }
        return ResponseUtil.wrapOrNotFound(algorithmDTO);
    }

    /**
     * DELETE  /algorithms/:id : delete the "id" algorithm.
     *
     * @param id the id of the algorithmDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/algorithms/{id}")
    public ResponseEntity<Void> deleteAlgorithm(@PathVariable String id) {
        log.debug("REST request to delete Algorithm : {}", id);
        algorithmService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
