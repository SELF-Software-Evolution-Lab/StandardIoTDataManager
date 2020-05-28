package co.edu.uniandes.xrepo.web.rest;

import co.edu.uniandes.xrepo.XrepoApp;

import co.edu.uniandes.xrepo.domain.Algorithm;
import co.edu.uniandes.xrepo.domain.Laboratory;
import co.edu.uniandes.xrepo.repository.AlgorithmRepository;
import co.edu.uniandes.xrepo.service.AlgorithmService;
import co.edu.uniandes.xrepo.service.dto.AlgorithmDTO;
import co.edu.uniandes.xrepo.service.mapper.AlgorithmMapper;
import co.edu.uniandes.xrepo.web.rest.errors.ExceptionTranslator;

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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static co.edu.uniandes.xrepo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AlgorithmResource REST controller.
 *
 * @see AlgorithmResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = XrepoApp.class)
public class AlgorithmResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_MAPPER_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_MAPPER_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_MAPPER_FILE_URL = "AAAAAAAAAA";
    private static final String UPDATED_MAPPER_FILE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_REDUCER_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_REDUCER_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_REDUCER_FILE_URL = "AAAAAAAAAA";
    private static final String UPDATED_REDUCER_FILE_URL = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_SUCCESSFUL_RUN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_SUCCESSFUL_RUN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private AlgorithmRepository algorithmRepository;

    @Autowired
    private AlgorithmMapper algorithmMapper;

    @Autowired
    private AlgorithmService algorithmService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restAlgorithmMockMvc;

    private Algorithm algorithm;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AlgorithmResource algorithmResource = new AlgorithmResource(algorithmService);
        this.restAlgorithmMockMvc = MockMvcBuilders.standaloneSetup(algorithmResource)
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
    public static Algorithm createEntity() {
        Algorithm algorithm = new Algorithm()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .mapperText(DEFAULT_MAPPER_TEXT)
            .mapperFileUrl(DEFAULT_MAPPER_FILE_URL)
            .reducerText(DEFAULT_REDUCER_TEXT)
            .reducerFileUrl(DEFAULT_REDUCER_FILE_URL)
            .dateCreated(DEFAULT_DATE_CREATED)
            .lastSuccessfulRun(DEFAULT_LAST_SUCCESSFUL_RUN);
        // Add required entity
        Laboratory laboratory = LaboratoryResourceIntTest.createEntity();
        laboratory.setId("fixed-id-for-tests");
        algorithm.setLaboratory(laboratory);
        return algorithm;
    }

    @Before
    public void initTest() {
        algorithmRepository.deleteAll();
        algorithm = createEntity();
    }

    @Test
    public void createAlgorithm() throws Exception {
        int databaseSizeBeforeCreate = algorithmRepository.findAll().size();

        // Create the Algorithm
        AlgorithmDTO algorithmDTO = algorithmMapper.toDto(algorithm);
        restAlgorithmMockMvc.perform(post("/api/algorithms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(algorithmDTO)))
            .andExpect(status().isCreated());

        // Validate the Algorithm in the database
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeCreate + 1);
        Algorithm testAlgorithm = algorithmList.get(algorithmList.size() - 1);
        assertThat(testAlgorithm.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAlgorithm.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAlgorithm.getMapperText()).isEqualTo(DEFAULT_MAPPER_TEXT);
        assertThat(testAlgorithm.getMapperFileUrl()).isEqualTo(DEFAULT_MAPPER_FILE_URL);
        assertThat(testAlgorithm.getReducerText()).isEqualTo(DEFAULT_REDUCER_TEXT);
        assertThat(testAlgorithm.getReducerFileUrl()).isEqualTo(DEFAULT_REDUCER_FILE_URL);
        assertThat(testAlgorithm.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testAlgorithm.getLastSuccessfulRun()).isEqualTo(DEFAULT_LAST_SUCCESSFUL_RUN);
    }

    @Test
    public void createAlgorithmWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = algorithmRepository.findAll().size();

        // Create the Algorithm with an existing ID
        algorithm.setId("existing_id");
        AlgorithmDTO algorithmDTO = algorithmMapper.toDto(algorithm);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlgorithmMockMvc.perform(post("/api/algorithms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(algorithmDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Algorithm in the database
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = algorithmRepository.findAll().size();
        // set the field null
        algorithm.setName(null);

        // Create the Algorithm, which fails.
        AlgorithmDTO algorithmDTO = algorithmMapper.toDto(algorithm);

        restAlgorithmMockMvc.perform(post("/api/algorithms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(algorithmDTO)))
            .andExpect(status().isBadRequest());

        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllAlgorithms() throws Exception {
        // Initialize the database
        algorithmRepository.save(algorithm);

        // Get all the algorithmList
        restAlgorithmMockMvc.perform(get("/api/algorithms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(algorithm.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].mapperText").value(hasItem(DEFAULT_MAPPER_TEXT.toString())))
            .andExpect(jsonPath("$.[*].mapperFileUrl").value(hasItem(DEFAULT_MAPPER_FILE_URL.toString())))
            .andExpect(jsonPath("$.[*].reducerText").value(hasItem(DEFAULT_REDUCER_TEXT.toString())))
            .andExpect(jsonPath("$.[*].reducerFileUrl").value(hasItem(DEFAULT_REDUCER_FILE_URL.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastSuccessfulRun").value(hasItem(DEFAULT_LAST_SUCCESSFUL_RUN.toString())));
    }
    
    @Test
    public void getAlgorithm() throws Exception {
        // Initialize the database
        algorithmRepository.save(algorithm);

        // Get the algorithm
        restAlgorithmMockMvc.perform(get("/api/algorithms/{id}", algorithm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(algorithm.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.mapperText").value(DEFAULT_MAPPER_TEXT.toString()))
            .andExpect(jsonPath("$.mapperFileUrl").value(DEFAULT_MAPPER_FILE_URL.toString()))
            .andExpect(jsonPath("$.reducerText").value(DEFAULT_REDUCER_TEXT.toString()))
            .andExpect(jsonPath("$.reducerFileUrl").value(DEFAULT_REDUCER_FILE_URL.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.lastSuccessfulRun").value(DEFAULT_LAST_SUCCESSFUL_RUN.toString()));
    }

    @Test
    public void getNonExistingAlgorithm() throws Exception {
        // Get the algorithm
        restAlgorithmMockMvc.perform(get("/api/algorithms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateAlgorithm() throws Exception {
        // Initialize the database
        algorithmRepository.save(algorithm);

        int databaseSizeBeforeUpdate = algorithmRepository.findAll().size();

        // Update the algorithm
        Algorithm updatedAlgorithm = algorithmRepository.findById(algorithm.getId()).get();
        updatedAlgorithm
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .mapperText(UPDATED_MAPPER_TEXT)
            .mapperFileUrl(UPDATED_MAPPER_FILE_URL)
            .reducerText(UPDATED_REDUCER_TEXT)
            .reducerFileUrl(UPDATED_REDUCER_FILE_URL)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastSuccessfulRun(UPDATED_LAST_SUCCESSFUL_RUN);
        AlgorithmDTO algorithmDTO = algorithmMapper.toDto(updatedAlgorithm);

        restAlgorithmMockMvc.perform(put("/api/algorithms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(algorithmDTO)))
            .andExpect(status().isOk());

        // Validate the Algorithm in the database
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeUpdate);
        Algorithm testAlgorithm = algorithmList.get(algorithmList.size() - 1);
        assertThat(testAlgorithm.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAlgorithm.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAlgorithm.getMapperText()).isEqualTo(UPDATED_MAPPER_TEXT);
        assertThat(testAlgorithm.getMapperFileUrl()).isEqualTo(UPDATED_MAPPER_FILE_URL);
        assertThat(testAlgorithm.getReducerText()).isEqualTo(UPDATED_REDUCER_TEXT);
        assertThat(testAlgorithm.getReducerFileUrl()).isEqualTo(UPDATED_REDUCER_FILE_URL);
        assertThat(testAlgorithm.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testAlgorithm.getLastSuccessfulRun()).isEqualTo(UPDATED_LAST_SUCCESSFUL_RUN);
    }

    @Test
    public void updateNonExistingAlgorithm() throws Exception {
        int databaseSizeBeforeUpdate = algorithmRepository.findAll().size();

        // Create the Algorithm
        AlgorithmDTO algorithmDTO = algorithmMapper.toDto(algorithm);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlgorithmMockMvc.perform(put("/api/algorithms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(algorithmDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Algorithm in the database
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteAlgorithm() throws Exception {
        // Initialize the database
        algorithmRepository.save(algorithm);

        int databaseSizeBeforeDelete = algorithmRepository.findAll().size();

        // Delete the algorithm
        restAlgorithmMockMvc.perform(delete("/api/algorithms/{id}", algorithm.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Algorithm> algorithmList = algorithmRepository.findAll();
        assertThat(algorithmList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Algorithm.class);
        Algorithm algorithm1 = new Algorithm();
        algorithm1.setId("id1");
        Algorithm algorithm2 = new Algorithm();
        algorithm2.setId(algorithm1.getId());
        assertThat(algorithm1).isEqualTo(algorithm2);
        algorithm2.setId("id2");
        assertThat(algorithm1).isNotEqualTo(algorithm2);
        algorithm1.setId(null);
        assertThat(algorithm1).isNotEqualTo(algorithm2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlgorithmDTO.class);
        AlgorithmDTO algorithmDTO1 = new AlgorithmDTO();
        algorithmDTO1.setId("id1");
        AlgorithmDTO algorithmDTO2 = new AlgorithmDTO();
        assertThat(algorithmDTO1).isNotEqualTo(algorithmDTO2);
        algorithmDTO2.setId(algorithmDTO1.getId());
        assertThat(algorithmDTO1).isEqualTo(algorithmDTO2);
        algorithmDTO2.setId("id2");
        assertThat(algorithmDTO1).isNotEqualTo(algorithmDTO2);
        algorithmDTO1.setId(null);
        assertThat(algorithmDTO1).isNotEqualTo(algorithmDTO2);
    }
}
