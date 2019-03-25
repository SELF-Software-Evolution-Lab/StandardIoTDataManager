package co.edu.uniandes.xrepo.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.uniandes.xrepo.domain.Sensor;
import co.edu.uniandes.xrepo.domain.TargetSystem;
import co.edu.uniandes.xrepo.service.dto.SensorDTO;
import co.edu.uniandes.xrepo.service.dto.TargetSystemDTO;

/**
 * Mapper for the entity TargetSystem and its DTO TargetSystemDTO.
 */
@Mapper(componentModel = "spring")
public interface SensorMapper extends EntityMapper<SensorDTO, Sensor> {

    default Sensor fromId(String id) {
        if (id == null) {
            return null;
        }
        Sensor sensor = new Sensor();
        sensor.setInternalId(id);
        return sensor;
    }
}
