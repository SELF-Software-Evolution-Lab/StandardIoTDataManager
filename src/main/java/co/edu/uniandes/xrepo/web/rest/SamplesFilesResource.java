package co.edu.uniandes.xrepo.web.rest;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
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
import co.edu.uniandes.xrepo.service.SamplingService;
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

    private final SamplingService samplingService;

    private final String samplesFilesLocation;

    private final String removePrefixHdfs;

    public SamplesFilesResource(BatchTaskService batchTaskService,
                                SamplingService samplingService,
                                @Value("${xrepo.samples-files-location}") String samplesFilesLocation,
                                @Value("${xrepo.remove-prefix-hdfs}") String removePrefixHdfs) {
        this.batchTaskService = batchTaskService;
        this.samplingService = samplingService;
        this.samplesFilesLocation = samplesFilesLocation;
        this.removePrefixHdfs = removePrefixHdfs;
    }

    @PostMapping("/samples-files-2")
    public ResponseEntity<Void> createSamplesFiles2(@RequestPart("file") MultipartFile file, @RequestPart(value = "samplingId", required = false) String samplingId) {

        BatchTask tarea = new BatchTask();
        tarea.progress(0);
        tarea.setStartDate(Instant.now());
        File fileTemp = new File(file.getOriginalFilename());

        log.debug("REST request to save archivo : {};{};{};{}", file.getName(), file.getOriginalFilename(),
                file.getSize(), fileTemp.getName());

        String nombreArchivo = samplingService.getFileName( file, samplingId);

        try {

            //SFTPClient sftpClient = new SFTPClient( filePem , "andes", "andes", this.serverMongo );

            //System.out.println("sftpClient "+sftpClient);
            long fileSize = file.getSize();
            Path filePath = Paths.get(samplesFilesLocation, nombreArchivo);

            File archivo = filePath.toFile();
            archivo.setReadable(true, false);
            archivo.setWritable(true, false);

            file.transferTo(archivo);

            archivo.setReadable(true, false);
            archivo.setWritable(true, false);

            SamplesFilesParametersDTO parameters = new SamplesFilesParametersDTO();

            parameters.setFilePath(filePath.toString());
            parameters.setFileSize(fileSize);

            //Actualizar filesUrls de sampling
            String[] array = nombreArchivo.split("_");

            String uriFile = filePath.toString().replace(removePrefixHdfs,"");
            samplingService.addFileUriToSampling(array[0], uriFile);

            tarea.progress(100);
            tarea.setState(TaskState.COMPLETED);
            tarea.setType(TaskType.FILE_LOAD);
            tarea.setDescription("Process File " + fileTemp.getName());
            tarea.setCreateDate(Instant.now());
            tarea.setUser(SecurityUtils.getCurrentUserLogin().get());

            tarea.objectToParameters(parameters);
            tarea.setEndDate(Instant.now());
            tarea = batchTaskService.save(tarea);

            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, tarea.getId())).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }

    }
}
