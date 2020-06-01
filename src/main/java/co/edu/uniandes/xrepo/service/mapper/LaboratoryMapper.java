package co.edu.uniandes.xrepo.service.mapper;

import co.edu.uniandes.xrepo.domain.*;
import co.edu.uniandes.xrepo.service.dto.LaboratoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Laboratory and its DTO LaboratoryDTO.
 */
@Mapper(componentModel = "spring", uses = {SamplingMapper.class})
public interface LaboratoryMapper extends EntityMapper<LaboratoryDTO, Laboratory> {

    @Mapping(source = "sampling.id", target = "samplingId")
    @Mapping(source = "sampling.name", target = "samplingName")
    LaboratoryDTO toDto(Laboratory laboratory);

    @Mapping(target = "algorithms", ignore = true)
    @Mapping(target = "subSets", ignore = true)
    @Mapping(source = "samplingId", target = "sampling")
    Laboratory toEntity(LaboratoryDTO laboratoryDTO);

    default Laboratory fromId(String id) {
        if (id == null) {
            return null;
        }
        Laboratory laboratory = new Laboratory();
        laboratory.setId(id);
        return laboratory;
    }
}
