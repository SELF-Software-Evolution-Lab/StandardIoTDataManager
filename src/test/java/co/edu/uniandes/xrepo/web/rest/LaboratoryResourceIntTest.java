package co.edu.uniandes.xrepo.web.rest;

import co.edu.uniandes.xrepo.XrepoApp;

import co.edu.uniandes.xrepo.domain.Laboratory;
import co.edu.uniandes.xrepo.domain.Sampling;
import co.edu.uniandes.xrepo.repository.LaboratoryRepository;
import co.edu.uniandes.xrepo.service.LaboratoryService;
import co.edu.uniandes.xrepo.service.dto.LaboratoryDTO;
import co.edu.uniandes.xrepo.service.mapper.LaboratoryMapper;
import co.edu.uniandes.xrepo.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static co.edu.uniandes.xrepo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LaboratoryResource REST controller.
 *
 * @see LaboratoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = XrepoApp.class)
public class LaboratoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_SHARE_URL = "AAAAAAAAAA";
    private static final String UPDATED_SHARE_URL = "BBBBBBBBBB";

    private static final Instant DEFAULT_SHARE_VALID_THRU = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHARE_VALID_THRU = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ArrayList<String> DEFAULT_TAGS = new ArrayList<>(Arrays.asList("London", "Tokyo", "New York"));
    private static final ArrayList<String> UPDATED_TAGS = new ArrayList<>(Arrays.asList("Paris", "Rio", "Berlin"));;

    private final String hdfsResultsLocation = "";

    private final String nfsGatewayMountPoint = "";

    @Autowired
    private LaboratoryRepository laboratoryRepository;

    @Autowired
    private LaboratoryMapper laboratoryMapper;

    @Autowired
    private LaboratoryService laboratoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restLaboratoryMockMvc;

    private Laboratory laboratory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LaboratoryResource laboratoryResource = new LaboratoryResource(laboratoryService, hdfsResultsLocation, nfsGatewayMountPoint);
        this.restLaboratoryMockMvc = MockMvcBuilders.standaloneSetup(laboratoryResource)
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
    public static Laboratory createEntity() {
        Laboratory laboratory = new Laboratory()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .dateCreated(DEFAULT_DATE_CREATED)
            .shareUrl(DEFAULT_SHARE_URL)
            .shareValidThru(DEFAULT_SHARE_VALID_THRU)
            .tags(DEFAULT_TAGS);
        // Add required entity
        Sampling sampling = SamplingResourceIntTest.createEntity();
        sampling.setId("fixed-id-for-tests");
        laboratory.setSampling(sampling);
        return laboratory;
    }

    @Before
    public void initTest() {
        laboratoryRepository.deleteAll();
        laboratory = createEntity();
    }

    @Test
    public void createLaboratory() throws Exception {
        int databaseSizeBeforeCreate = laboratoryRepository.findAll().size();

        // Create the Laboratory
        LaboratoryDTO laboratoryDTO = laboratoryMapper.toDto(laboratory);
        restLaboratoryMockMvc.perform(post("/api/laboratories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(laboratoryDTO)))
            .andExpect(status().isCreated());

        // Validate the Laboratory in the database
        List<Laboratory> laboratoryList = laboratoryRepository.findAll();
        assertThat(laboratoryList).hasSize(databaseSizeBeforeCreate + 1);
        Laboratory testLaboratory = laboratoryList.get(laboratoryList.size() - 1);
        assertThat(testLaboratory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLaboratory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLaboratory.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testLaboratory.getShareUrl()).isEqualTo(DEFAULT_SHARE_URL);
        assertThat(testLaboratory.getShareValidThru()).isEqualTo(DEFAULT_SHARE_VALID_THRU);
        assertThat(testLaboratory.getTags()).isEqualTo(DEFAULT_TAGS);
    }

    @Test
    public void createLaboratoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = laboratoryRepository.findAll().size();

        // Create the Laboratory with an existing ID
        laboratory.setId("existing_id");
        LaboratoryDTO laboratoryDTO = laboratoryMapper.toDto(laboratory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLaboratoryMockMvc.perform(post("/api/laboratories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(laboratoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Laboratory in the database
        List<Laboratory> laboratoryList = laboratoryRepository.findAll();
        assertThat(laboratoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = laboratoryRepository.findAll().size();
        // set the field null
        laboratory.setName(null);

        // Create the Laboratory, which fails.
        LaboratoryDTO laboratoryDTO = laboratoryMapper.toDto(laboratory);

        restLaboratoryMockMvc.perform(post("/api/laboratories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(laboratoryDTO)))
            .andExpect(status().isBadRequest());

        List<Laboratory> laboratoryList = laboratoryRepository.findAll();
        assertThat(laboratoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllLaboratories() throws Exception {
        // Initialize the database
        laboratoryRepository.save(laboratory);

        // Get all the laboratoryList
        restLaboratoryMockMvc.perform(get("/api/laboratories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(laboratory.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].shareUrl").value(hasItem(DEFAULT_SHARE_URL.toString())))
            .andExpect(jsonPath("$.[*].shareValidThru").value(hasItem(DEFAULT_SHARE_VALID_THRU.toString())))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS.toString())));
    }

    @Test
    public void getLaboratory() throws Exception {
        // Initialize the database
        laboratoryRepository.save(laboratory);

        // Get the laboratory
        restLaboratoryMockMvc.perform(get("/api/laboratories/{id}", laboratory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(laboratory.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.shareUrl").value(DEFAULT_SHARE_URL.toString()))
            .andExpect(jsonPath("$.shareValidThru").value(DEFAULT_SHARE_VALID_THRU.toString()))
            .andExpect(jsonPath("$.tags").value(DEFAULT_TAGS.toString()));
    }

    @Test
    public void getNonExistingLaboratory() throws Exception {
        // Get the laboratory
        restLaboratoryMockMvc.perform(get("/api/laboratories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateLaboratory() throws Exception {
        // Initialize the database
        laboratoryRepository.save(laboratory);

        int databaseSizeBeforeUpdate = laboratoryRepository.findAll().size();

        // Update the laboratory
        Laboratory updatedLaboratory = laboratoryRepository.findById(laboratory.getId()).get();
        updatedLaboratory
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .dateCreated(UPDATED_DATE_CREATED)
            .shareUrl(UPDATED_SHARE_URL)
            .shareValidThru(UPDATED_SHARE_VALID_THRU)
            .tags(UPDATED_TAGS);
        LaboratoryDTO laboratoryDTO = laboratoryMapper.toDto(updatedLaboratory);

        restLaboratoryMockMvc.perform(put("/api/laboratories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(laboratoryDTO)))
            .andExpect(status().isOk());

        // Validate the Laboratory in the database
        List<Laboratory> laboratoryList = laboratoryRepository.findAll();
        assertThat(laboratoryList).hasSize(databaseSizeBeforeUpdate);
        Laboratory testLaboratory = laboratoryList.get(laboratoryList.size() - 1);
        assertThat(testLaboratory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLaboratory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLaboratory.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testLaboratory.getShareUrl()).isEqualTo(UPDATED_SHARE_URL);
        assertThat(testLaboratory.getShareValidThru()).isEqualTo(UPDATED_SHARE_VALID_THRU);
        assertThat(testLaboratory.getTags()).isEqualTo(UPDATED_TAGS);
    }

    @Test
    public void updateNonExistingLaboratory() throws Exception {
        int databaseSizeBeforeUpdate = laboratoryRepository.findAll().size();

        // Create the Laboratory
        LaboratoryDTO laboratoryDTO = laboratoryMapper.toDto(laboratory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLaboratoryMockMvc.perform(put("/api/laboratories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(laboratoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Laboratory in the database
        List<Laboratory> laboratoryList = laboratoryRepository.findAll();
        assertThat(laboratoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteLaboratory() throws Exception {
        // Initialize the database
        laboratoryRepository.save(laboratory);

        int databaseSizeBeforeDelete = laboratoryRepository.findAll().size();

        // Delete the laboratory
        restLaboratoryMockMvc.perform(delete("/api/laboratories/{id}", laboratory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Laboratory> laboratoryList = laboratoryRepository.findAll();
        assertThat(laboratoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Laboratory.class);
        Laboratory laboratory1 = new Laboratory();
        laboratory1.setId("id1");
        Laboratory laboratory2 = new Laboratory();
        laboratory2.setId(laboratory1.getId());
        assertThat(laboratory1).isEqualTo(laboratory2);
        laboratory2.setId("id2");
        assertThat(laboratory1).isNotEqualTo(laboratory2);
        laboratory1.setId(null);
        assertThat(laboratory1).isNotEqualTo(laboratory2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LaboratoryDTO.class);
        LaboratoryDTO laboratoryDTO1 = new LaboratoryDTO();
        laboratoryDTO1.setId("id1");
        LaboratoryDTO laboratoryDTO2 = new LaboratoryDTO();
        assertThat(laboratoryDTO1).isNotEqualTo(laboratoryDTO2);
        laboratoryDTO2.setId(laboratoryDTO1.getId());
        assertThat(laboratoryDTO1).isEqualTo(laboratoryDTO2);
        laboratoryDTO2.setId("id2");
        assertThat(laboratoryDTO1).isNotEqualTo(laboratoryDTO2);
        laboratoryDTO1.setId(null);
        assertThat(laboratoryDTO1).isNotEqualTo(laboratoryDTO2);
    }
}
