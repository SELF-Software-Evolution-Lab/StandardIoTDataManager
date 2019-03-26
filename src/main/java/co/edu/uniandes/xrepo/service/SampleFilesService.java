package co.edu.uniandes.xrepo.service;

import co.edu.uniandes.xrepo.domain.SampleFiles;
import co.edu.uniandes.xrepo.repository.SampleFilesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing SampleFiles.
 */
@Service
public class SampleFilesService {

    private final Logger log = LoggerFactory.getLogger(SampleFilesService.class);

    private final SampleFilesRepository sampleFilesRepository;

    public SampleFilesService(SampleFilesRepository sampleFilesRepository) {
        this.sampleFilesRepository = sampleFilesRepository;
    }

    /**
     * Save a sampleFiles.
     *
     * @param sampleFiles the entity to save
     * @return the persisted entity
     */
    public SampleFiles save(SampleFiles sampleFiles) {
        log.debug("Request to save SampleFiles : {}", sampleFiles);
       ;
        Path path = Paths.get(String.format("c:\\AJAR\\%s.%s",sampleFiles.getName(), sampleFiles.getPathContentType().split("/")[1]));
        try {
            Files.write(path, sampleFiles.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("******************************Finalizo***********************************");

        sampleFiles.setPath(new byte[]{});
        
        return sampleFilesRepository.save(sampleFiles);
    }

    /**
     * Get all the sampleFiles.
     *
     * @return the list of entities
     */
    public List<SampleFiles> findAll() {
        log.debug("Request to get all SampleFiles");
        return sampleFilesRepository.findAll();
    }


    /**
     * Get one sampleFiles by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<SampleFiles> findOne(String id) {
        log.debug("Request to get SampleFiles : {}", id);
        return sampleFilesRepository.findById(id);
    }

    /**
     * Delete the sampleFiles by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete SampleFiles : {}", id);
        sampleFilesRepository.deleteById(id);
    }
}
