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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import co.edu.uniandes.xrepo.XrepoApp;
import co.edu.uniandes.xrepo.domain.Organization;
import co.edu.uniandes.xrepo.domain.TargetSystem;
import co.edu.uniandes.xrepo.repository.TargetSystemRepository;
import co.edu.uniandes.xrepo.service.TargetSystemService;
import co.edu.uniandes.xrepo.service.dto.TargetSystemDTO;
import co.edu.uniandes.xrepo.service.mapper.TargetSystemMapper;
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
 * Test class for the TargetSystemResource REST controller.
 *
 * @see TargetSystemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = XrepoApp.class)
public class TargetSystemResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    @Autowired
    private TargetSystemRepository targetSystemRepository;

    @Autowired
    private TargetSystemMapper targetSystemMapper;

    @Autowired
    private TargetSystemService targetSystemService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restTargetSystemMockMvc;

    private TargetSystem targetSystem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TargetSystemResource targetSystemResource = new TargetSystemResource(targetSystemService);
        this.restTargetSystemMockMvc = MockMvcBuilders.standaloneSetup(targetSystemResource)
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
    public static TargetSystem createEntity() {
        TargetSystem targetSystem = new TargetSystem()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .created(DEFAULT_CREATED)
            .createdBy(DEFAULT_CREATED_BY);
        // Add required entity
        Organization organization = OrganizationResourceIntTest.createEntity();
        organization.setId("fixed-id-for-tests");
        targetSystem.setOrganization(organization);
        return targetSystem;
    }

    @Before
    public void initTest() {
        targetSystemRepository.deleteAll();
        targetSystem = createEntity();
    }

    @Test
    public void createTargetSystem() throws Exception {
        int databaseSizeBeforeCreate = targetSystemRepository.findAll().size();

        // Create the TargetSystem
        TargetSystemDTO targetSystemDTO = targetSystemMapper.toDto(targetSystem);
        restTargetSystemMockMvc.perform(post("/api/target-systems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(targetSystemDTO)))
            .andExpect(status().isCreated());

        // Validate the TargetSystem in the database
        List<TargetSystem> targetSystemList = targetSystemRepository.findAll();
        assertThat(targetSystemList).hasSize(databaseSizeBeforeCreate + 1);
        TargetSystem testTargetSystem = targetSystemList.get(targetSystemList.size() - 1);
        assertThat(testTargetSystem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTargetSystem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTargetSystem.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testTargetSystem.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    public void createTargetSystemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = targetSystemRepository.findAll().size();

        // Create the TargetSystem with an existing ID
        targetSystem.setId("existing_id");
        TargetSystemDTO targetSystemDTO = targetSystemMapper.toDto(targetSystem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTargetSystemMockMvc.perform(post("/api/target-systems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(targetSystemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TargetSystem in the database
        List<TargetSystem> targetSystemList = targetSystemRepository.findAll();
        assertThat(targetSystemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = targetSystemRepository.findAll().size();
        // set the field null
        targetSystem.setName(null);

        // Create the TargetSystem, which fails.
        TargetSystemDTO targetSystemDTO = targetSystemMapper.toDto(targetSystem);

        restTargetSystemMockMvc.perform(post("/api/target-systems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(targetSystemDTO)))
            .andExpect(status().isBadRequest());

        List<TargetSystem> targetSystemList = targetSystemRepository.findAll();
        assertThat(targetSystemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = targetSystemRepository.findAll().size();
        // set the field null
        targetSystem.setCreated(null);

        // Create the TargetSystem, which fails.
        TargetSystemDTO targetSystemDTO = targetSystemMapper.toDto(targetSystem);

        restTargetSystemMockMvc.perform(post("/api/target-systems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(targetSystemDTO)))
            .andExpect(status().isBadRequest());

        List<TargetSystem> targetSystemList = targetSystemRepository.findAll();
        assertThat(targetSystemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = targetSystemRepository.findAll().size();
        // set the field null
        targetSystem.setCreatedBy(null);

        // Create the TargetSystem, which fails.
        TargetSystemDTO targetSystemDTO = targetSystemMapper.toDto(targetSystem);

        restTargetSystemMockMvc.perform(post("/api/target-systems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(targetSystemDTO)))
            .andExpect(status().isBadRequest());

        List<TargetSystem> targetSystemList = targetSystemRepository.findAll();
        assertThat(targetSystemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllTargetSystems() throws Exception {
        // Initialize the database
        targetSystemRepository.save(targetSystem);

        // Get all the targetSystemList
        restTargetSystemMockMvc.perform(get("/api/target-systems?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(targetSystem.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())));
    }
    
    @Test
    public void getTargetSystem() throws Exception {
        // Initialize the database
        targetSystemRepository.save(targetSystem);

        // Get the targetSystem
        restTargetSystemMockMvc.perform(get("/api/target-systems/{id}", targetSystem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(targetSystem.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()));
    }

    @Test
    public void getNonExistingTargetSystem() throws Exception {
        // Get the targetSystem
        restTargetSystemMockMvc.perform(get("/api/target-systems/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateTargetSystem() throws Exception {
        // Initialize the database
        targetSystemRepository.save(targetSystem);

        int databaseSizeBeforeUpdate = targetSystemRepository.findAll().size();

        // Update the targetSystem
        TargetSystem updatedTargetSystem = targetSystemRepository.findById(targetSystem.getId()).get();
        updatedTargetSystem
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .createdBy(UPDATED_CREATED_BY);
        TargetSystemDTO targetSystemDTO = targetSystemMapper.toDto(updatedTargetSystem);

        restTargetSystemMockMvc.perform(put("/api/target-systems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(targetSystemDTO)))
            .andExpect(status().isOk());

        // Validate the TargetSystem in the database
        List<TargetSystem> targetSystemList = targetSystemRepository.findAll();
        assertThat(targetSystemList).hasSize(databaseSizeBeforeUpdate);
        TargetSystem testTargetSystem = targetSystemList.get(targetSystemList.size() - 1);
        assertThat(testTargetSystem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTargetSystem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTargetSystem.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testTargetSystem.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    public void updateNonExistingTargetSystem() throws Exception {
        int databaseSizeBeforeUpdate = targetSystemRepository.findAll().size();

        // Create the TargetSystem
        TargetSystemDTO targetSystemDTO = targetSystemMapper.toDto(targetSystem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTargetSystemMockMvc.perform(put("/api/target-systems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(targetSystemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TargetSystem in the database
        List<TargetSystem> targetSystemList = targetSystemRepository.findAll();
        assertThat(targetSystemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteTargetSystem() throws Exception {
        // Initialize the database
        targetSystemRepository.save(targetSystem);

        int databaseSizeBeforeDelete = targetSystemRepository.findAll().size();

        // Delete the targetSystem
        restTargetSystemMockMvc.perform(delete("/api/target-systems/{id}", targetSystem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TargetSystem> targetSystemList = targetSystemRepository.findAll();
        assertThat(targetSystemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TargetSystem.class);
        TargetSystem targetSystem1 = new TargetSystem();
        targetSystem1.setId("id1");
        TargetSystem targetSystem2 = new TargetSystem();
        targetSystem2.setId(targetSystem1.getId());
        assertThat(targetSystem1).isEqualTo(targetSystem2);
        targetSystem2.setId("id2");
        assertThat(targetSystem1).isNotEqualTo(targetSystem2);
        targetSystem1.setId(null);
        assertThat(targetSystem1).isNotEqualTo(targetSystem2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TargetSystemDTO.class);
        TargetSystemDTO targetSystemDTO1 = new TargetSystemDTO();
        targetSystemDTO1.setId("id1");
        TargetSystemDTO targetSystemDTO2 = new TargetSystemDTO();
        assertThat(targetSystemDTO1).isNotEqualTo(targetSystemDTO2);
        targetSystemDTO2.setId(targetSystemDTO1.getId());
        assertThat(targetSystemDTO1).isEqualTo(targetSystemDTO2);
        targetSystemDTO2.setId("id2");
        assertThat(targetSystemDTO1).isNotEqualTo(targetSystemDTO2);
        targetSystemDTO1.setId(null);
        assertThat(targetSystemDTO1).isNotEqualTo(targetSystemDTO2);
    }
}
