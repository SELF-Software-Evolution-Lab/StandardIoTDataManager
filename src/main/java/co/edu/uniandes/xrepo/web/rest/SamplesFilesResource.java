package co.edu.uniandes.xrepo.web.rest;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import co.edu.uniandes.xrepo.domain.BatchTask;
import co.edu.uniandes.xrepo.domain.enumeration.TaskState;
import co.edu.uniandes.xrepo.domain.enumeration.TaskType;
import co.edu.uniandes.xrepo.security.SecurityUtils;
import co.edu.uniandes.xrepo.service.BatchTaskService;
import co.edu.uniandes.xrepo.service.dto.SamplesFilesParametersDTO;
import co.edu.uniandes.xrepo.web.rest.util.HeaderUtil;

/**
 * REST controller for managing SamplesFiles.
 */
@RestController
@RequestMapping("/api")
public class SamplesFilesResource {

    private final Logger log = LoggerFactory.getLogger(SamplesFilesResource.class);

    private static final String ENTITY_NAME = "samplesFiles";

    private final BatchTaskService batchTaskService;

    private final String samplesFilesLocation; 

    public SamplesFilesResource(BatchTaskService batchTaskService, @Value("${xrepo.samples-files-location}") String samplesFilesLocation) {
        this.batchTaskService = batchTaskService;
        this.samplesFilesLocation = samplesFilesLocation;
    }

    @PostMapping("/samples-files-2")
    public ResponseEntity<Void> createSamplesFiles2(@RequestPart MultipartFile file) {

        File fileTemp = new File(file.getOriginalFilename());
        log.debug("REST request to save archivo : {};{};{};{}", file.getName(), file.getOriginalFilename(),
                file.getSize(), fileTemp.getName());

        try {
            long fileSize = file.getSize();
            Path filePath = Paths.get(samplesFilesLocation, fileTemp.getName());
            File archivo = filePath.toFile();
            archivo.setReadable(true, false); 
            archivo.setWritable(true, false); 
            file.transferTo(archivo);
            archivo.setReadable(true, false); 
            archivo.setWritable(true, false); 
            BatchTask tarea = new BatchTask();
            tarea.progress(0);
            tarea.setState(TaskState.PENDING);
            tarea.setType(TaskType.FILE_LOAD);
            tarea.setDescription("Process File " + fileTemp.getName());
            tarea.setCreateDate(Instant.now());
            tarea.setUser(SecurityUtils.getCurrentUserLogin().get());
            
            SamplesFilesParametersDTO parameters = new SamplesFilesParametersDTO();

            parameters.setFilePath(filePath.toString());
            parameters.setFileSize(fileSize);

            tarea.objectToParameters(parameters);
            tarea = batchTaskService.save(tarea);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, tarea.getId())).build();
        } catch (IllegalStateException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }


        return ResponseEntity.status(500).build();
    }
}
