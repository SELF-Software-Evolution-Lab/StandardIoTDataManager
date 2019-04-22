package co.edu.uniandes.xrepo.web.rest;

import co.edu.uniandes.xrepo.service.SamplesFilesService;
import co.edu.uniandes.xrepo.web.rest.errors.BadRequestAlertException;
import co.edu.uniandes.xrepo.web.rest.util.HeaderUtil;
import co.edu.uniandes.xrepo.service.dto.SamplesFilesDTO;
import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * REST controller for managing SamplesFiles.
 */
@RestController
@RequestMapping("/api")
public class SamplesFilesResource {

    private final Logger log = LoggerFactory.getLogger(SamplesFilesResource.class);

    private static final String ENTITY_NAME = "samplesFiles";

    private final SamplesFilesService samplesFilesService;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job processJob;

    public SamplesFilesResource(SamplesFilesService samplesFilesService) {
        this.samplesFilesService = samplesFilesService;
    }

    /**
     * POST /samples-files : Create a new samplesFiles.
     *
     * @param samplesFilesDTO the samplesFilesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new
     *         samplesFilesDTO, or with status 400 (Bad Request) if the samplesFiles
     *         has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/samples-files")
    public ResponseEntity<SamplesFilesDTO> createSamplesFiles(@RequestBody SamplesFilesDTO samplesFilesDTO)
            throws URISyntaxException {
        log.debug("REST request to save SamplesFiles : {}", samplesFilesDTO);
        if (samplesFilesDTO.getId() != null) {
            throw new BadRequestAlertException("A new samplesFiles cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SamplesFilesDTO result = samplesFilesService.save(samplesFilesDTO);
        return ResponseEntity.created(new URI("/api/samples-files/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * POST /samples-files : Create a new samplesFiles.
     *
     * @param samplesFilesDTO the samplesFilesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new
     *         samplesFilesDTO, or with status 400 (Bad Request) if the samplesFiles
     *         has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/samples-files-2")
    public ResponseEntity<SamplesFilesDTO> createSamplesFiles2(@RequestPart MultipartFile file)
            throws URISyntaxException {

        File fileTemp = new File(file.getOriginalFilename());
        log.debug("REST request to save archivo : {};{};{};{}", file.getName(), file.getOriginalFilename(),
                file.getSize(), fileTemp.getName());

        SamplesFilesDTO result = new SamplesFilesDTO();
        try {

            file.transferTo(new File("C:\\AJAR\\SamplesFiles\\" + fileTemp.getName()));

            SamplesFilesDTO samplesFilesDTO2 = new SamplesFilesDTO();
            samplesFilesDTO2.setName(fileTemp.getName());
            samplesFilesDTO2.setContentType(file.getContentType());
            samplesFilesDTO2.setSize(BigDecimal.valueOf(file.getSize()));
            samplesFilesDTO2.setPath("C:\\AJAR\\SamplesFiles\\" + fileTemp.getName());
            samplesFilesDTO2.setCreateDateTime(LocalDate.now());
            samplesFilesDTO2.setState(1);
            samplesFilesDTO2.setResult("");

            result = samplesFilesService.save(samplesFilesDTO2);

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
        .addString("filePath", "C:\\AJAR\\SamplesFiles\\" + fileTemp.getName()).addString("SampleFileId", result.getId())
                .toJobParameters();
        try {
            jobLauncher.run(processJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ResponseEntity.created(new URI("/api/samples-files/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * PUT /samples-files : Updates an existing samplesFiles.
     *
     * @param samplesFilesDTO the samplesFilesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     *         samplesFilesDTO, or with status 400 (Bad Request) if the
     *         samplesFilesDTO is not valid, or with status 500 (Internal Server
     *         Error) if the samplesFilesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/samples-files")
    public ResponseEntity<SamplesFilesDTO> updateSamplesFiles(@RequestBody SamplesFilesDTO samplesFilesDTO)
            throws URISyntaxException {
        log.debug("REST request to update SamplesFiles : {}", samplesFilesDTO);
        if (samplesFilesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SamplesFilesDTO result = samplesFilesService.save(samplesFilesDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, samplesFilesDTO.getId().toString()))
                .body(result);
    }

    /**
     * GET /samples-files : get all the samplesFiles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of samplesFiles
     *         in body
     */
    @GetMapping("/samples-files")
    public List<SamplesFilesDTO> getAllSamplesFiles() {
        log.debug("REST request to get all SamplesFiles");
        return samplesFilesService.findAll();
    }

    /**
     * GET  /samples-files/:id : get the "id" samplesFiles.
     *
     * @param id the id of the samplesFilesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the samplesFilesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/samples-files/{id}")
    public ResponseEntity<SamplesFilesDTO> getSamplesFiles(@PathVariable String id) {
        log.debug("REST request to get SamplesFiles : {}", id);
        Optional<SamplesFilesDTO> samplesFilesDTO = samplesFilesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(samplesFilesDTO);
    }

    /**
     * DELETE  /samples-files/:id : delete the "id" samplesFiles.
     *
     * @param id the id of the samplesFilesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/samples-files/{id}")
    public ResponseEntity<Void> deleteSamplesFiles(@PathVariable String id) {
        log.debug("REST request to delete SamplesFiles : {}", id);
        samplesFilesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
