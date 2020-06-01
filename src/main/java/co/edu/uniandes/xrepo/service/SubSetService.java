package co.edu.uniandes.xrepo.service;

import co.edu.uniandes.xrepo.domain.SubSet;
import co.edu.uniandes.xrepo.domain.enumeration.SubSetType;
import co.edu.uniandes.xrepo.repository.SubSetRepository;
import co.edu.uniandes.xrepo.service.dto.SubSetDTO;
import co.edu.uniandes.xrepo.service.mapper.SubSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing SubSet.
 */
@Service
public class SubSetService {

    private final Logger log = LoggerFactory.getLogger(SubSetService.class);

    private final SubSetRepository subSetRepository;

    private final SubSetMapper subSetMapper;

    public SubSetService(SubSetRepository subSetRepository, SubSetMapper subSetMapper) {
        this.subSetRepository = subSetRepository;
        this.subSetMapper = subSetMapper;
    }

    /**
     * Save a subSet.
     *
     * @param subSetDTO the entity to save
     * @return the persisted entity
     */
    public SubSetDTO save(SubSetDTO subSetDTO) {
        log.debug("Request to save SubSet : {}", subSetDTO);
        SubSet subSet = subSetMapper.toEntity(subSetDTO);
        subSet = subSetRepository.save(subSet);
        return subSetMapper.toDto(subSet);
    }

    public SubSetDTO saveOnAlgorithm(SubSetDTO subSetDTO) {
        log.debug("Request to save SubSet : {}", subSetDTO);
        //check if the type exists and update it
        List<SubSet> subSets = subSetRepository.findAll();
        for(SubSet item : subSets){
            if (item.getSetType() == SubSetType.TRAINING && subSetDTO.getSetType() == SubSetType.TRAINING){
                subSetDTO.setId(item.getId());
            }
            if (item.getSetType() == SubSetType.VALIDATION && subSetDTO.getSetType() == SubSetType.VALIDATION){
                subSetDTO.setId(item.getId());
            }
        }
        SubSet subSet = subSetMapper.toEntity(subSetDTO);
        subSet = subSetRepository.save(subSet);
        return subSetMapper.toDto(subSet);
    }

    /**
     * Get all the subSets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public Page<SubSetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SubSets");
        return subSetRepository.findAll(pageable)
            .map(subSetMapper::toDto);
    }


    /**
     * Get one subSet by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<SubSetDTO> findOne(String id) {
        log.debug("Request to get SubSet : {}", id);
        return subSetRepository.findById(id)
            .map(subSetMapper::toDto);
    }

    /**
     * Delete the subSet by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete SubSet : {}", id);
        subSetRepository.deleteById(id);
    }
}
