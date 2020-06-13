package co.edu.uniandes.xrepo.service.mapper;

import co.edu.uniandes.xrepo.domain.*;
import co.edu.uniandes.xrepo.service.dto.SubSetDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SubSet and its DTO SubSetDTO.
 */
@Mapper(componentModel = "spring", uses = {LaboratoryMapper.class})
public interface SubSetMapper extends EntityMapper<SubSetDTO, SubSet> {

    @Mapping(source = "laboratory.id", target = "laboratoryId")
    @Mapping(source = "laboratory.name", target = "laboratoryName")
    SubSetDTO toDto(SubSet subSet);

    @Mapping(source = "laboratoryId", target = "laboratory")
    @Mapping(target = "algorithm", ignore = true)
    SubSet toEntity(SubSetDTO subSetDTO);

    default SubSet fromId(String id) {
        if (id == null) {
            return null;
        }
        SubSet subSet = new SubSet();
        subSet.setId(id);
        return subSet;
    }
}
