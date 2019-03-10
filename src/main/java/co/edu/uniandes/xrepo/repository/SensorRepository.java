package co.edu.uniandes.xrepo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.xrepo.domain.Sensor;


/**
 * Spring Data MongoDB repository for the Sensor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SensorRepository extends MongoRepository<Sensor, String> {

}
