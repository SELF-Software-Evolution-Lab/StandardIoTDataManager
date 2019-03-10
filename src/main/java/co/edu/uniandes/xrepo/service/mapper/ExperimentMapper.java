package co.edu.uniandes.xrepo.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.uniandes.xrepo.domain.Experiment;
import co.edu.uniandes.xrepo.service.dto.ExperimentDTO;

/**
 * Mapper for the entity Experiment and its DTO ExperimentDTO.
 */
@Mapper(componentModel = "spring", uses = {TargetSystemMapper.class})
public interface ExperimentMapper extends EntityMapper<ExperimentDTO, Experiment> {

    @Mapping(source = "system.id", target = "systemId")
    @Mapping(source = "system.name", target = "systemName")
    ExperimentDTO toDto(Experiment experiment);

    @Mapping(source = "systemId", target = "system")
    Experiment toEntity(ExperimentDTO experimentDTO);

    default Experiment fromId(String id) {
        if (id == null) {
            return null;
        }
        Experiment experiment = new Experiment();
        experiment.setId(id);
        return experiment;
    }
}
