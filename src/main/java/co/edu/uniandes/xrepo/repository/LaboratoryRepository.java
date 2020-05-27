package co.edu.uniandes.xrepo.repository;

import co.edu.uniandes.xrepo.domain.Laboratory;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Laboratory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LaboratoryRepository extends MongoRepository<Laboratory, String> {

}
