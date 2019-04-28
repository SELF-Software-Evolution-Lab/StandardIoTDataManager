package co.edu.uniandes.xrepo.web.rest;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import co.edu.uniandes.xrepo.domain.BatchTask;
import co.edu.uniandes.xrepo.domain.enumeration.TaskState;
import co.edu.uniandes.xrepo.domain.enumeration.TaskType;
import co.edu.uniandes.xrepo.service.BatchTaskService;
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

    public SamplesFilesResource(BatchTaskService batchTaskService) {
        this.batchTaskService = batchTaskService;
    }

    @PostMapping("/samples-files-2")
    public ResponseEntity<Void> createSamplesFiles2(@RequestPart MultipartFile file)
            throws URISyntaxException {

        File fileTemp = new File(file.getOriginalFilename());
        log.debug("REST request to save archivo : {};{};{};{}", file.getName(), file.getOriginalFilename(),
                file.getSize(), fileTemp.getName());

        try {

            file.transferTo(new File("C:\\AJAR\\SamplesFiles\\" + fileTemp.getName()));

            BatchTask tarea = new BatchTask();
            tarea.progress(0);
            tarea.setState(TaskState.PENDING);
            tarea.setType(TaskType.FILE_LOAD);
            tarea.setDescription("Process File " + fileTemp.getName());
            tarea.setCreateDate(Instant.now());
            
            Map<String, String> parameters = new HashMap<String,String>();
            parameters.put("filePath", "C:\\AJAR\\SamplesFiles\\" + fileTemp.getName());
            parameters.put("fileSize", String.valueOf(file.getSize()));

            tarea.objectToParameters(parameters);
            tarea = batchTaskService.save(tarea);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, tarea.getId())).build();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(500).build();
    }
}
