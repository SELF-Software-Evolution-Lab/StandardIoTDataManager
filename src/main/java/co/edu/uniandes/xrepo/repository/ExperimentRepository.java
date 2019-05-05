package co.edu.uniandes.xrepo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.xrepo.domain.Experiment;


/**
 * Spring Data MongoDB repository for the Experiment entity.
 */
@Repository
public interface ExperimentRepository extends MongoRepository<Experiment, String> {

    @Query(value = "{ 'tags' : {$all : ?0 }}")
    List<Experiment> findWithTags(List<String> tagsSearch);

    List<Experiment> findBySystem_Id(String systemId);
}
