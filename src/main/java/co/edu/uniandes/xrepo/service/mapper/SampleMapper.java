package co.edu.uniandes.xrepo.service.mapper;

import java.time.Instant;

import co.edu.uniandes.xrepo.domain.*;
import co.edu.uniandes.xrepo.service.dto.SampleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Sample and its DTO SampleDTO.
 */
@Mapper(componentModel = "spring", uses = {}, imports = Instant.class)
public interface SampleMapper extends EntityMapper<SampleDTO, Sample> {

    @Mapping(target = "ts", expression = "java(Instant.ofEpochSecond(dto.getTs().getEpochSeconds(), dto.getTs().getNanos()))")
    Sample toEntity(SampleDTO dto);

    @Mapping(target = "ts.epochSeconds", source="ts.epochSecond")
    @Mapping(target = "ts.nanos", source="ts.nano")
    SampleDTO toDto(Sample entity);

    default Sample fromId(String id) {
        if (id == null) {
            return null;
        }
        Sample sample = new Sample();
        sample.setId(id);
        return sample;
    }
}
