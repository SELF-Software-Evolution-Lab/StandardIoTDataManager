package co.edu.uniandes.xrepo.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import co.edu.uniandes.xrepo.domain.Device;
import co.edu.uniandes.xrepo.repository.DeviceRepository;

/**
 * Service Implementation for managing Device.
 */
@Service
public class DeviceService {

    private final Logger log = LoggerFactory.getLogger(DeviceService.class);

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    /**
     * Save a device.
     *
     * @param device the entity to save
     * @return the persisted entity
     */
    public Device save(Device device) {
        log.debug("Request to save Device : {}", device);
        return deviceRepository.save(device);
    }

    /**
     * Get all the devices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public Page<Device> findAll(Pageable pageable) {
        log.debug("Request to get all Devices");
        return deviceRepository.findAll(pageable);
    }


    /**
     * Get one device by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<Device> findOne(String id) {
        log.debug("Request to get Device : {}", id);
        return deviceRepository.findById(id);
    }

    /**
     * Delete the device by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Device : {}", id);
        deviceRepository.deleteById(id);
    }
}
