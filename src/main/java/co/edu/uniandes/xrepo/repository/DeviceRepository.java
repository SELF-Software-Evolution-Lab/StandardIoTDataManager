package co.edu.uniandes.xrepo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.xrepo.domain.Device;


/**
 * Spring Data MongoDB repository for the Device entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeviceRepository extends MongoRepository<Device, String> {

}
