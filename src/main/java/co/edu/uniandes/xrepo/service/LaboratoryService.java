package co.edu.uniandes.xrepo.service;

import co.edu.uniandes.xrepo.domain.Algorithm;
import co.edu.uniandes.xrepo.domain.Laboratory;
import co.edu.uniandes.xrepo.domain.SubSet;
import co.edu.uniandes.xrepo.repository.LaboratoryRepository;
import co.edu.uniandes.xrepo.repository.SubSetRepository;
import co.edu.uniandes.xrepo.service.dto.LaboratoryDTO;
import co.edu.uniandes.xrepo.service.mapper.LaboratoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service Implementation for managing Laboratory.
 */
@Service
public class LaboratoryService {

    private final Logger log = LoggerFactory.getLogger(LaboratoryService.class);

    private final LaboratoryRepository laboratoryRepository;
    private final SubSetRepository subSetRepository;

    private final LaboratoryMapper laboratoryMapper;

    public LaboratoryService(LaboratoryRepository laboratoryRepository, SubSetRepository subSetRepository, LaboratoryMapper laboratoryMapper) {
        this.laboratoryRepository = laboratoryRepository;
        this.subSetRepository = subSetRepository;
        this.laboratoryMapper = laboratoryMapper;
    }

    /**
     * Save a laboratory.
     *
     * @param laboratoryDTO the entity to save
     * @return the persisted entity
     */
    public LaboratoryDTO save(LaboratoryDTO laboratoryDTO, String serverURI) {
        log.debug("Request to save Laboratory : {}", laboratoryDTO);
        Laboratory laboratory = laboratoryMapper.toEntity(laboratoryDTO);
        laboratory = laboratoryRepository.save(laboratory);

        if (laboratoryDTO.getShareValidThru() != null && laboratoryDTO.getShareUrl() == null){
            laboratory.setShareUrl("/laboratory/" + laboratory.getId() + "/share");
            laboratory = laboratoryRepository.save(laboratory);
        }
        return laboratoryMapper.toDto(laboratory);
    }

    /**
     * Get all the laboratories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public Page<LaboratoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Laboratories");
        return laboratoryRepository.findAll(pageable)
            .map(laboratoryMapper::toDto);
    }


    /**
     * Get one laboratory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<LaboratoryDTO> findOne(String id) {
        log.debug("Request to get Laboratory : {}", id);
        return laboratoryRepository.findById(id)
            .map(laboratoryMapper::toDto);
    }

    /**
     * Get laboratory shared files by id.
     *
     * @param id the id of the entity
     * @return the String Array of files
     */
    public List<String> findAllSharedFiles(String id) {
        log.debug("Request to get all Laboratory shared files: {}", id);
        List<String> samplingFiles = laboratoryRepository.findById(id).get()
            .getSampling().getFileUris().stream().collect(Collectors.toList());
        List<String> subsetFiles = subSetRepository.findByLaboratoryId(id)
            .stream().map(x -> x.getFileHdfsLocation())
            .flatMap(x -> x.stream()).collect(Collectors.toList());

        return Stream.concat(samplingFiles.stream(), subsetFiles.stream()).collect(Collectors.toList());
    }

    /**
     * Delete the laboratory by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Laboratory : {}", id);
        laboratoryRepository.deleteById(id);
    }
}
