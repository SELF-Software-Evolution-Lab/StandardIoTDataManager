package co.edu.uniandes.xrepo.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.xrepo.domain.Sensor;
import co.edu.uniandes.xrepo.service.SensorService;
import co.edu.uniandes.xrepo.web.rest.errors.BadRequestAlertException;
import co.edu.uniandes.xrepo.web.rest.util.HeaderUtil;
import co.edu.uniandes.xrepo.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Sensor.
 */
@RestController
@RequestMapping("/api")
public class SensorResource {

    private final Logger log = LoggerFactory.getLogger(SensorResource.class);

    private static final String ENTITY_NAME = "sensor";

    private final SensorService sensorService;

    public SensorResource(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    /**
     * POST  /sensors : Create a new sensor.
     *
     * @param sensor the sensor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sensor, or with status 400 (Bad Request) if the sensor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sensors")
    public ResponseEntity<Sensor> createSensor(@Valid @RequestBody Sensor sensor) throws URISyntaxException {
        log.debug("REST request to save Sensor : {}", sensor);
        if (sensor.getId() != null) {
            throw new BadRequestAlertException("A new sensor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sensor result = sensorService.save(sensor);
        return ResponseEntity.created(new URI("/api/sensors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sensors : Updates an existing sensor.
     *
     * @param sensor the sensor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sensor,
     * or with status 400 (Bad Request) if the sensor is not valid,
     * or with status 500 (Internal Server Error) if the sensor couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sensors")
    public ResponseEntity<Sensor> updateSensor(@Valid @RequestBody Sensor sensor) throws URISyntaxException {
        log.debug("REST request to update Sensor : {}", sensor);
        if (sensor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Sensor result = sensorService.save(sensor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sensor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sensors : get all the sensors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sensors in body
     */
    @GetMapping("/sensors")
    public ResponseEntity<List<Sensor>> getAllSensors(Pageable pageable) {
        log.debug("REST request to get a page of Sensors");
        Page<Sensor> page = sensorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sensors");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /sensors/:id : get the "id" sensor.
     *
     * @param id the id of the sensor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sensor, or with status 404 (Not Found)
     */
    @GetMapping("/sensors/{id}")
    public ResponseEntity<Sensor> getSensor(@PathVariable String id) {
        log.debug("REST request to get Sensor : {}", id);
        Optional<Sensor> sensor = sensorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sensor);
    }

    /**
     * DELETE  /sensors/:id : delete the "id" sensor.
     *
     * @param id the id of the sensor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sensors/{id}")
    public ResponseEntity<Void> deleteSensor(@PathVariable String id) {
        log.debug("REST request to delete Sensor : {}", id);
        sensorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
