package co.edu.uniandes.xrepo.web.rest;

import co.edu.uniandes.xrepo.XrepoApp;

import co.edu.uniandes.xrepo.domain.SubSet;
import co.edu.uniandes.xrepo.domain.Laboratory;
import co.edu.uniandes.xrepo.repository.SubSetRepository;
import co.edu.uniandes.xrepo.service.SubSetService;
import co.edu.uniandes.xrepo.service.dto.SubSetDTO;
import co.edu.uniandes.xrepo.service.mapper.SubSetMapper;
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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;


import static co.edu.uniandes.xrepo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.uniandes.xrepo.domain.enumeration.SubSetType;
/**
 * Test class for the SubSetResource REST controller.
 *
 * @see SubSetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = XrepoApp.class)
public class SubSetResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_HDFS_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_FILE_HDFS_LOCATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DOWNLOAD_URL = "AAAAAAAAAA";
    private static final String UPDATED_DOWNLOAD_URL = "BBBBBBBBBB";

    private static final SubSetType DEFAULT_SET_TYPE = SubSetType.TRAINING;
    private static final SubSetType UPDATED_SET_TYPE = SubSetType.VALIDATION;

    @Autowired
    private SubSetRepository subSetRepository;

    @Autowired
    private SubSetMapper subSetMapper;

    @Autowired
    private SubSetService subSetService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restSubSetMockMvc;

    private SubSet subSet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SubSetResource subSetResource = new SubSetResource(subSetService);
        this.restSubSetMockMvc = MockMvcBuilders.standaloneSetup(subSetResource)
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
    public static SubSet createEntity() {
        SubSet subSet = new SubSet()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .fileHdfsLocation(Collections.singletonList(DEFAULT_FILE_HDFS_LOCATION))
            .dateCreated(DEFAULT_DATE_CREATED)
            .downloadUrl(Collections.singletonList(DEFAULT_DOWNLOAD_URL))
            .setType(DEFAULT_SET_TYPE);
        // Add required entity
        Laboratory laboratory = LaboratoryResourceIntTest.createEntity();
        laboratory.setId("fixed-id-for-tests");
        subSet.setLaboratory(laboratory);
        return subSet;
    }

    @Before
    public void initTest() {
        subSetRepository.deleteAll();
        subSet = createEntity();
    }

    @Test
    public void createSubSet() throws Exception {
        int databaseSizeBeforeCreate = subSetRepository.findAll().size();

        // Create the SubSet
        SubSetDTO subSetDTO = subSetMapper.toDto(subSet);
        restSubSetMockMvc.perform(post("/api/sub-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subSetDTO)))
            .andExpect(status().isCreated());

        // Validate the SubSet in the database
        List<SubSet> subSetList = subSetRepository.findAll();
        assertThat(subSetList).hasSize(databaseSizeBeforeCreate + 1);
        SubSet testSubSet = subSetList.get(subSetList.size() - 1);
        assertThat(testSubSet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSubSet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSubSet.getFileHdfsLocation()).isEqualTo(DEFAULT_FILE_HDFS_LOCATION);
        assertThat(testSubSet.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testSubSet.getDownloadUrl()).isEqualTo(DEFAULT_DOWNLOAD_URL);
        assertThat(testSubSet.getSetType()).isEqualTo(DEFAULT_SET_TYPE);
    }

    @Test
    public void createSubSetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subSetRepository.findAll().size();

        // Create the SubSet with an existing ID
        subSet.setId("existing_id");
        SubSetDTO subSetDTO = subSetMapper.toDto(subSet);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubSetMockMvc.perform(post("/api/sub-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subSetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubSet in the database
        List<SubSet> subSetList = subSetRepository.findAll();
        assertThat(subSetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = subSetRepository.findAll().size();
        // set the field null
        subSet.setName(null);

        // Create the SubSet, which fails.
        SubSetDTO subSetDTO = subSetMapper.toDto(subSet);

        restSubSetMockMvc.perform(post("/api/sub-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subSetDTO)))
            .andExpect(status().isBadRequest());

        List<SubSet> subSetList = subSetRepository.findAll();
        assertThat(subSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkFileHdfsLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = subSetRepository.findAll().size();
        // set the field null
        subSet.setFileHdfsLocation(null);

        // Create the SubSet, which fails.
        SubSetDTO subSetDTO = subSetMapper.toDto(subSet);

        restSubSetMockMvc.perform(post("/api/sub-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subSetDTO)))
            .andExpect(status().isBadRequest());

        List<SubSet> subSetList = subSetRepository.findAll();
        assertThat(subSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkSetTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = subSetRepository.findAll().size();
        // set the field null
        subSet.setSetType(null);

        // Create the SubSet, which fails.
        SubSetDTO subSetDTO = subSetMapper.toDto(subSet);

        restSubSetMockMvc.perform(post("/api/sub-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subSetDTO)))
            .andExpect(status().isBadRequest());

        List<SubSet> subSetList = subSetRepository.findAll();
        assertThat(subSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllSubSets() throws Exception {
        // Initialize the database
        subSetRepository.save(subSet);

        // Get all the subSetList
        restSubSetMockMvc.perform(get("/api/sub-sets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subSet.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].fileHdfsLocation").value(hasItem(DEFAULT_FILE_HDFS_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].downloadUrl").value(hasItem(DEFAULT_DOWNLOAD_URL.toString())))
            .andExpect(jsonPath("$.[*].setType").value(hasItem(DEFAULT_SET_TYPE.toString())));
    }

    @Test
    public void getSubSet() throws Exception {
        // Initialize the database
        subSetRepository.save(subSet);

        // Get the subSet
        restSubSetMockMvc.perform(get("/api/sub-sets/{id}", subSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(subSet.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.fileHdfsLocation").value(DEFAULT_FILE_HDFS_LOCATION.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.downloadUrl").value(DEFAULT_DOWNLOAD_URL.toString()))
            .andExpect(jsonPath("$.setType").value(DEFAULT_SET_TYPE.toString()));
    }

    @Test
    public void getNonExistingSubSet() throws Exception {
        // Get the subSet
        restSubSetMockMvc.perform(get("/api/sub-sets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateSubSet() throws Exception {
        // Initialize the database
        subSetRepository.save(subSet);

        int databaseSizeBeforeUpdate = subSetRepository.findAll().size();

        // Update the subSet
        SubSet updatedSubSet = subSetRepository.findById(subSet.getId()).get();
        updatedSubSet
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .fileHdfsLocation(Collections.singletonList(UPDATED_FILE_HDFS_LOCATION))
            .dateCreated(UPDATED_DATE_CREATED)
            .downloadUrl(Collections.singletonList(UPDATED_DOWNLOAD_URL))
            .setType(UPDATED_SET_TYPE);
        SubSetDTO subSetDTO = subSetMapper.toDto(updatedSubSet);

        restSubSetMockMvc.perform(put("/api/sub-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subSetDTO)))
            .andExpect(status().isOk());

        // Validate the SubSet in the database
        List<SubSet> subSetList = subSetRepository.findAll();
        assertThat(subSetList).hasSize(databaseSizeBeforeUpdate);
        SubSet testSubSet = subSetList.get(subSetList.size() - 1);
        assertThat(testSubSet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubSet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSubSet.getFileHdfsLocation()).isEqualTo(UPDATED_FILE_HDFS_LOCATION);
        assertThat(testSubSet.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testSubSet.getDownloadUrl()).isEqualTo(UPDATED_DOWNLOAD_URL);
        assertThat(testSubSet.getSetType()).isEqualTo(UPDATED_SET_TYPE);
    }

    @Test
    public void updateNonExistingSubSet() throws Exception {
        int databaseSizeBeforeUpdate = subSetRepository.findAll().size();

        // Create the SubSet
        SubSetDTO subSetDTO = subSetMapper.toDto(subSet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubSetMockMvc.perform(put("/api/sub-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subSetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubSet in the database
        List<SubSet> subSetList = subSetRepository.findAll();
        assertThat(subSetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteSubSet() throws Exception {
        // Initialize the database
        subSetRepository.save(subSet);

        int databaseSizeBeforeDelete = subSetRepository.findAll().size();

        // Delete the subSet
        restSubSetMockMvc.perform(delete("/api/sub-sets/{id}", subSet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SubSet> subSetList = subSetRepository.findAll();
        assertThat(subSetList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubSet.class);
        SubSet subSet1 = new SubSet();
        subSet1.setId("id1");
        SubSet subSet2 = new SubSet();
        subSet2.setId(subSet1.getId());
        assertThat(subSet1).isEqualTo(subSet2);
        subSet2.setId("id2");
        assertThat(subSet1).isNotEqualTo(subSet2);
        subSet1.setId(null);
        assertThat(subSet1).isNotEqualTo(subSet2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubSetDTO.class);
        SubSetDTO subSetDTO1 = new SubSetDTO();
        subSetDTO1.setId("id1");
        SubSetDTO subSetDTO2 = new SubSetDTO();
        assertThat(subSetDTO1).isNotEqualTo(subSetDTO2);
        subSetDTO2.setId(subSetDTO1.getId());
        assertThat(subSetDTO1).isEqualTo(subSetDTO2);
        subSetDTO2.setId("id2");
        assertThat(subSetDTO1).isNotEqualTo(subSetDTO2);
        subSetDTO1.setId(null);
        assertThat(subSetDTO1).isNotEqualTo(subSetDTO2);
    }
}
