package co.edu.uniandes.xrepo.service;

import co.edu.uniandes.xrepo.domain.SamplesFiles;
import co.edu.uniandes.xrepo.repository.SamplesFilesRepository;
import co.edu.uniandes.xrepo.service.dto.SamplesFilesDTO;
import co.edu.uniandes.xrepo.service.mapper.SamplesFilesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing SamplesFiles.
 */
@Service
public class SamplesFilesService {

    private final Logger log = LoggerFactory.getLogger(SamplesFilesService.class);

    private final SamplesFilesRepository samplesFilesRepository;

    private final SamplesFilesMapper samplesFilesMapper;

    public SamplesFilesService(SamplesFilesRepository samplesFilesRepository, SamplesFilesMapper samplesFilesMapper) {
        this.samplesFilesRepository = samplesFilesRepository;
        this.samplesFilesMapper = samplesFilesMapper;
    }

    /**
     * Save a samplesFiles.
     *
     * @param samplesFilesDTO the entity to save
     * @return the persisted entity
     */
    public SamplesFilesDTO save(SamplesFilesDTO samplesFilesDTO) {
        log.debug("Request to save SamplesFiles : {}", samplesFilesDTO);
        SamplesFiles samplesFiles = samplesFilesMapper.toEntity(samplesFilesDTO);
        samplesFiles = samplesFilesRepository.save(samplesFiles);
        return samplesFilesMapper.toDto(samplesFiles);
    }

    /**
     * Get all the samplesFiles.
     *
     * @return the list of entities
     */
    public List<SamplesFilesDTO> findAll() {
        log.debug("Request to get all SamplesFiles");
        return samplesFilesRepository.findAll().stream()
            .map(samplesFilesMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one samplesFiles by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<SamplesFilesDTO> findOne(String id) {
        log.debug("Request to get SamplesFiles : {}", id);
        return samplesFilesRepository.findById(id)
            .map(samplesFilesMapper::toDto);
    }

    /**
     * Delete the samplesFiles by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete SamplesFiles : {}", id);
        samplesFilesRepository.deleteById(id);
    }
}
