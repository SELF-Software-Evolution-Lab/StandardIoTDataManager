package co.edu.uniandes.xrepo.web.rest;
import co.edu.uniandes.xrepo.domain.BatchTask;
import co.edu.uniandes.xrepo.service.BatchTaskService;
import co.edu.uniandes.xrepo.web.rest.errors.BadRequestAlertException;
import co.edu.uniandes.xrepo.web.rest.util.HeaderUtil;
import co.edu.uniandes.xrepo.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing BatchTask.
 */
@RestController
@RequestMapping("/api")
public class BatchTaskResource {

    private final Logger log = LoggerFactory.getLogger(BatchTaskResource.class);

    private static final String ENTITY_NAME = "batchTask";

    private final BatchTaskService batchTaskService;

    public BatchTaskResource(BatchTaskService batchTaskService) {
        this.batchTaskService = batchTaskService;
    }

    /**
     * POST  /batch-tasks : Create a new batchTask.
     *
     * @param batchTask the batchTask to create
     * @return the ResponseEntity with status 201 (Created) and with body the new batchTask, or with status 400 (Bad Request) if the batchTask has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/batch-tasks")
    public ResponseEntity<BatchTask> createBatchTask(@RequestBody BatchTask batchTask) throws URISyntaxException {
        log.debug("REST request to save BatchTask : {}", batchTask);
        if (batchTask.getId() != null) {
            throw new BadRequestAlertException("A new batchTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BatchTask result = batchTaskService.save(batchTask);
        return ResponseEntity.created(new URI("/api/batch-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /batch-tasks : Updates an existing batchTask.
     *
     * @param batchTask the batchTask to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated batchTask,
     * or with status 400 (Bad Request) if the batchTask is not valid,
     * or with status 500 (Internal Server Error) if the batchTask couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/batch-tasks")
    public ResponseEntity<BatchTask> updateBatchTask(@RequestBody BatchTask batchTask) throws URISyntaxException {
        log.debug("REST request to update BatchTask : {}", batchTask);
        if (batchTask.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BatchTask result = batchTaskService.save(batchTask);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, batchTask.getId().toString()))
            .body(result);
    }

    /**
     * GET  /batch-tasks : get all the batchTasks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of batchTasks in body
     */
    @GetMapping("/batch-tasks")
    public ResponseEntity<List<BatchTask>> getAllBatchTasks(Pageable pageable) {
        log.debug("REST request to get a page of BatchTasks");
        Page<BatchTask> page = batchTaskService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/batch-tasks");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /batch-tasks/:id : get the "id" batchTask.
     *
     * @param id the id of the batchTask to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the batchTask, or with status 404 (Not Found)
     */
    @GetMapping("/batch-tasks/{id}")
    public ResponseEntity<BatchTask> getBatchTask(@PathVariable String id) {
        log.debug("REST request to get BatchTask : {}", id);
        Optional<BatchTask> batchTask = batchTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(batchTask);
    }

    /**
     * DELETE  /batch-tasks/:id : delete the "id" batchTask.
     *
     * @param id the id of the batchTask to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/batch-tasks/{id}")
    public ResponseEntity<Void> deleteBatchTask(@PathVariable String id) {
        log.debug("REST request to delete BatchTask : {}", id);
        batchTaskService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
