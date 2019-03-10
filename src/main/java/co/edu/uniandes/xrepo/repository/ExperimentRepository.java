package co.edu.uniandes.xrepo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.xrepo.domain.Experiment;


/**
 * Spring Data MongoDB repository for the Experiment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExperimentRepository extends MongoRepository<Experiment, String> {

}
