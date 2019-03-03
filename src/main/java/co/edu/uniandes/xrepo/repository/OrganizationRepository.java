package co.edu.uniandes.xrepo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.xrepo.domain.Organization;


/**
 * Spring Data MongoDB repository for the Organization entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationRepository extends MongoRepository<Organization, String> {

}
