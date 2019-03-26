package co.edu.uniandes.xrepo.web.rest;

import co.edu.uniandes.xrepo.XrepoApp;

import co.edu.uniandes.xrepo.domain.SampleFiles;
import co.edu.uniandes.xrepo.repository.SampleFilesRepository;
import co.edu.uniandes.xrepo.service.SampleFilesService;
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

import java.util.List;


import static co.edu.uniandes.xrepo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SampleFilesResource REST controller.
 *
 * @see SampleFilesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = XrepoApp.class)
public class SampleFilesResourceIntTest {

    private static final byte[] DEFAULT_PATH = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PATH = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PATH_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PATH_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_SIZE = 1L;
    private static final Long UPDATED_SIZE = 2L;

    @Autowired
    private SampleFilesRepository sampleFilesRepository;

    @Autowired
    private SampleFilesService sampleFilesService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restSampleFilesMockMvc;

    private SampleFiles sampleFiles;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SampleFilesResource sampleFilesResource = new SampleFilesResource(sampleFilesService);
        this.restSampleFilesMockMvc = MockMvcBuilders.standaloneSetup(sampleFilesResource)
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
    public static SampleFiles createEntity() {
        SampleFiles sampleFiles = new SampleFiles()
            .path(DEFAULT_PATH)
            .pathContentType(DEFAULT_PATH_CONTENT_TYPE)
            .name(DEFAULT_NAME)
            .size(DEFAULT_SIZE);
        return sampleFiles;
    }

    @Before
    public void initTest() {
        sampleFilesRepository.deleteAll();
        sampleFiles = createEntity();
    }

    @Test
    public void createSampleFiles() throws Exception {
        int databaseSizeBeforeCreate = sampleFilesRepository.findAll().size();

        // Create the SampleFiles
        restSampleFilesMockMvc.perform(post("/api/sample-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleFiles)))
            .andExpect(status().isCreated());

        // Validate the SampleFiles in the database
        List<SampleFiles> sampleFilesList = sampleFilesRepository.findAll();
        assertThat(sampleFilesList).hasSize(databaseSizeBeforeCreate + 1);
        SampleFiles testSampleFiles = sampleFilesList.get(sampleFilesList.size() - 1);
        assertThat(testSampleFiles.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testSampleFiles.getPathContentType()).isEqualTo(DEFAULT_PATH_CONTENT_TYPE);
        assertThat(testSampleFiles.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSampleFiles.getSize()).isEqualTo(DEFAULT_SIZE);
    }

    @Test
    public void createSampleFilesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sampleFilesRepository.findAll().size();

        // Create the SampleFiles with an existing ID
        sampleFiles.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restSampleFilesMockMvc.perform(post("/api/sample-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleFiles)))
            .andExpect(status().isBadRequest());

        // Validate the SampleFiles in the database
        List<SampleFiles> sampleFilesList = sampleFilesRepository.findAll();
        assertThat(sampleFilesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllSampleFiles() throws Exception {
        // Initialize the database
        sampleFilesRepository.save(sampleFiles);

        // Get all the sampleFilesList
        restSampleFilesMockMvc.perform(get("/api/sample-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sampleFiles.getId())))
            .andExpect(jsonPath("$.[*].pathContentType").value(hasItem(DEFAULT_PATH_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(Base64Utils.encodeToString(DEFAULT_PATH))))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.intValue())));
    }
    
    @Test
    public void getSampleFiles() throws Exception {
        // Initialize the database
        sampleFilesRepository.save(sampleFiles);

        // Get the sampleFiles
        restSampleFilesMockMvc.perform(get("/api/sample-files/{id}", sampleFiles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sampleFiles.getId()))
            .andExpect(jsonPath("$.pathContentType").value(DEFAULT_PATH_CONTENT_TYPE))
            .andExpect(jsonPath("$.path").value(Base64Utils.encodeToString(DEFAULT_PATH)))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE.intValue()));
    }

    @Test
    public void getNonExistingSampleFiles() throws Exception {
        // Get the sampleFiles
        restSampleFilesMockMvc.perform(get("/api/sample-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateSampleFiles() throws Exception {
        // Initialize the database
        sampleFilesService.save(sampleFiles);

        int databaseSizeBeforeUpdate = sampleFilesRepository.findAll().size();

        // Update the sampleFiles
        SampleFiles updatedSampleFiles = sampleFilesRepository.findById(sampleFiles.getId()).get();
        updatedSampleFiles
            .path(UPDATED_PATH)
            .pathContentType(UPDATED_PATH_CONTENT_TYPE)
            .name(UPDATED_NAME)
            .size(UPDATED_SIZE);

        restSampleFilesMockMvc.perform(put("/api/sample-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSampleFiles)))
            .andExpect(status().isOk());

        // Validate the SampleFiles in the database
        List<SampleFiles> sampleFilesList = sampleFilesRepository.findAll();
        assertThat(sampleFilesList).hasSize(databaseSizeBeforeUpdate);
        SampleFiles testSampleFiles = sampleFilesList.get(sampleFilesList.size() - 1);
        assertThat(testSampleFiles.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testSampleFiles.getPathContentType()).isEqualTo(UPDATED_PATH_CONTENT_TYPE);
        assertThat(testSampleFiles.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSampleFiles.getSize()).isEqualTo(UPDATED_SIZE);
    }

    @Test
    public void updateNonExistingSampleFiles() throws Exception {
        int databaseSizeBeforeUpdate = sampleFilesRepository.findAll().size();

        // Create the SampleFiles

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSampleFilesMockMvc.perform(put("/api/sample-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sampleFiles)))
            .andExpect(status().isBadRequest());

        // Validate the SampleFiles in the database
        List<SampleFiles> sampleFilesList = sampleFilesRepository.findAll();
        assertThat(sampleFilesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteSampleFiles() throws Exception {
        // Initialize the database
        sampleFilesService.save(sampleFiles);

        int databaseSizeBeforeDelete = sampleFilesRepository.findAll().size();

        // Delete the sampleFiles
        restSampleFilesMockMvc.perform(delete("/api/sample-files/{id}", sampleFiles.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SampleFiles> sampleFilesList = sampleFilesRepository.findAll();
        assertThat(sampleFilesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SampleFiles.class);
        SampleFiles sampleFiles1 = new SampleFiles();
        sampleFiles1.setId("id1");
        SampleFiles sampleFiles2 = new SampleFiles();
        sampleFiles2.setId(sampleFiles1.getId());
        assertThat(sampleFiles1).isEqualTo(sampleFiles2);
        sampleFiles2.setId("id2");
        assertThat(sampleFiles1).isNotEqualTo(sampleFiles2);
        sampleFiles1.setId(null);
        assertThat(sampleFiles1).isNotEqualTo(sampleFiles2);
    }
}
