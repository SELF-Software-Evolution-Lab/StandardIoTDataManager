package co.edu.uniandes.xrepo.repository;

import co.edu.uniandes.xrepo.domain.SubSet;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data MongoDB repository for the SubSet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubSetRepository extends MongoRepository<SubSet, String> {
    List<SubSet> findByLaboratoryId(String Id);
}
