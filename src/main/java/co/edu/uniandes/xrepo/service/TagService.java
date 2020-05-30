package co.edu.uniandes.xrepo.service;

import co.edu.uniandes.xrepo.domain.Experiment;
import co.edu.uniandes.xrepo.domain.Sampling;
import co.edu.uniandes.xrepo.domain.Tag;
import co.edu.uniandes.xrepo.repository.ExperimentRepository;
import co.edu.uniandes.xrepo.repository.SamplingRepository;
import co.edu.uniandes.xrepo.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service Implementation for managing Tag.
 */
@Service
public class TagService {

    private final Logger log = LoggerFactory.getLogger(TagService.class);

    private final TagRepository tagRepository;

    private final ExperimentRepository experimentRepository;

    private final SamplingRepository samplingRepository;

    public TagService(TagRepository tagRepository, ExperimentRepository experimentRepository, SamplingRepository samplingRepository) {
        this.tagRepository = tagRepository;
        this.experimentRepository = experimentRepository;
        this.samplingRepository = samplingRepository;
    }

    /**
     * Save a tag.
     *
     * @param tag the entity to save
     * @return the persisted entity
     */
    public Tag save(Tag tag) {
        log.debug("Request to save Tag : {}", tag);
        return tagRepository.save(tag);
    }

    /**
     * Get all the tags.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public Page<Tag> findAll(Pageable pageable) {
        log.debug("Request to get all Tags");
        return tagRepository.findAll(pageable);
    }


    /**
     * Get one tag by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<Tag> findOne(String id) {
        log.debug("Request to get Tag : {}", id);
        return tagRepository.findById(id);
    }

    /**
     * Delete the tag by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Tag : {}", id);
        tagRepository.deleteById(id);
    }

    public List<String> findByTargetSystem(String tsId) {

        final List<Experiment> experimentsBySystem = experimentRepository.findBySystem_Id(tsId);

        final List<Sampling> samplings = samplingRepository.findByTargetSystemId(tsId);

        final Stream<String> experimentsTags = experimentsBySystem.stream()
            .flatMap(experiment -> experiment.getTags().stream());

        final Stream<String> samplingsTags = samplings.stream().
            flatMap(sampling -> sampling.getTags().stream());

        Set<String> collect = Stream.concat(experimentsTags,
            samplingsTags).collect(Collectors.toSet());

        return new ArrayList<>(collect);
    }
}
