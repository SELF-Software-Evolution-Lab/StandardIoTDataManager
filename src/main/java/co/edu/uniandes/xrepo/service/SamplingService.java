package co.edu.uniandes.xrepo.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import co.edu.uniandes.xrepo.domain.Device;
import co.edu.uniandes.xrepo.domain.Sampling;
import co.edu.uniandes.xrepo.repository.SamplingRepository;
import co.edu.uniandes.xrepo.service.dto.SamplingDTO;
import co.edu.uniandes.xrepo.service.dto.SensorDTO;
import co.edu.uniandes.xrepo.service.mapper.SamplingMapper;

/**
 * Service Implementation for managing Sampling.
 */
@Service
public class SamplingService {

    private final Logger log = LoggerFactory.getLogger(SamplingService.class);

    private final SamplingRepository samplingRepository;

    private final SamplingMapper samplingMapper;

    public SamplingService(SamplingRepository samplingRepository, SamplingMapper samplingMapper) {
        this.samplingRepository = samplingRepository;
        this.samplingMapper = samplingMapper;
    }

    /**
     * Save a sampling.
     *
     * @param samplingDTO the entity to save
     * @return the persisted entity
     */
    public SamplingDTO save(SamplingDTO samplingDTO) {
        log.debug("Request to save Sampling : {}", samplingDTO);
        Sampling sampling = samplingMapper.toEntity(samplingDTO);
        bindDeviceSensors(samplingDTO, sampling);
        sampling = samplingRepository.save(sampling);
        return samplingMapper.toDto(sampling);
    }

    private void bindDeviceSensors(SamplingDTO samplingDTO, Sampling sampling) {
        final Map<String, Set<String>> deviceSensor = sampling.getDeviceSensor();
        List<SensorDTO> inputSensors = samplingDTO.getSensors();
        inputSensors.stream().map(dto -> Pair.of(dto.getDeviceId(), dto.getInternalId()))
            .forEach(devSensor -> {
                Set<String> sensors = deviceSensor.getOrDefault(devSensor.getFirst(), new HashSet<>());
                sensors.add(devSensor.getSecond());
                deviceSensor.put(devSensor.getFirst(), sensors);
            });
    }

    /**
     * Get all the samplings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public Page<SamplingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Samplings");
        return samplingRepository.findAll(pageable)
            .map(samplingMapper::toDto);
    }


    /**
     * Get one sampling by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<SamplingDTO> findOne(String id) {
        log.debug("Request to get Sampling : {}", id);
        Optional<Sampling> byId = samplingRepository.findById(id);
        return byId.map(samplingMapper::toDto).map(samplingDTO -> addDevice2Sensor(byId.get(), samplingDTO));
    }

    private SamplingDTO addDevice2Sensor(Sampling source, SamplingDTO target) {
        for (Map.Entry<String, Set<String>> devSensor : source.getDeviceSensor().entrySet()) {
            Optional<Device> device = target.getDevices().stream()
                .filter(eachDvc -> eachDvc.getInternalId().equals(devSensor.getKey()))
                .findFirst();
            if(device.isPresent()) {
                for (String sensorId : devSensor.getValue()) {
                    target.getSensors().stream()
                        .filter(sensor -> sensor.getInternalId().equals(sensorId))
                        .findFirst().ifPresent(sensorDTO -> {
                            sensorDTO.setDeviceId(device.get().getInternalId());
                            sensorDTO.setDeviceName(device.get().getName());
                        });
                }
            }
        }
        return target;
    }

    /**
     * Delete the sampling by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Sampling : {}", id);
        samplingRepository.deleteById(id);
    }
}
