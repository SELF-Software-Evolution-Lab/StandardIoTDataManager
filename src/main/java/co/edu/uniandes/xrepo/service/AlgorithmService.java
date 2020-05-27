package co.edu.uniandes.xrepo.service;

import co.edu.uniandes.xrepo.domain.Algorithm;
import co.edu.uniandes.xrepo.repository.AlgorithmRepository;
import co.edu.uniandes.xrepo.service.dto.AlgorithmDTO;
import co.edu.uniandes.xrepo.service.mapper.AlgorithmMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service Implementation for managing Algorithm.
 */
@Service
public class AlgorithmService {

    private final Logger log = LoggerFactory.getLogger(AlgorithmService.class);

    private final AlgorithmRepository algorithmRepository;

    private final AlgorithmMapper algorithmMapper;

    public AlgorithmService(AlgorithmRepository algorithmRepository, AlgorithmMapper algorithmMapper) {
        this.algorithmRepository = algorithmRepository;
        this.algorithmMapper = algorithmMapper;
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
}
