package co.edu.uniandes.xrepo.web.rest;

import co.edu.uniandes.xrepo.XrepoApp;

import co.edu.uniandes.xrepo.domain.SamplesFiles;
import co.edu.uniandes.xrepo.repository.SamplesFilesRepository;
import co.edu.uniandes.xrepo.service.SamplesFilesService;
import co.edu.uniandes.xrepo.service.dto.SamplesFilesDTO;
import co.edu.uniandes.xrepo.service.mapper.SamplesFilesMapper;
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
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static co.edu.uniandes.xrepo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SamplesFilesResource REST controller.
 *
 * @see SamplesFilesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = XrepoApp.class)
public class SamplesFilesResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_TYPE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_SIZE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SIZE = new BigDecimal(2);

    private static final Integer DEFAULT_STATE = 1;
    private static final Integer UPDATED_STATE = 2;

    private static final String DEFAULT_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_RESULT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATE_DATE_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATE_DATE_TIME = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private SamplesFilesRepository samplesFilesRepository;

    @Autowired
    private SamplesFilesMapper samplesFilesMapper;

    @Autowired
    private SamplesFilesService samplesFilesService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restSamplesFilesMockMvc;

    private SamplesFiles samplesFiles;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SamplesFilesResource samplesFilesResource = new SamplesFilesResource(samplesFilesService);
        this.restSamplesFilesMockMvc = MockMvcBuilders.standaloneSetup(samplesFilesResource)
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
    public static SamplesFiles createEntity() {
        SamplesFiles samplesFiles = new SamplesFiles()
            .name(DEFAULT_NAME)
            .path(DEFAULT_PATH)
            .contentType(DEFAULT_CONTENT_TYPE)
            .size(DEFAULT_SIZE)
            .state(DEFAULT_STATE)
            .result(DEFAULT_RESULT)
            .createDateTime(DEFAULT_CREATE_DATE_TIME)
            .updateDateTime(DEFAULT_UPDATE_DATE_TIME);
        return samplesFiles;
    }

    @Before
    public void initTest() {
        samplesFilesRepository.deleteAll();
        samplesFiles = createEntity();
    }

    @Test
    public void createSamplesFiles() throws Exception {
        int databaseSizeBeforeCreate = samplesFilesRepository.findAll().size();

        // Create the SamplesFiles
        SamplesFilesDTO samplesFilesDTO = samplesFilesMapper.toDto(samplesFiles);
        restSamplesFilesMockMvc.perform(post("/api/samples-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(samplesFilesDTO)))
            .andExpect(status().isCreated());

        // Validate the SamplesFiles in the database
        List<SamplesFiles> samplesFilesList = samplesFilesRepository.findAll();
        assertThat(samplesFilesList).hasSize(databaseSizeBeforeCreate + 1);
        SamplesFiles testSamplesFiles = samplesFilesList.get(samplesFilesList.size() - 1);
        assertThat(testSamplesFiles.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSamplesFiles.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testSamplesFiles.getContentType()).isEqualTo(DEFAULT_CONTENT_TYPE);
        assertThat(testSamplesFiles.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testSamplesFiles.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testSamplesFiles.getResult()).isEqualTo(DEFAULT_RESULT);
        assertThat(testSamplesFiles.getCreateDateTime()).isEqualTo(DEFAULT_CREATE_DATE_TIME);
        assertThat(testSamplesFiles.getUpdateDateTime()).isEqualTo(DEFAULT_UPDATE_DATE_TIME);
    }

    @Test
    public void createSamplesFilesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = samplesFilesRepository.findAll().size();

        // Create the SamplesFiles with an existing ID
        samplesFiles.setId("existing_id");
        SamplesFilesDTO samplesFilesDTO = samplesFilesMapper.toDto(samplesFiles);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSamplesFilesMockMvc.perform(post("/api/samples-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(samplesFilesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SamplesFiles in the database
        List<SamplesFiles> samplesFilesList = samplesFilesRepository.findAll();
        assertThat(samplesFilesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllSamplesFiles() throws Exception {
        // Initialize the database
        samplesFilesRepository.save(samplesFiles);

        // Get all the samplesFilesList
        restSamplesFilesMockMvc.perform(get("/api/samples-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(samplesFiles.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.toString())))
            .andExpect(jsonPath("$.[*].createDateTime").value(hasItem(DEFAULT_CREATE_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateDateTime").value(hasItem(DEFAULT_UPDATE_DATE_TIME.toString())));
    }
    
    @Test
    public void getSamplesFiles() throws Exception {
        // Initialize the database
        samplesFilesRepository.save(samplesFiles);

        // Get the samplesFiles
        restSamplesFilesMockMvc.perform(get("/api/samples-files/{id}", samplesFiles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(samplesFiles.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH.toString()))
            .andExpect(jsonPath("$.contentType").value(DEFAULT_CONTENT_TYPE.toString()))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE.intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT.toString()))
            .andExpect(jsonPath("$.createDateTime").value(DEFAULT_CREATE_DATE_TIME.toString()))
            .andExpect(jsonPath("$.updateDateTime").value(DEFAULT_UPDATE_DATE_TIME.toString()));
    }

    @Test
    public void getNonExistingSamplesFiles() throws Exception {
        // Get the samplesFiles
        restSamplesFilesMockMvc.perform(get("/api/samples-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateSamplesFiles() throws Exception {
        // Initialize the database
        samplesFilesRepository.save(samplesFiles);

        int databaseSizeBeforeUpdate = samplesFilesRepository.findAll().size();

        // Update the samplesFiles
        SamplesFiles updatedSamplesFiles = samplesFilesRepository.findById(samplesFiles.getId()).get();
        updatedSamplesFiles
            .name(UPDATED_NAME)
            .path(UPDATED_PATH)
            .contentType(UPDATED_CONTENT_TYPE)
            .size(UPDATED_SIZE)
            .state(UPDATED_STATE)
            .result(UPDATED_RESULT)
            .createDateTime(UPDATED_CREATE_DATE_TIME)
            .updateDateTime(UPDATED_UPDATE_DATE_TIME);
        SamplesFilesDTO samplesFilesDTO = samplesFilesMapper.toDto(updatedSamplesFiles);

        restSamplesFilesMockMvc.perform(put("/api/samples-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(samplesFilesDTO)))
            .andExpect(status().isOk());

        // Validate the SamplesFiles in the database
        List<SamplesFiles> samplesFilesList = samplesFilesRepository.findAll();
        assertThat(samplesFilesList).hasSize(databaseSizeBeforeUpdate);
        SamplesFiles testSamplesFiles = samplesFilesList.get(samplesFilesList.size() - 1);
        assertThat(testSamplesFiles.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSamplesFiles.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testSamplesFiles.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testSamplesFiles.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testSamplesFiles.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testSamplesFiles.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testSamplesFiles.getCreateDateTime()).isEqualTo(UPDATED_CREATE_DATE_TIME);
        assertThat(testSamplesFiles.getUpdateDateTime()).isEqualTo(UPDATED_UPDATE_DATE_TIME);
    }

    @Test
    public void updateNonExistingSamplesFiles() throws Exception {
        int databaseSizeBeforeUpdate = samplesFilesRepository.findAll().size();

        // Create the SamplesFiles
        SamplesFilesDTO samplesFilesDTO = samplesFilesMapper.toDto(samplesFiles);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSamplesFilesMockMvc.perform(put("/api/samples-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(samplesFilesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SamplesFiles in the database
        List<SamplesFiles> samplesFilesList = samplesFilesRepository.findAll();
        assertThat(samplesFilesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteSamplesFiles() throws Exception {
        // Initialize the database
        samplesFilesRepository.save(samplesFiles);

        int databaseSizeBeforeDelete = samplesFilesRepository.findAll().size();

        // Delete the samplesFiles
        restSamplesFilesMockMvc.perform(delete("/api/samples-files/{id}", samplesFiles.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SamplesFiles> samplesFilesList = samplesFilesRepository.findAll();
        assertThat(samplesFilesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SamplesFiles.class);
        SamplesFiles samplesFiles1 = new SamplesFiles();
        samplesFiles1.setId("id1");
        SamplesFiles samplesFiles2 = new SamplesFiles();
        samplesFiles2.setId(samplesFiles1.getId());
        assertThat(samplesFiles1).isEqualTo(samplesFiles2);
        samplesFiles2.setId("id2");
        assertThat(samplesFiles1).isNotEqualTo(samplesFiles2);
        samplesFiles1.setId(null);
        assertThat(samplesFiles1).isNotEqualTo(samplesFiles2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SamplesFilesDTO.class);
        SamplesFilesDTO samplesFilesDTO1 = new SamplesFilesDTO();
        samplesFilesDTO1.setId("id1");
        SamplesFilesDTO samplesFilesDTO2 = new SamplesFilesDTO();
        assertThat(samplesFilesDTO1).isNotEqualTo(samplesFilesDTO2);
        samplesFilesDTO2.setId(samplesFilesDTO1.getId());
        assertThat(samplesFilesDTO1).isEqualTo(samplesFilesDTO2);
        samplesFilesDTO2.setId("id2");
        assertThat(samplesFilesDTO1).isNotEqualTo(samplesFilesDTO2);
        samplesFilesDTO1.setId(null);
        assertThat(samplesFilesDTO1).isNotEqualTo(samplesFilesDTO2);
    }
}
