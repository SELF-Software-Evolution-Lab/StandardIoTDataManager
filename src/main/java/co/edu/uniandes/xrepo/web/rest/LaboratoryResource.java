package co.edu.uniandes.xrepo.web.rest;
import co.edu.uniandes.xrepo.service.LaboratoryService;
import co.edu.uniandes.xrepo.web.rest.errors.BadRequestAlertException;
import co.edu.uniandes.xrepo.web.rest.util.HeaderUtil;
import co.edu.uniandes.xrepo.web.rest.util.PaginationUtil;
import co.edu.uniandes.xrepo.service.dto.LaboratoryDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

/**
 * REST controller for managing Laboratory.
 */
@RestController
@RequestMapping("/api")
public class LaboratoryResource {

    private final Logger log = LoggerFactory.getLogger(LaboratoryResource.class);

    private static final String ENTITY_NAME = "laboratory";

    private final LaboratoryService laboratoryService;
    private final String hdfsResultsLocation;
    private final String nfsGatewayMountPoint;

    public LaboratoryResource(LaboratoryService laboratoryService
        , @Value("${xrepo.mr-results-hdfs-location}") String hdfsResultsLocation
        , @Value("${xrepo.remove-prefix-hdfs}") String nfsGatewayMountPoint) {
        this.laboratoryService = laboratoryService;
        this.hdfsResultsLocation = hdfsResultsLocation;
        this.nfsGatewayMountPoint = nfsGatewayMountPoint;
    }

    /**
     * POST  /laboratories : Create a new laboratory.
     *
     * @param laboratoryDTO the laboratoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new laboratoryDTO, or with status 400 (Bad Request) if the laboratory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/laboratories")
    public ResponseEntity<LaboratoryDTO> createLaboratory(@Valid @RequestBody LaboratoryDTO laboratoryDTO, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save Laboratory : {}", laboratoryDTO);
        if (laboratoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new laboratory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LaboratoryDTO result = laboratoryService.save(laboratoryDTO, request.getLocalName());
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
    public ResponseEntity<LaboratoryDTO> updateLaboratory(@Valid @RequestBody LaboratoryDTO laboratoryDTO, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to update Laboratory : {}", laboratoryDTO);
        if (laboratoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LaboratoryDTO result = laboratoryService.save(laboratoryDTO, request.getRequestURI());
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

    @GetMapping("/laboratories/files/{id}")
    public ResponseEntity<List<String>> getLaboratorySharedFiles(@PathVariable String id) {
        log.debug("REST request to get Laboratory shared files : {}", id);
        List<String> sharedFilesURL = laboratoryService.findAllSharedFiles(id);
        return ResponseEntity.ok().body(sharedFilesURL);
    }

    /*same as Get Lab authorized on anonymous connections*/
    @GetMapping("/laboratories/anonymous/{id}")
    public ResponseEntity<LaboratoryDTO> getAnonymousLaboratory(@PathVariable String id) {
        log.debug("REST request to get Laboratory : {}", id);
        Optional<LaboratoryDTO> laboratoryDTO = laboratoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(laboratoryDTO);
    }

    @GetMapping("/laboratories/files/download/{name}/{date}/{time}")
    public ResponseEntity<FileSystemResource> getFileFromHDFS(@PathVariable String name, @PathVariable String date, @PathVariable String time) throws IOException {
        log.debug("REST request to download Laboratory shared files : {}", name);
        Path path = Paths.get(nfsGatewayMountPoint + hdfsResultsLocation, name, date, time, "part-00000");
        File file = path.toFile();
        FileSystemResource fileSystemResource = new FileSystemResource(file);

        return ResponseEntity.ok()
            .contentLength(fileSystemResource.contentLength())
            .contentType(MediaType.parseMediaType("text/csv"))
            .header(CONTENT_DISPOSITION,"attachment; filename="+file.getName())
            .body(fileSystemResource);
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
