package co.edu.uniandes.xrepo.service.mapper;

import co.edu.uniandes.xrepo.domain.*;
import co.edu.uniandes.xrepo.service.dto.SamplesFilesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SamplesFiles and its DTO SamplesFilesDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SamplesFilesMapper extends EntityMapper<SamplesFilesDTO, SamplesFiles> {



    default SamplesFiles fromId(String id) {
        if (id == null) {
            return null;
        }
        SamplesFiles samplesFiles = new SamplesFiles();
        samplesFiles.setId(id);
        return samplesFiles;
    }
}
