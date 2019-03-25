package co.edu.uniandes.xrepo.service;

import java.time.Instant;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import co.edu.uniandes.xrepo.domain.Experiment;
import co.edu.uniandes.xrepo.domain.User;
import co.edu.uniandes.xrepo.repository.ExperimentRepository;
import co.edu.uniandes.xrepo.security.SecurityUtils;
import co.edu.uniandes.xrepo.service.dto.ExperimentDTO;
import co.edu.uniandes.xrepo.service.mapper.ExperimentMapper;

/**
 * Service Implementation for managing Experiment.
 */
@Service
public class ExperimentService {

    private final Logger log = LoggerFactory.getLogger(ExperimentService.class);

    private final ExperimentRepository experimentRepository;

    private final ExperimentMapper experimentMapper;
    private final UserService userService;

    public ExperimentService(ExperimentRepository experimentRepository, ExperimentMapper experimentMapper,
                             UserService userService) {
        this.experimentRepository = experimentRepository;
        this.experimentMapper = experimentMapper;
        this.userService = userService;
    }

    /**
     * Save a experiment.
     *
     * @param experimentDTO the entity to save
     * @return the persisted entity
     */
    public ExperimentDTO save(ExperimentDTO experimentDTO) {
        log.debug("Request to save Experiment : {}", experimentDTO);
        Experiment experiment = experimentMapper.toEntity(experimentDTO);
        if (experiment.getId() == null) {
            experiment.setCreated(Instant.now());
            Optional<User> creator = userService.getUserWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin().get());
            experiment.setCreatedBy(creator.get().getFirstName() + " " +creator.get().getLastName());
        }
        experiment = experimentRepository.save(experiment);
        return experimentMapper.toDto(experiment);
    }

    /**
     * Get all the experiments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public Page<ExperimentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Experiments");
        return experimentRepository.findAll(pageable)
            .map(experimentMapper::toDto);
    }


    /**
     * Get one experiment by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<ExperimentDTO> findOne(String id) {
        log.debug("Request to get Experiment : {}", id);
        return experimentRepository.findById(id)
            .map(experimentMapper::toDto);
    }

    /**
     * Delete the experiment by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Experiment : {}", id);
        experimentRepository.deleteById(id);
    }
}
