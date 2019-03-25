package co.edu.uniandes.xrepo.web.rest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import co.edu.uniandes.xrepo.XrepoApp;
import co.edu.uniandes.xrepo.domain.Experiment;
import co.edu.uniandes.xrepo.domain.Sampling;
import co.edu.uniandes.xrepo.repository.SamplingRepository;
import co.edu.uniandes.xrepo.service.SamplingService;
import co.edu.uniandes.xrepo.service.dto.SamplingDTO;
import co.edu.uniandes.xrepo.service.mapper.SamplingMapper;
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
 * Test class for the SamplingResource REST controller.
 *
 * @see SamplingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = XrepoApp.class)
public class SamplingResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private SamplingRepository samplingRepository;

    @Autowired
    private SamplingMapper samplingMapper;

    @Autowired
    private SamplingService samplingService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restSamplingMockMvc;

    private Sampling sampling;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SamplingResource samplingResource = new SamplingResource(samplingService);
        this.restSamplingMockMvc = MockMvcBuilders.standaloneSetup(samplingResource)
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
    public static Sampling createEntity() {
        Sampling sampling = new Sampling()
            .name(DEFAULT_NAME)
            .notes(DEFAULT_NOTES)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME);
        // Add required entity
        Experiment experiment = ExperimentResourceIntTest.createEntity();
        experiment.setId("fixed-id-for-tests");
        sampling.setExperiment(experiment);
        return sampling;
    }

    @Before
    public void initTest() {
        samplingRepository.deleteAll();
        sampling = createEntity();
    }

    @Test
    public void createSampling() throws Exception {
        int databaseSizeBeforeCreate = samplingRepository.findAll().size();

        // Create the Sampling
        SamplingDTO samplingDTO = samplingMapper.toDto(sampling);
        restSamplingMockMvc.perform(post("/api/samplings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(samplingDTO)))
            .andExpect(status().isCreated());

        // Validate the Sampling in the database
        List<Sampling> samplingList = samplingRepository.findAll();
        assertThat(samplingList).hasSize(databaseSizeBeforeCreate + 1);
        Sampling testSampling = samplingList.get(samplingList.size() - 1);
        assertThat(testSampling.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSampling.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testSampling.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testSampling.getEndTime()).isEqualTo(DEFAULT_END_TIME);
    }

    @Test
    public void createSamplingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = samplingRepository.findAll().size();

        // Create the Sampling with an existing ID
        sampling.setId("existing_id");
        SamplingDTO samplingDTO = samplingMapper.toDto(sampling);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSamplingMockMvc.perform(post("/api/samplings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(samplingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sampling in the database
        List<Sampling> samplingList = samplingRepository.findAll();
        assertThat(samplingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = samplingRepository.findAll().size();
        // set the field null
        sampling.setName(null);

        // Create the Sampling, which fails.
        SamplingDTO samplingDTO = samplingMapper.toDto(sampling);

        restSamplingMockMvc.perform(post("/api/samplings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(samplingDTO)))
            .andExpect(status().isBadRequest());

        List<Sampling> samplingList = samplingRepository.findAll();
        assertThat(samplingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllSamplings() throws Exception {
        // Initialize the database
        samplingRepository.save(sampling);

        // Get all the samplingList
        restSamplingMockMvc.perform(get("/api/samplings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sampling.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())));
    }
    
    @Test
    public void getSampling() throws Exception {
        // Initialize the database
        samplingRepository.save(sampling);

        // Get the sampling
        MvcResult mvcResult = restSamplingMockMvc.perform(get("/api/samplings/{id}", sampling.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sampling.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

    }

    @Test
    public void getNonExistingSampling() throws Exception {
        // Get the sampling
        restSamplingMockMvc.perform(get("/api/samplings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateSampling() throws Exception {
        // Initialize the database
        samplingRepository.save(sampling);

        int databaseSizeBeforeUpdate = samplingRepository.findAll().size();

        // Update the sampling
        Sampling updatedSampling = samplingRepository.findById(sampling.getId()).get();
        updatedSampling
            .name(UPDATED_NAME)
            .notes(UPDATED_NOTES)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME);
        SamplingDTO samplingDTO = samplingMapper.toDto(updatedSampling);

        restSamplingMockMvc.perform(put("/api/samplings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(samplingDTO)))
            .andExpect(status().isOk());

        // Validate the Sampling in the database
        List<Sampling> samplingList = samplingRepository.findAll();
        assertThat(samplingList).hasSize(databaseSizeBeforeUpdate);
        Sampling testSampling = samplingList.get(samplingList.size() - 1);
        assertThat(testSampling.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSampling.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testSampling.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testSampling.getEndTime()).isEqualTo(UPDATED_END_TIME);
    }

    @Test
    public void updateNonExistingSampling() throws Exception {
        int databaseSizeBeforeUpdate = samplingRepository.findAll().size();

        // Create the Sampling
        SamplingDTO samplingDTO = samplingMapper.toDto(sampling);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSamplingMockMvc.perform(put("/api/samplings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(samplingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sampling in the database
        List<Sampling> samplingList = samplingRepository.findAll();
        assertThat(samplingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteSampling() throws Exception {
        // Initialize the database
        samplingRepository.save(sampling);

        int databaseSizeBeforeDelete = samplingRepository.findAll().size();

        // Delete the sampling
        restSamplingMockMvc.perform(delete("/api/samplings/{id}", sampling.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Sampling> samplingList = samplingRepository.findAll();
        assertThat(samplingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sampling.class);
        Sampling sampling1 = new Sampling();
        sampling1.setId("id1");
        Sampling sampling2 = new Sampling();
        sampling2.setId(sampling1.getId());
        assertThat(sampling1).isEqualTo(sampling2);
        sampling2.setId("id2");
        assertThat(sampling1).isNotEqualTo(sampling2);
        sampling1.setId(null);
        assertThat(sampling1).isNotEqualTo(sampling2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SamplingDTO.class);
        SamplingDTO samplingDTO1 = new SamplingDTO();
        samplingDTO1.setId("id1");
        SamplingDTO samplingDTO2 = new SamplingDTO();
        assertThat(samplingDTO1).isNotEqualTo(samplingDTO2);
        samplingDTO2.setId(samplingDTO1.getId());
        assertThat(samplingDTO1).isEqualTo(samplingDTO2);
        samplingDTO2.setId("id2");
        assertThat(samplingDTO1).isNotEqualTo(samplingDTO2);
        samplingDTO1.setId(null);
        assertThat(samplingDTO1).isNotEqualTo(samplingDTO2);
    }
}
