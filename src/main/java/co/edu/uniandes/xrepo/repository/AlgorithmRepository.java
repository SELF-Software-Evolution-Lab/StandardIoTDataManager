package co.edu.uniandes.xrepo.repository;

import co.edu.uniandes.xrepo.domain.Algorithm;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Algorithm entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlgorithmRepository extends MongoRepository<Algorithm, String> {

}
