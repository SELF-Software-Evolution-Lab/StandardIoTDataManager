package co.edu.uniandes.xrepo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.xrepo.domain.Experiment;
import co.edu.uniandes.xrepo.domain.Sampling;


/**
 * Spring Data MongoDB repository for the Sampling entity.
 */

@Repository
public interface SamplingRepository extends MongoRepository<Sampling, String> {

    @Query(value = "{ 'tags' : {$all : ?0 }}")
    List<Sampling> findWithTags(List<String> tagsSearch);

    List<Sampling> findByExperimentIn(List<Experiment> experiments);
}
