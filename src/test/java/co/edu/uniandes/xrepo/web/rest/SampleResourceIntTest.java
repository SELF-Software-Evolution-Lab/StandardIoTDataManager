package co.edu.uniandes.xrepo.web.rest;

import co.edu.uniandes.xrepo.XrepoApp;

import co.edu.uniandes.xrepo.domain.Sample;
import co.edu.uniandes.xrepo.repository.SampleRepository;
import co.edu.uniandes.xrepo.service.SampleService;
import co.edu.uniandes.xrepo.service.SearchEngineService;
import co.edu.uniandes.xrepo.service.dto.SampleDTO;
import co.edu.uniandes.xrepo.service.mapper.SampleMapper;
import co.edu.uniandes.xrepo.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static co.edu.uniandes.xrepo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SampleResource REST controller.
 *
 * @see SampleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = XrepoApp.class)
public class SampleResourceIntTest {

    private static final LocalDateTime DEFAULT_DATE_TIME = LocalDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final LocalDateTime UPDATED_DATE_TIME = LocalDateTime.ofInstant(Instant.now().truncatedTo(ChronoUnit.MILLIS), ZoneOffset.UTC);

    private static final Instant DEFAULT_TS = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TS = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_SENSOR_INTERNAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_SENSOR_INTERNAL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SAMPLING_ID = "AAAAAAAAAA";
    private static final String UPDATED_SAMPLING_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EXPERIMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXPERIMENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TARGET_SYSTEM_ID = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_SYSTEM_ID = "BBBBBBBBBB";

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private SampleMapper sampleMapper;

    @Autowired
    private SampleService sampleService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restSampleMockMvc;

    private Sample sample;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SampleResource sampleResource = new SampleResource(sampleService, Mockito.mock(SearchEngineService.class));
        this.restSampleMockMvc = MockMvcBuilders.standaloneSetup(sampleResource)
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
    public static Sample createEntity() {
        Sample sample = new Sample()
            .dateTime(DEFAULT_DATE_TIME)
            .ts(DEFAULT_TS)
            .sensorInternalId(DEFAULT_SENSOR_INTERNAL_ID)
            .samplingId(DEFAULT_SAMPLING_ID)
            .experimentId(DEFAULT_EXPERIMENT_ID)
            .targetSystemId(DEFAULT_TARGET_SYSTEM_ID);
        return sample;
    }

    @Before
    public void initTest() {
        sampleRepository.deleteAll();
        sample = createEntity();
    }

    @Test
    public void createSample() throws Exception {
        int databaseSizeBeforeCreate = sampleRepository.findAll().size();

        // Create the Sample
        SampleDTO sampleDTO = sampleMapper.toDto(sample);
        restSampleMockMvc.perform(post("/api/samples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleDTO)))
            .andExpect(status().isCreated());

        // Validate the Sample in the database
        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeCreate + 1);
        Sample testSample = sampleList.get(sampleList.size() - 1);
        assertThat(testSample.getDateTime()).isEqualTo(DEFAULT_DATE_TIME);
        assertThat(testSample.getTs()).isEqualTo(DEFAULT_TS);
        assertThat(testSample.getSensorInternalId()).isEqualTo(DEFAULT_SENSOR_INTERNAL_ID);
        assertThat(testSample.getSamplingId()).isEqualTo(DEFAULT_SAMPLING_ID);
        assertThat(testSample.getExperimentId()).isEqualTo(DEFAULT_EXPERIMENT_ID);
        assertThat(testSample.getTargetSystemId()).isEqualTo(DEFAULT_TARGET_SYSTEM_ID);
    }

    @Test
    public void createSampleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sampleRepository.findAll().size();

        // Create the Sample with an existing ID
        sample.setId("existing_id");
        SampleDTO sampleDTO = sampleMapper.toDto(sample);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSampleMockMvc.perform(post("/api/samples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sample in the database
        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sampleRepository.findAll().size();
        // set the field null
        sample.setDateTime(null);

        // Create the Sample, which fails.
        SampleDTO sampleDTO = sampleMapper.toDto(sample);

        restSampleMockMvc.perform(post("/api/samples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleDTO)))
            .andExpect(status().isBadRequest());

        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkTsIsRequired() throws Exception {
        int databaseSizeBeforeTest = sampleRepository.findAll().size();
        // set the field null
        sample.setTs(null);

        // Create the Sample, which fails.
        SampleDTO sampleDTO = sampleMapper.toDto(sample);

        restSampleMockMvc.perform(post("/api/samples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleDTO)))
            .andExpect(status().isBadRequest());

        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkExperimentIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = sampleRepository.findAll().size();
        // set the field null
        sample.setExperimentId(null);

        // Create the Sample, which fails.
        SampleDTO sampleDTO = sampleMapper.toDto(sample);

        restSampleMockMvc.perform(post("/api/samples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleDTO)))
            .andExpect(status().isBadRequest());

        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkTargetSystemIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = sampleRepository.findAll().size();
        // set the field null
        sample.setTargetSystemId(null);

        // Create the Sample, which fails.
        SampleDTO sampleDTO = sampleMapper.toDto(sample);

        restSampleMockMvc.perform(post("/api/samples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleDTO)))
            .andExpect(status().isBadRequest());

        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllSamples() throws Exception {
        // Initialize the database
        sampleRepository.save(sample);

        // Get all the sampleList
        restSampleMockMvc.perform(get("/api/samples?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sample.getId())))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(DEFAULT_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].ts").value(hasItem(DEFAULT_TS.toString())))
            .andExpect(jsonPath("$.[*].sensorInternalId").value(hasItem(DEFAULT_SENSOR_INTERNAL_ID.toString())))
            .andExpect(jsonPath("$.[*].samplingId").value(hasItem(DEFAULT_SAMPLING_ID.toString())))
            .andExpect(jsonPath("$.[*].experimentId").value(hasItem(DEFAULT_EXPERIMENT_ID.toString())))
            .andExpect(jsonPath("$.[*].targetSystemId").value(hasItem(DEFAULT_TARGET_SYSTEM_ID.toString())));
    }
    
    @Test
    public void getSample() throws Exception {
        // Initialize the database
        sampleRepository.save(sample);

        // Get the sample
        restSampleMockMvc.perform(get("/api/samples/{id}", sample.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sample.getId()))
            .andExpect(jsonPath("$.dateTime").value(DEFAULT_DATE_TIME.toString()))
            .andExpect(jsonPath("$.ts").value(DEFAULT_TS.toString()))
            .andExpect(jsonPath("$.sensorInternalId").value(DEFAULT_SENSOR_INTERNAL_ID.toString()))
            .andExpect(jsonPath("$.samplingId").value(DEFAULT_SAMPLING_ID.toString()))
            .andExpect(jsonPath("$.experimentId").value(DEFAULT_EXPERIMENT_ID.toString()))
            .andExpect(jsonPath("$.targetSystemId").value(DEFAULT_TARGET_SYSTEM_ID.toString()));
    }

    @Test
    public void getNonExistingSample() throws Exception {
        // Get the sample
        restSampleMockMvc.perform(get("/api/samples/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateSample() throws Exception {
        // Initialize the database
        sampleRepository.save(sample);

        int databaseSizeBeforeUpdate = sampleRepository.findAll().size();

        // Update the sample
        Sample updatedSample = sampleRepository.findById(sample.getId()).get();
        updatedSample
            .dateTime(UPDATED_DATE_TIME)
            .ts(UPDATED_TS)
            .sensorInternalId(UPDATED_SENSOR_INTERNAL_ID)
            .samplingId(UPDATED_SAMPLING_ID)
            .experimentId(UPDATED_EXPERIMENT_ID)
            .targetSystemId(UPDATED_TARGET_SYSTEM_ID);
        SampleDTO sampleDTO = sampleMapper.toDto(updatedSample);

        restSampleMockMvc.perform(put("/api/samples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleDTO)))
            .andExpect(status().isOk());

        // Validate the Sample in the database
        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeUpdate);
        Sample testSample = sampleList.get(sampleList.size() - 1);
        assertThat(testSample.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testSample.getTs()).isEqualTo(UPDATED_TS);
        assertThat(testSample.getSensorInternalId()).isEqualTo(UPDATED_SENSOR_INTERNAL_ID);
        assertThat(testSample.getSamplingId()).isEqualTo(UPDATED_SAMPLING_ID);
        assertThat(testSample.getExperimentId()).isEqualTo(UPDATED_EXPERIMENT_ID);
        assertThat(testSample.getTargetSystemId()).isEqualTo(UPDATED_TARGET_SYSTEM_ID);
    }

    @Test
    public void updateNonExistingSample() throws Exception {
        int databaseSizeBeforeUpdate = sampleRepository.findAll().size();

        // Create the Sample
        SampleDTO sampleDTO = sampleMapper.toDto(sample);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSampleMockMvc.perform(put("/api/samples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sample in the database
        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteSample() throws Exception {
        // Initialize the database
        sampleRepository.save(sample);

        int databaseSizeBeforeDelete = sampleRepository.findAll().size();

        // Delete the sample
        restSampleMockMvc.perform(delete("/api/samples/{id}", sample.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sample.class);
        Sample sample1 = new Sample();
        sample1.setId("id1");
        Sample sample2 = new Sample();
        sample2.setId(sample1.getId());
        assertThat(sample1).isEqualTo(sample2);
        sample2.setId("id2");
        assertThat(sample1).isNotEqualTo(sample2);
        sample1.setId(null);
        assertThat(sample1).isNotEqualTo(sample2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SampleDTO.class);
        SampleDTO sampleDTO1 = new SampleDTO();
        sampleDTO1.setId("id1");
        SampleDTO sampleDTO2 = new SampleDTO();
        assertThat(sampleDTO1).isNotEqualTo(sampleDTO2);
        sampleDTO2.setId(sampleDTO1.getId());
        assertThat(sampleDTO1).isEqualTo(sampleDTO2);
        sampleDTO2.setId("id2");
        assertThat(sampleDTO1).isNotEqualTo(sampleDTO2);
        sampleDTO1.setId(null);
        assertThat(sampleDTO1).isNotEqualTo(sampleDTO2);
    }
}
