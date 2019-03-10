package co.edu.uniandes.xrepo.service.mapper;

import co.edu.uniandes.xrepo.domain.*;
import co.edu.uniandes.xrepo.service.dto.TargetSystemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TargetSystem and its DTO TargetSystemDTO.
 */
@Mapper(componentModel = "spring", uses = {OrganizationMapper.class})
public interface TargetSystemMapper extends EntityMapper<TargetSystemDTO, TargetSystem> {

    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "organization.name", target = "organizationName")
    TargetSystemDTO toDto(TargetSystem targetSystem);

    @Mapping(source = "organizationId", target = "organization")
    TargetSystem toEntity(TargetSystemDTO targetSystemDTO);

    default TargetSystem fromId(String id) {
        if (id == null) {
            return null;
        }
        TargetSystem targetSystem = new TargetSystem();
        targetSystem.setId(id);
        return targetSystem;
    }
}
