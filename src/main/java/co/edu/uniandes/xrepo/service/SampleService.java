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

/**
 * Service Implementation for managing Sample.
 */
@Service
public class SampleService {

    private final Logger log = LoggerFactory.getLogger(SampleService.class);

    private final SampleRepository sampleRepository;
    private final SamplingRepository samplingRepository;

    public SampleService(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    /**
     * Save a sample.
     *
     * @param sample the entity to save
     * @return the persisted entity
     */
    public Sample save(Sample sample) {
        log.debug("Request to save Sample : {}", sample);
        return sampleRepository.save(sample);
    }

    /**
     * Get all the samples.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public Page<Sample> findAll(Pageable pageable) {
        log.debug("Request to get all Samples");
        return sampleRepository.findAll(pageable);
    }


    /**
     * Get one sample by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<Sample> findOne(String id) {
        log.debug("Request to get Sample : {}", id);
        return sampleRepository.findById(id);
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
