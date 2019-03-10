package co.edu.uniandes.xrepo.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import co.edu.uniandes.xrepo.domain.Sensor;
import co.edu.uniandes.xrepo.repository.SensorRepository;

/**
 * Service Implementation for managing Sensor.
 */
@Service
public class SensorService {

    private final Logger log = LoggerFactory.getLogger(SensorService.class);

    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    /**
     * Save a sensor.
     *
     * @param sensor the entity to save
     * @return the persisted entity
     */
    public Sensor save(Sensor sensor) {
        log.debug("Request to save Sensor : {}", sensor);
        return sensorRepository.save(sensor);
    }

    /**
     * Get all the sensors.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public Page<Sensor> findAll(Pageable pageable) {
        log.debug("Request to get all Sensors");
        return sensorRepository.findAll(pageable);
    }


    /**
     * Get one sensor by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<Sensor> findOne(String id) {
        log.debug("Request to get Sensor : {}", id);
        return sensorRepository.findById(id);
    }

    /**
     * Delete the sensor by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Sensor : {}", id);
        sensorRepository.deleteById(id);
    }
}
