package co.edu.uniandes.xrepo.service;

import co.edu.uniandes.xrepo.domain.Tag;
import co.edu.uniandes.xrepo.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service Implementation for managing Tag.
 */
@Service
public class TagService {

    private final Logger log = LoggerFactory.getLogger(TagService.class);

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
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
}
