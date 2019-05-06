package co.edu.uniandes.xrepo.service;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import co.edu.uniandes.xrepo.domain.BatchTask;
import co.edu.uniandes.xrepo.domain.enumeration.TaskState;
import co.edu.uniandes.xrepo.domain.enumeration.TaskType;
import co.edu.uniandes.xrepo.repository.BatchTaskRepository;
import co.edu.uniandes.xrepo.security.SecurityUtils;
import co.edu.uniandes.xrepo.service.reports.SearchReportFileLocator;

/**
 * Service Implementation for managing BatchTask.
 */
@Service
public class BatchTaskService {

    private final Logger log = LoggerFactory.getLogger(BatchTaskService.class);

    private final BatchTaskRepository batchTaskRepository;
    private final SearchReportFileLocator reportFileLocator;

    public BatchTaskService(BatchTaskRepository batchTaskRepository, @Lazy SearchReportFileLocator reportFileLocator) {
        this.batchTaskRepository = batchTaskRepository;
        this.reportFileLocator = reportFileLocator;
    }

    /**
     * Save a batchTask.
     *
     * @param batchTask the entity to save
     * @return the persisted entity
     */
    public BatchTask save(BatchTask batchTask) {
        log.debug("Request to save BatchTask : {}", batchTask);
        return batchTaskRepository.save(batchTask);
    }

    /**
     * Get all the batchTasks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public Page<BatchTask> findAll(Pageable pageable) {
        log.debug("Request to get all BatchTasks");
        return batchTaskRepository.findAll(pageable);
    }


    /**
     * Get one batchTask by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<BatchTask> findOne(String id) {
        log.debug("Request to get BatchTask : {}", id);
        return batchTaskRepository.findById(id);
    }

    /**
     * Delete the batchTask by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete BatchTask : {}", id);
        batchTaskRepository.deleteById(id);
    }

    /**
     * Get all the batchTasks whit state PENDING.
     *
     * @return the list of entities
     */
    public List<BatchTask> findAllPending() {
        log.debug("Request to get all pending BatchTasks");
        return batchTaskRepository.findByState(TaskState.PENDING);
    }

    public Page<BatchTask> findAllSearchReportsByUser(Pageable pageable) {
        return batchTaskRepository.findByTypeAndUser(TaskType.REPORT, currentUser(), pageable);
    }

    public Page<BatchTask> findAllUploadFilesByUser(Pageable pageable) {
        return batchTaskRepository.findByTypeAndUser(TaskType.FILE_LOAD, currentUser(), pageable);
    }

    public File fileFromReport(String id) {
        Optional<BatchTask> byId = batchTaskRepository.findById(id);
        BatchTask batchTask = byId.orElseThrow(() -> new NoSuchElementException("Report for id " + id + " wasn't found"));
        return reportFileLocator.locateReportFile(batchTask);
    }

    private String currentUser() {
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if (!currentUserLogin.isPresent()) {
            throw new AccessDeniedException("Current User not found");
        }
        return currentUserLogin.get();
    }
}
