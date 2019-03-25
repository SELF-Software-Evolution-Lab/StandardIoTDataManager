package co.edu.uniandes.xrepo.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.uniandes.xrepo.domain.Sampling;
import co.edu.uniandes.xrepo.service.dto.SamplingDTO;

/**
 * Mapper for the entity Sampling and its DTO SamplingDTO.
 */
@Mapper(componentModel = "spring", uses = {ExperimentMapper.class, SensorMapper.class})
public interface SamplingMapper extends EntityMapper<SamplingDTO, Sampling> {

    @Mapping(source = "experiment.id", target = "experimentId")
    @Mapping(source = "experiment.name", target = "experimentName")
    SamplingDTO toDto(Sampling sampling);

    @Mapping(source = "experimentId", target = "experiment")
    @Mapping(target = "devices", ignore = true)
    Sampling toEntity(SamplingDTO samplingDTO);

    default Sampling fromId(String id) {
        if (id == null) {
            return null;
        }
        Sampling sampling = new Sampling();
        sampling.setId(id);
        return sampling;
    }
}
