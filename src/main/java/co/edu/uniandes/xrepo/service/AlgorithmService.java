package co.edu.uniandes.xrepo.service;

import co.edu.uniandes.xrepo.domain.Algorithm;
import co.edu.uniandes.xrepo.repository.AlgorithmRepository;
import co.edu.uniandes.xrepo.service.dto.AlgorithmDTO;
import co.edu.uniandes.xrepo.service.mapper.AlgorithmMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Service Implementation for managing Algorithm.
 */
@Service
public class AlgorithmService {

    private final Logger log = LoggerFactory.getLogger(AlgorithmService.class);

    private final AlgorithmRepository algorithmRepository;

    private final AlgorithmMapper algorithmMapper;

    private final String tempAlgorithmLocation;
    private final String algorithmHdfsLocation;

    public AlgorithmService(AlgorithmRepository algorithmRepository, AlgorithmMapper algorithmMapper,
                            @Value("${xrepo.temp-algorithm-files-location}") String tempAlgorithmLocation,
                            @Value("${xrepo.algorithm-hdfs-location}") String algorithmHdfsLocation) {
        this.algorithmRepository = algorithmRepository;
        this.algorithmMapper = algorithmMapper;
        this.tempAlgorithmLocation = tempAlgorithmLocation;
        this.algorithmHdfsLocation = algorithmHdfsLocation;
    }

    /**
     * Save a algorithm.
     *
     * @param algorithmDTO the entity to save
     * @return the persisted entity
     */
    public AlgorithmDTO save(AlgorithmDTO algorithmDTO) {
        log.debug("Request to save Algorithm : {}", algorithmDTO);
        Algorithm algorithm = algorithmMapper.toEntity(algorithmDTO);
        algorithm = algorithmRepository.save(algorithm);
        //proceed to create mapper and reducer files
        String folderLocation = null;
        try {
            folderLocation = writeAlgorithmFile("mapper.py", algorithm.getId(), algorithmDTO.getMapperText());
            folderLocation = writeAlgorithmFile("reducer.py",algorithm.getId(), algorithmDTO.getReducerText());
        } catch (IOException e) {
            log.error("Unexpected error handling report file", e);
            e.printStackTrace();
        }
        //send files to HDFS server
        sendAlgoritm(folderLocation);
        //save the location on the database
        algorithm.setMapperFileUrl(algorithmHdfsLocation + algorithm.getId() + "/" + "mapper.py");
        algorithm.setReducerFileUrl(algorithmHdfsLocation + algorithm.getId() + "/" + "reducer.py");
        algorithm = algorithmRepository.save(algorithm);
        return algorithmMapper.toDto(algorithm);
    }

    /**
     * Get all the algorithms.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public Page<AlgorithmDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Algorithms");
        return algorithmRepository.findAll(pageable)
            .map(algorithmMapper::toDto);
    }


    /**
     * Get one algorithm by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<AlgorithmDTO> findOne(String id) {
        log.debug("Request to get Algorithm : {}", id);
        return algorithmRepository.findById(id)
            .map(algorithmMapper::toDto);
    }

    /**
     * Delete the algorithm by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Algorithm : {}", id);
        algorithmRepository.deleteById(id);
    }

    private String writeAlgorithmFile(String fileName, String algorithmID, String fileContents)  throws IOException {
        log.info("Request to create Algorithm File: {}", fileName);
        Path path = Paths.get(tempAlgorithmLocation, algorithmID, fileName);
        File file = path.toFile();
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            writer.write(fileContents);
            log.info("local algorithm generated succesfully", file.getName());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return file.getParentFile().getAbsolutePath();
    }

    private void sendAlgoritm(String localPath){
        log.info("trying to send file using scp");
        String shellScript = null;
        try {
            /*local path already contains the algorithm ID in the folder structure*/
            shellScript = new ClassPathResource("mapreduce-files/scpSendFiles.sh").getURI().getPath();
            Process console = Runtime.getRuntime().exec(new String[]{shellScript
                ,"-l",localPath
                ,"-r",algorithmHdfsLocation});
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
