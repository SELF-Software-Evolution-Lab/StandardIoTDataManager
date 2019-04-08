package co.edu.uniandes.xrepo.repository;

import co.edu.uniandes.xrepo.domain.SamplesFiles;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the SamplesFiles entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SamplesFilesRepository extends MongoRepository<SamplesFiles, String> {

}
