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

import co.edu.uniandes.xrepo.service.TargetSystemService;
import co.edu.uniandes.xrepo.service.dto.TargetSystemDTO;
import co.edu.uniandes.xrepo.web.rest.errors.BadRequestAlertException;
import co.edu.uniandes.xrepo.web.rest.util.HeaderUtil;
import co.edu.uniandes.xrepo.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing TargetSystem.
 */
@RestController
@RequestMapping("/api")
public class TargetSystemResource {

    private final Logger log = LoggerFactory.getLogger(TargetSystemResource.class);

    private static final String ENTITY_NAME = "targetSystem";

    private final TargetSystemService targetSystemService;

    public TargetSystemResource(TargetSystemService targetSystemService) {
        this.targetSystemService = targetSystemService;
    }

    /**
     * POST  /target-systems : Create a new targetSystem.
     *
     * @param targetSystemDTO the targetSystemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new targetSystemDTO, or with status 400 (Bad Request) if the targetSystem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/target-systems")
    public ResponseEntity<TargetSystemDTO> createTargetSystem(@Valid @RequestBody TargetSystemDTO targetSystemDTO) throws URISyntaxException {
        log.debug("REST request to save TargetSystem : {}", targetSystemDTO);
        if (targetSystemDTO.getId() != null) {
            throw new BadRequestAlertException("A new targetSystem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TargetSystemDTO result = targetSystemService.save(targetSystemDTO);
        return ResponseEntity.created(new URI("/api/target-systems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /target-systems : Updates an existing targetSystem.
     *
     * @param targetSystemDTO the targetSystemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated targetSystemDTO,
     * or with status 400 (Bad Request) if the targetSystemDTO is not valid,
     * or with status 500 (Internal Server Error) if the targetSystemDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/target-systems")
    public ResponseEntity<TargetSystemDTO> updateTargetSystem(@Valid @RequestBody TargetSystemDTO targetSystemDTO) throws URISyntaxException {
        log.debug("REST request to update TargetSystem : {}", targetSystemDTO);
        if (targetSystemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TargetSystemDTO result = targetSystemService.save(targetSystemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, targetSystemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /target-systems : get all the targetSystems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of targetSystems in body
     */
    @GetMapping("/target-systems")
    public ResponseEntity<List<TargetSystemDTO>> getAllTargetSystems(Pageable pageable) {
        log.debug("REST request to get a page of TargetSystems");
        Page<TargetSystemDTO> page = targetSystemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/target-systems");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /target-systems/:id : get the "id" targetSystem.
     *
     * @param id the id of the targetSystemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the targetSystemDTO, or with status 404 (Not Found)
     */
    @GetMapping("/target-systems/{id}")
    public ResponseEntity<TargetSystemDTO> getTargetSystem(@PathVariable String id) {
        log.debug("REST request to get TargetSystem : {}", id);
        Optional<TargetSystemDTO> targetSystemDTO = targetSystemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(targetSystemDTO);
    }

    /**
     * DELETE  /target-systems/:id : delete the "id" targetSystem.
     *
     * @param id the id of the targetSystemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/target-systems/{id}")
    public ResponseEntity<Void> deleteTargetSystem(@PathVariable String id) {
        log.debug("REST request to delete TargetSystem : {}", id);
        targetSystemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
