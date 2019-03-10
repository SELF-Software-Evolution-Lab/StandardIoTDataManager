package co.edu.uniandes.xrepo.web.rest;

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
import co.edu.uniandes.xrepo.domain.Experiment;
import co.edu.uniandes.xrepo.domain.TargetSystem;
import co.edu.uniandes.xrepo.repository.ExperimentRepository;
import co.edu.uniandes.xrepo.service.ExperimentService;
import co.edu.uniandes.xrepo.service.dto.ExperimentDTO;
import co.edu.uniandes.xrepo.service.mapper.ExperimentMapper;
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
 * Test class for the ExperimentResource REST controller.
 *
 * @see ExperimentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = XrepoApp.class)
public class ExperimentResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private ExperimentMapper experimentMapper;

    @Autowired
    private ExperimentService experimentService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restExperimentMockMvc;

    private Experiment experiment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExperimentResource experimentResource = new ExperimentResource(experimentService);
        this.restExperimentMockMvc = MockMvcBuilders.standaloneSetup(experimentResource)
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
    public static Experiment createEntity() {
        Experiment experiment = new Experiment()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .notes(DEFAULT_NOTES);
        // Add required entity
        TargetSystem targetSystem = TargetSystemResourceIntTest.createEntity();
        targetSystem.setId("fixed-id-for-tests");
        experiment.setSystem(targetSystem);
        return experiment;
    }

    @Before
    public void initTest() {
        experimentRepository.deleteAll();
        experiment = createEntity();
    }

    @Test
    public void createExperiment() throws Exception {
        int databaseSizeBeforeCreate = experimentRepository.findAll().size();

        // Create the Experiment
        ExperimentDTO experimentDTO = experimentMapper.toDto(experiment);
        restExperimentMockMvc.perform(post("/api/experiments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experimentDTO)))
            .andExpect(status().isCreated());

        // Validate the Experiment in the database
        List<Experiment> experimentList = experimentRepository.findAll();
        assertThat(experimentList).hasSize(databaseSizeBeforeCreate + 1);
        Experiment testExperiment = experimentList.get(experimentList.size() - 1);
        assertThat(testExperiment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExperiment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testExperiment.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    public void createExperimentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = experimentRepository.findAll().size();

        // Create the Experiment with an existing ID
        experiment.setId("existing_id");
        ExperimentDTO experimentDTO = experimentMapper.toDto(experiment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExperimentMockMvc.perform(post("/api/experiments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experimentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Experiment in the database
        List<Experiment> experimentList = experimentRepository.findAll();
        assertThat(experimentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = experimentRepository.findAll().size();
        // set the field null
        experiment.setName(null);

        // Create the Experiment, which fails.
        ExperimentDTO experimentDTO = experimentMapper.toDto(experiment);

        restExperimentMockMvc.perform(post("/api/experiments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experimentDTO)))
            .andExpect(status().isBadRequest());

        List<Experiment> experimentList = experimentRepository.findAll();
        assertThat(experimentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllExperiments() throws Exception {
        // Initialize the database
        experimentRepository.save(experiment);

        // Get all the experimentList
        restExperimentMockMvc.perform(get("/api/experiments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(experiment.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }
    
    @Test
    public void getExperiment() throws Exception {
        // Initialize the database
        experimentRepository.save(experiment);

        // Get the experiment
        restExperimentMockMvc.perform(get("/api/experiments/{id}", experiment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(experiment.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    public void getNonExistingExperiment() throws Exception {
        // Get the experiment
        restExperimentMockMvc.perform(get("/api/experiments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateExperiment() throws Exception {
        // Initialize the database
        experimentRepository.save(experiment);

        int databaseSizeBeforeUpdate = experimentRepository.findAll().size();

        // Update the experiment
        Experiment updatedExperiment = experimentRepository.findById(experiment.getId()).get();
        updatedExperiment
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES);
        ExperimentDTO experimentDTO = experimentMapper.toDto(updatedExperiment);

        restExperimentMockMvc.perform(put("/api/experiments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experimentDTO)))
            .andExpect(status().isOk());

        // Validate the Experiment in the database
        List<Experiment> experimentList = experimentRepository.findAll();
        assertThat(experimentList).hasSize(databaseSizeBeforeUpdate);
        Experiment testExperiment = experimentList.get(experimentList.size() - 1);
        assertThat(testExperiment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExperiment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExperiment.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    public void updateNonExistingExperiment() throws Exception {
        int databaseSizeBeforeUpdate = experimentRepository.findAll().size();

        // Create the Experiment
        ExperimentDTO experimentDTO = experimentMapper.toDto(experiment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExperimentMockMvc.perform(put("/api/experiments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(experimentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Experiment in the database
        List<Experiment> experimentList = experimentRepository.findAll();
        assertThat(experimentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteExperiment() throws Exception {
        // Initialize the database
        experimentRepository.save(experiment);

        int databaseSizeBeforeDelete = experimentRepository.findAll().size();

        // Delete the experiment
        restExperimentMockMvc.perform(delete("/api/experiments/{id}", experiment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Experiment> experimentList = experimentRepository.findAll();
        assertThat(experimentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Experiment.class);
        Experiment experiment1 = new Experiment();
        experiment1.setId("id1");
        Experiment experiment2 = new Experiment();
        experiment2.setId(experiment1.getId());
        assertThat(experiment1).isEqualTo(experiment2);
        experiment2.setId("id2");
        assertThat(experiment1).isNotEqualTo(experiment2);
        experiment1.setId(null);
        assertThat(experiment1).isNotEqualTo(experiment2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExperimentDTO.class);
        ExperimentDTO experimentDTO1 = new ExperimentDTO();
        experimentDTO1.setId("id1");
        ExperimentDTO experimentDTO2 = new ExperimentDTO();
        assertThat(experimentDTO1).isNotEqualTo(experimentDTO2);
        experimentDTO2.setId(experimentDTO1.getId());
        assertThat(experimentDTO1).isEqualTo(experimentDTO2);
        experimentDTO2.setId("id2");
        assertThat(experimentDTO1).isNotEqualTo(experimentDTO2);
        experimentDTO1.setId(null);
        assertThat(experimentDTO1).isNotEqualTo(experimentDTO2);
    }
}
