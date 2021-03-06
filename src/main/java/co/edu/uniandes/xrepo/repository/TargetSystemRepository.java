package co.edu.uniandes.xrepo.repository;

import co.edu.uniandes.xrepo.domain.TargetSystem;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the TargetSystem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TargetSystemRepository extends MongoRepository<TargetSystem, String> {

}
