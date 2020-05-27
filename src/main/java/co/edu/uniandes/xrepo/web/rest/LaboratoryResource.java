package co.edu.uniandes.xrepo.web.rest;
import co.edu.uniandes.xrepo.service.LaboratoryService;
import co.edu.uniandes.xrepo.web.rest.errors.BadRequestAlertException;
import co.edu.uniandes.xrepo.web.rest.util.HeaderUtil;
import co.edu.uniandes.xrepo.web.rest.util.PaginationUtil;
import co.edu.uniandes.xrepo.service.dto.LaboratoryDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Laboratory.
 */
@RestController
@RequestMapping("/api")
public class LaboratoryResource {

    private final Logger log = LoggerFactory.getLogger(LaboratoryResource.class);

    private static final String ENTITY_NAME = "laboratory";

    private final LaboratoryService laboratoryService;

    public LaboratoryResource(LaboratoryService laboratoryService) {
        this.laboratoryService = laboratoryService;
    }

    /**
     * POST  /laboratories : Create a new laboratory.
     *
     * @param laboratoryDTO the laboratoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new laboratoryDTO, or with status 400 (Bad Request) if the laboratory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/laboratories")
    public ResponseEntity<LaboratoryDTO> createLaboratory(@Valid @RequestBody LaboratoryDTO laboratoryDTO) throws URISyntaxException {
        log.debug("REST request to save Laboratory : {}", laboratoryDTO);
        if (laboratoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new laboratory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LaboratoryDTO result = laboratoryService.save(laboratoryDTO);
        return ResponseEntity.created(new URI("/api/laboratories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /laboratories : Updates an existing laboratory.
     *
     * @param laboratoryDTO the laboratoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated laboratoryDTO,
     * or with status 400 (Bad Request) if the laboratoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the laboratoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/laboratories")
    public ResponseEntity<LaboratoryDTO> updateLaboratory(@Valid @RequestBody LaboratoryDTO laboratoryDTO) throws URISyntaxException {
        log.debug("REST request to update Laboratory : {}", laboratoryDTO);
        if (laboratoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LaboratoryDTO result = laboratoryService.save(laboratoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, laboratoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /laboratories : get all the laboratories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of laboratories in body
     */
    @GetMapping("/laboratories")
    public ResponseEntity<List<LaboratoryDTO>> getAllLaboratories(Pageable pageable) {
        log.debug("REST request to get a page of Laboratories");
        Page<LaboratoryDTO> page = laboratoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/laboratories");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /laboratories/:id : get the "id" laboratory.
     *
     * @param id the id of the laboratoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the laboratoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/laboratories/{id}")
    public ResponseEntity<LaboratoryDTO> getLaboratory(@PathVariable String id) {
        log.debug("REST request to get Laboratory : {}", id);
        Optional<LaboratoryDTO> laboratoryDTO = laboratoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(laboratoryDTO);
    }

    /**
     * DELETE  /laboratories/:id : delete the "id" laboratory.
     *
     * @param id the id of the laboratoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/laboratories/{id}")
    public ResponseEntity<Void> deleteLaboratory(@PathVariable String id) {
        log.debug("REST request to delete Laboratory : {}", id);
        laboratoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
