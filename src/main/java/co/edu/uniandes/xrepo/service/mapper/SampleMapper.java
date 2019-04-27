package co.edu.uniandes.xrepo.service.mapper;

import co.edu.uniandes.xrepo.domain.*;
import co.edu.uniandes.xrepo.service.dto.SampleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Sample and its DTO SampleDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SampleMapper extends EntityMapper<SampleDTO, Sample> {



    default Sample fromId(String id) {
        if (id == null) {
            return null;
        }
        Sample sample = new Sample();
        sample.setId(id);
        return sample;
    }
}
