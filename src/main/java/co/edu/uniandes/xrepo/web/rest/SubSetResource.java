package co.edu.uniandes.xrepo.web.rest;
import co.edu.uniandes.xrepo.service.SubSetService;
import co.edu.uniandes.xrepo.web.rest.errors.BadRequestAlertException;
import co.edu.uniandes.xrepo.web.rest.util.HeaderUtil;
import co.edu.uniandes.xrepo.web.rest.util.PaginationUtil;
import co.edu.uniandes.xrepo.service.dto.SubSetDTO;
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
 * REST controller for managing SubSet.
 */
@RestController
@RequestMapping("/api")
public class SubSetResource {

    private final Logger log = LoggerFactory.getLogger(SubSetResource.class);

    private static final String ENTITY_NAME = "subSet";

    private final SubSetService subSetService;

    public SubSetResource(SubSetService subSetService) {
        this.subSetService = subSetService;
    }

    /**
     * POST  /sub-sets : Create a new subSet.
     *
     * @param subSetDTO the subSetDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new subSetDTO, or with status 400 (Bad Request) if the subSet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sub-sets")
    public ResponseEntity<SubSetDTO> createSubSet(@Valid @RequestBody SubSetDTO subSetDTO) throws URISyntaxException {
        log.debug("REST request to save SubSet : {}", subSetDTO);
        if (subSetDTO.getId() != null) {
            throw new BadRequestAlertException("A new subSet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubSetDTO result = subSetService.save(subSetDTO);
        return ResponseEntity.created(new URI("/api/sub-sets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sub-sets : Updates an existing subSet.
     *
     * @param subSetDTO the subSetDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated subSetDTO,
     * or with status 400 (Bad Request) if the subSetDTO is not valid,
     * or with status 500 (Internal Server Error) if the subSetDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sub-sets")
    public ResponseEntity<SubSetDTO> updateSubSet(@Valid @RequestBody SubSetDTO subSetDTO) throws URISyntaxException {
        log.debug("REST request to update SubSet : {}", subSetDTO);
        if (subSetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SubSetDTO result = subSetService.save(subSetDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, subSetDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sub-sets : get all the subSets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of subSets in body
     */
    @GetMapping("/sub-sets")
    public ResponseEntity<List<SubSetDTO>> getAllSubSets(Pageable pageable) {
        log.debug("REST request to get a page of SubSets");
        Page<SubSetDTO> page = subSetService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sub-sets");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /sub-sets/:id : get the "id" subSet.
     *
     * @param id the id of the subSetDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the subSetDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sub-sets/{id}")
    public ResponseEntity<SubSetDTO> getSubSet(@PathVariable String id) {
        log.debug("REST request to get SubSet : {}", id);
        Optional<SubSetDTO> subSetDTO = subSetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subSetDTO);
    }

    /**
     * DELETE  /sub-sets/:id : delete the "id" subSet.
     *
     * @param id the id of the subSetDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sub-sets/{id}")
    public ResponseEntity<Void> deleteSubSet(@PathVariable String id) {
        log.debug("REST request to delete SubSet : {}", id);
        subSetService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
