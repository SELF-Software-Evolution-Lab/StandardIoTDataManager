package co.edu.uniandes.xrepo.service;

import java.time.Instant;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import co.edu.uniandes.xrepo.domain.TargetSystem;
import co.edu.uniandes.xrepo.domain.User;
import co.edu.uniandes.xrepo.repository.TargetSystemRepository;
import co.edu.uniandes.xrepo.security.SecurityUtils;
import co.edu.uniandes.xrepo.service.dto.TargetSystemDTO;
import co.edu.uniandes.xrepo.service.mapper.TargetSystemMapper;

/**
 * Service Implementation for managing TargetSystem.
 */
@Service
public class TargetSystemService {

    private final Logger log = LoggerFactory.getLogger(TargetSystemService.class);

    private final TargetSystemRepository targetSystemRepository;

    private final TargetSystemMapper targetSystemMapper;
    private final UserService userService;

    public TargetSystemService(TargetSystemRepository targetSystemRepository, TargetSystemMapper targetSystemMapper,
                               UserService userService) {
        this.targetSystemRepository = targetSystemRepository;
        this.targetSystemMapper = targetSystemMapper;
        this.userService = userService;
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
        if (targetSystem.getId() == null){
            targetSystem.setCreated(Instant.now());
            Optional<User> creator = userService.getUserWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin().get());
            targetSystem.setCreatedBy(creator.get().getFirstName() + " " +creator.get().getLastName());
        }
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
