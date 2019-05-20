package co.edu.uniandes.xrepo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.xrepo.domain.Sampling;
import co.edu.uniandes.xrepo.repository.custom.SamplingRepositoryCustom;


/**
 * Spring Data MongoDB repository for the Sampling entity.
 */

@Repository
public interface SamplingRepository extends MongoRepository<Sampling, String>, SamplingRepositoryCustom {

    List<Sampling> findByTargetSystemId(String tsId);
}
