package co.edu.uniandes.xrepo.service.mapper;

import co.edu.uniandes.xrepo.domain.*;
import co.edu.uniandes.xrepo.service.dto.AlgorithmDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Algorithm and its DTO AlgorithmDTO.
 */
@Mapper(componentModel = "spring", uses = {LaboratoryMapper.class, SubSetMapper.class})
public interface AlgorithmMapper extends EntityMapper<AlgorithmDTO, Algorithm> {

    @Mapping(source = "laboratory.id", target = "laboratoryId")
    @Mapping(source = "laboratory.name", target = "laboratoryName")
    @Mapping(source = "subSet.id", target = "subSetId")
    AlgorithmDTO toDto(Algorithm algorithm);

    @Mapping(source = "laboratoryId", target = "laboratory")
    @Mapping(source = "subSetId", target = "subSet")
    Algorithm toEntity(AlgorithmDTO algorithmDTO);

    default Algorithm fromId(String id) {
        if (id == null) {
            return null;
        }
        Algorithm algorithm = new Algorithm();
        algorithm.setId(id);
        return algorithm;
    }
}
