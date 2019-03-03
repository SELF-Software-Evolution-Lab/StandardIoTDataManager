package co.edu.uniandes.xrepo.service.mapper;

import org.mapstruct.Mapper;

import co.edu.uniandes.xrepo.domain.Organization;
import co.edu.uniandes.xrepo.service.dto.OrganizationDTO;

/**
 * Mapper for the entity Organization and its DTO OrganizationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrganizationMapper extends EntityMapper<OrganizationDTO, Organization> {



    default Organization fromId(String id) {
        if (id == null) {
            return null;
        }
        Organization organization = new Organization();
        organization.setId(id);
        return organization;
    }
}
