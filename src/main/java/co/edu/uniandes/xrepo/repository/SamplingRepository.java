package co.edu.uniandes.xrepo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.xrepo.domain.Sampling;


/**
 * Spring Data MongoDB repository for the Sampling entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SamplingRepository extends MongoRepository<Sampling, String> {

}
