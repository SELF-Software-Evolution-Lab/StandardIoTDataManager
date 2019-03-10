package co.edu.uniandes.xrepo.service;

import co.edu.uniandes.xrepo.domain.TargetSystem;
import co.edu.uniandes.xrepo.repository.TargetSystemRepository;
import co.edu.uniandes.xrepo.service.dto.TargetSystemDTO;
import co.edu.uniandes.xrepo.service.mapper.TargetSystemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service Implementation for managing TargetSystem.
 */
@Service
public class TargetSystemService {

    private final Logger log = LoggerFactory.getLogger(TargetSystemService.class);

    private final TargetSystemRepository targetSystemRepository;

    private final TargetSystemMapper targetSystemMapper;

    public TargetSystemService(TargetSystemRepository targetSystemRepository, TargetSystemMapper targetSystemMapper) {
        this.targetSystemRepository = targetSystemRepository;
        this.targetSystemMapper = targetSystemMapper;
    }

    /**
     * Save a targetSystem.
     *
     * @param targetSystemDTO the entity to save
     * @return the persisted entity
     */
    public TargetSystemDTO save(TargetSystemDTO targetSystemDTO) {
        log.debug("Request to save TargetSystem : {}", targetSystemDTO);
        TargetSystem targetSystem = targetSystemMapper.toEntity(targetSystemDTO);
        targetSystem = targetSystemRepository.save(targetSystem);
        return targetSystemMapper.toDto(targetSystem);
    }

    /**
     * Get all the targetSystems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public Page<TargetSystemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TargetSystems");
        return targetSystemRepository.findAll(pageable)
            .map(targetSystemMapper::toDto);
    }


    /**
     * Get one targetSystem by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<TargetSystemDTO> findOne(String id) {
        log.debug("Request to get TargetSystem : {}", id);
        return targetSystemRepository.findById(id)
            .map(targetSystemMapper::toDto);
    }

    /**
     * Delete the targetSystem by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete TargetSystem : {}", id);
        targetSystemRepository.deleteById(id);
    }
}
