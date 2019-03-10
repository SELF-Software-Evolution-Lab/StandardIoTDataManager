package co.edu.uniandes.xrepo.web.rest;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import co.edu.uniandes.xrepo.XrepoApp;
import co.edu.uniandes.xrepo.domain.Sensor;
import co.edu.uniandes.xrepo.repository.SensorRepository;
import co.edu.uniandes.xrepo.service.SensorService;
import static co.edu.uniandes.xrepo.web.rest.TestUtil.createFormattingConversionService;
import co.edu.uniandes.xrepo.web.rest.errors.ExceptionTranslator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the SensorResource REST controller.
 *
 * @see SensorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = XrepoApp.class)
public class SensorResourceIntTest {

    private static final String DEFAULT_INTERNAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_INTERNAL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SENSOR_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SENSOR_TYPE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_POTENTIAL_FREQ = new BigDecimal(1);
    private static final BigDecimal UPDATED_POTENTIAL_FREQ = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SAMPLING_FREQ = new BigDecimal(1);
    private static final BigDecimal UPDATED_SAMPLING_FREQ = new BigDecimal(2);

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restSensorMockMvc;

    private Sensor sensor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SensorResource sensorResource = new SensorResource(sensorService);
        this.restSensorMockMvc = MockMvcBuilders.standaloneSetup(sensorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sensor createEntity() {
        Sensor sensor = new Sensor()
            .internalId(DEFAULT_INTERNAL_ID)
            .sensorType(DEFAULT_SENSOR_TYPE)
            .potentialFreq(DEFAULT_POTENTIAL_FREQ)
            .samplingFreq(DEFAULT_SAMPLING_FREQ);
        return sensor;
    }

    @Before
    public void initTest() {
        sensorRepository.deleteAll();
        sensor = createEntity();
    }

    @Test
    public void createSensor() throws Exception {
        int databaseSizeBeforeCreate = sensorRepository.findAll().size();

        // Create the Sensor
        restSensorMockMvc.perform(post("/api/sensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensor)))
            .andExpect(status().isCreated());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeCreate + 1);
        Sensor testSensor = sensorList.get(sensorList.size() - 1);
        assertThat(testSensor.getInternalId()).isEqualTo(DEFAULT_INTERNAL_ID);
        assertThat(testSensor.getSensorType()).isEqualTo(DEFAULT_SENSOR_TYPE);
        assertThat(testSensor.getPotentialFreq()).isEqualTo(DEFAULT_POTENTIAL_FREQ);
        assertThat(testSensor.getSamplingFreq()).isEqualTo(DEFAULT_SAMPLING_FREQ);
    }

    @Test
    public void createSensorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sensorRepository.findAll().size();

        // Create the Sensor with an existing ID
        sensor.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restSensorMockMvc.perform(post("/api/sensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensor)))
            .andExpect(status().isBadRequest());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkInternalIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = sensorRepository.findAll().size();
        // set the field null
        sensor.setInternalId(null);

        // Create the Sensor, which fails.

        restSensorMockMvc.perform(post("/api/sensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensor)))
            .andExpect(status().isBadRequest());

        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkSensorTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sensorRepository.findAll().size();
        // set the field null
        sensor.setSensorType(null);

        // Create the Sensor, which fails.

        restSensorMockMvc.perform(post("/api/sensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensor)))
            .andExpect(status().isBadRequest());

        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllSensors() throws Exception {
        // Initialize the database
        sensorRepository.save(sensor);

        // Get all the sensorList
        restSensorMockMvc.perform(get("/api/sensors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sensor.getId())))
            .andExpect(jsonPath("$.[*].internalId").value(hasItem(DEFAULT_INTERNAL_ID.toString())))
            .andExpect(jsonPath("$.[*].sensorType").value(hasItem(DEFAULT_SENSOR_TYPE.toString())))
            .andExpect(jsonPath("$.[*].potentialFreq").value(hasItem(DEFAULT_POTENTIAL_FREQ.intValue())))
            .andExpect(jsonPath("$.[*].samplingFreq").value(hasItem(DEFAULT_SAMPLING_FREQ.intValue())));
    }
    
    @Test
    public void getSensor() throws Exception {
        // Initialize the database
        sensorRepository.save(sensor);

        // Get the sensor
        restSensorMockMvc.perform(get("/api/sensors/{id}", sensor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sensor.getId()))
            .andExpect(jsonPath("$.internalId").value(DEFAULT_INTERNAL_ID.toString()))
            .andExpect(jsonPath("$.sensorType").value(DEFAULT_SENSOR_TYPE.toString()))
            .andExpect(jsonPath("$.potentialFreq").value(DEFAULT_POTENTIAL_FREQ.intValue()))
            .andExpect(jsonPath("$.samplingFreq").value(DEFAULT_SAMPLING_FREQ.intValue()));
    }

    @Test
    public void getNonExistingSensor() throws Exception {
        // Get the sensor
        restSensorMockMvc.perform(get("/api/sensors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateSensor() throws Exception {
        // Initialize the database
        sensorService.save(sensor);

        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();

        // Update the sensor
        Sensor updatedSensor = sensorRepository.findById(sensor.getId()).get();
        updatedSensor
            .internalId(UPDATED_INTERNAL_ID)
            .sensorType(UPDATED_SENSOR_TYPE)
            .potentialFreq(UPDATED_POTENTIAL_FREQ)
            .samplingFreq(UPDATED_SAMPLING_FREQ);

        restSensorMockMvc.perform(put("/api/sensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSensor)))
            .andExpect(status().isOk());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
        Sensor testSensor = sensorList.get(sensorList.size() - 1);
        assertThat(testSensor.getInternalId()).isEqualTo(UPDATED_INTERNAL_ID);
        assertThat(testSensor.getSensorType()).isEqualTo(UPDATED_SENSOR_TYPE);
        assertThat(testSensor.getPotentialFreq()).isEqualTo(UPDATED_POTENTIAL_FREQ);
        assertThat(testSensor.getSamplingFreq()).isEqualTo(UPDATED_SAMPLING_FREQ);
    }

    @Test
    public void updateNonExistingSensor() throws Exception {
        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();

        // Create the Sensor

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSensorMockMvc.perform(put("/api/sensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensor)))
            .andExpect(status().isBadRequest());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteSensor() throws Exception {
        // Initialize the database
        sensorService.save(sensor);

        int databaseSizeBeforeDelete = sensorRepository.findAll().size();

        // Delete the sensor
        restSensorMockMvc.perform(delete("/api/sensors/{id}", sensor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sensor.class);
        Sensor sensor1 = new Sensor();
        sensor1.setId("id1");
        Sensor sensor2 = new Sensor();
        sensor2.setId(sensor1.getId());
        assertThat(sensor1).isEqualTo(sensor2);
        sensor2.setId("id2");
        assertThat(sensor1).isNotEqualTo(sensor2);
        sensor1.setId(null);
        assertThat(sensor1).isNotEqualTo(sensor2);
    }
}
