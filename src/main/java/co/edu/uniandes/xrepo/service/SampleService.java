package co.edu.uniandes.xrepo.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import co.edu.uniandes.xrepo.domain.Sample;
import co.edu.uniandes.xrepo.domain.Sampling;
import co.edu.uniandes.xrepo.repository.SampleRepository;
import co.edu.uniandes.xrepo.repository.SamplingRepository;
import co.edu.uniandes.xrepo.service.dto.SampleDTO;
import co.edu.uniandes.xrepo.service.mapper.SampleMapper;

/**
 * Service Implementation for managing Sample.
 */
@Service
public class SampleService {

    private final Logger log = LoggerFactory.getLogger(SampleService.class);

    private final SampleRepository sampleRepository;

    private final SamplingRepository samplingRepository;

    private final SampleMapper sampleMapper;



    public SampleService(SampleRepository sampleRepository, SampleMapper sampleMapper, SamplingRepository samplingRepository) {
        this.sampleRepository = sampleRepository;
        this.sampleMapper = sampleMapper;
        this.samplingRepository = samplingRepository;
    }

    /**
     * Save a sample.
     *
     * @param sampleDTO the entity to save
     * @return the persisted entity
     */
    public SampleDTO save(SampleDTO sampleDTO) {
        log.debug("Request to save SampleDTO : {}", sampleDTO);
        Sample sample = sampleMapper.toEntity(sampleDTO);
        completeSample(sample);
        sample = sampleRepository.save(sample);
        return sampleMapper.toDto(sample);
    }

    private void completeSample(Sample sample) {
        Optional<Sampling> byId = samplingRepository.findById(sample.getSamplingId());
        Sampling sampling = byId.orElseThrow(() -> new IllegalArgumentException("Sampling Id " + sample.getSamplingId() + " not found"));
        sample.setExperimentId(sampling.getExperiment().getId());
        sample.setTargetSystemId(sampling.getExperiment().getSystem().getId());
    }

    /**
     * Get all the samples.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public Page<SampleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Samples");
        return sampleRepository.findAll(pageable)
            .map(sampleMapper::toDto);
    }


    /**
     * Get one sample by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<SampleDTO> findOne(String id) {
        log.debug("Request to get Sample : {}", id);
        return sampleRepository.findById(id)
            .map(sampleMapper::toDto);
    }

    /**
     * Delete the sample by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Sample : {}", id);
        sampleRepository.deleteById(id);
    }
}
