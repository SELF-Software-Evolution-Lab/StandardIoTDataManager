package co.edu.uniandes.xrepo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import co.edu.uniandes.xrepo.domain.Sampling;
import co.edu.uniandes.xrepo.domain.Sensor;
import co.edu.uniandes.xrepo.repository.SamplingRepository;
import co.edu.uniandes.xrepo.service.dto.SamplingDTO;
import co.edu.uniandes.xrepo.service.dto.SensorDTO;
import co.edu.uniandes.xrepo.service.mapper.SamplingMapper;

/**
 * Service Implementation for managing Sampling.
 */
@Service
public class SamplingService {

    private final Logger log = LoggerFactory.getLogger(SamplingService.class);

    private final SamplingRepository samplingRepository;

    private final SamplingMapper samplingMapper;

    public SamplingService(SamplingRepository samplingRepository, SamplingMapper samplingMapper) {
        this.samplingRepository = samplingRepository;
        this.samplingMapper = samplingMapper;
    }

    /**
     * Save a sampling.
     *
     * @param samplingDTO the entity to save
     * @return the persisted entity
     */
    public SamplingDTO save(SamplingDTO samplingDTO) {
        log.debug("Request to save Sampling : {}", samplingDTO);
        Sampling sampling = samplingMapper.toEntity(samplingDTO);
        sampling = samplingRepository.save(sampling);
        return samplingMapper.toDto(sampling);
    }

    /**
     * Get all the samplings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public Page<SamplingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Samplings");
        return samplingRepository.findAll(pageable)
            .map(samplingMapper::toDto);
    }


    /**
     * Get one sampling by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<SamplingDTO> findOne(String id) {
        log.debug("Request to get Sampling : {}", id);
        return samplingRepository.findById(id)
            .map(samplingMapper::toDto);
    }

    /**
     * Delete the sampling by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Sampling : {}", id);
        samplingRepository.deleteById(id);
    }
}
