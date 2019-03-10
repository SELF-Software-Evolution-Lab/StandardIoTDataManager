package co.edu.uniandes.xrepo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.xrepo.domain.Sample;


/**
 * Spring Data MongoDB repository for the Sample entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SampleRepository extends MongoRepository<Sample, String> {

}
