package co.edu.uniandes.xrepo.web.rest;

import co.edu.uniandes.xrepo.XrepoApp;

import co.edu.uniandes.xrepo.domain.BatchTask;
import co.edu.uniandes.xrepo.repository.BatchTaskRepository;
import co.edu.uniandes.xrepo.service.BatchTaskService;
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
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;


import static co.edu.uniandes.xrepo.web.rest.TestUtil.sameInstant;
import static co.edu.uniandes.xrepo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.uniandes.xrepo.domain.enumeration.TypeTask;
import co.edu.uniandes.xrepo.domain.enumeration.StateTask;
/**
 * Test class for the BatchTaskResource REST controller.
 *
 * @see BatchTaskResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = XrepoApp.class)
public class BatchTaskResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final TypeTask DEFAULT_TYPE = TypeTask.UNDEFINED;
    private static final TypeTask UPDATED_TYPE = TypeTask.REPORT;

    private static final StateTask DEFAULT_STATE = StateTask.PENDING;
    private static final StateTask UPDATED_STATE = StateTask.PROCESSING;

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_PROGRESS = 1;
    private static final Integer UPDATED_PROGRESS = 2;

    @Autowired
    private BatchTaskRepository batchTaskRepository;

    @Autowired
    private BatchTaskService batchTaskService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restBatchTaskMockMvc;

    private BatchTask batchTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BatchTaskResource batchTaskResource = new BatchTaskResource(batchTaskService);
        this.restBatchTaskMockMvc = MockMvcBuilders.standaloneSetup(batchTaskResource)
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
    public static BatchTask createEntity() {
        BatchTask batchTask = new BatchTask()
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .state(DEFAULT_STATE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .progress(DEFAULT_PROGRESS);
        return batchTask;
    }

    @Before
    public void initTest() {
        batchTaskRepository.deleteAll();
        batchTask = createEntity();
    }

    @Test
    public void createBatchTask() throws Exception {
        int databaseSizeBeforeCreate = batchTaskRepository.findAll().size();

        // Create the BatchTask
        restBatchTaskMockMvc.perform(post("/api/batch-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(batchTask)))
            .andExpect(status().isCreated());

        // Validate the BatchTask in the database
        List<BatchTask> batchTaskList = batchTaskRepository.findAll();
        assertThat(batchTaskList).hasSize(databaseSizeBeforeCreate + 1);
        BatchTask testBatchTask = batchTaskList.get(batchTaskList.size() - 1);
        assertThat(testBatchTask.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBatchTask.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testBatchTask.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testBatchTask.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testBatchTask.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testBatchTask.getProgress()).isEqualTo(DEFAULT_PROGRESS);
    }

    @Test
    public void createBatchTaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = batchTaskRepository.findAll().size();

        // Create the BatchTask with an existing ID
        batchTask.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restBatchTaskMockMvc.perform(post("/api/batch-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(batchTask)))
            .andExpect(status().isBadRequest());

        // Validate the BatchTask in the database
        List<BatchTask> batchTaskList = batchTaskRepository.findAll();
        assertThat(batchTaskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllBatchTasks() throws Exception {
        // Initialize the database
        batchTaskRepository.save(batchTask);

        // Get all the batchTaskList
        restBatchTaskMockMvc.perform(get("/api/batch-tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(batchTask.getId())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].progress").value(hasItem(DEFAULT_PROGRESS)));
    }
    
    @Test
    public void getBatchTask() throws Exception {
        // Initialize the database
        batchTaskRepository.save(batchTask);

        // Get the batchTask
        restBatchTaskMockMvc.perform(get("/api/batch-tasks/{id}", batchTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(batchTask.getId()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)))
            .andExpect(jsonPath("$.progress").value(DEFAULT_PROGRESS));
    }

    @Test
    public void getNonExistingBatchTask() throws Exception {
        // Get the batchTask
        restBatchTaskMockMvc.perform(get("/api/batch-tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateBatchTask() throws Exception {
        // Initialize the database
        batchTaskService.save(batchTask);

        int databaseSizeBeforeUpdate = batchTaskRepository.findAll().size();

        // Update the batchTask
        BatchTask updatedBatchTask = batchTaskRepository.findById(batchTask.getId()).get();
        updatedBatchTask
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .state(UPDATED_STATE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .progress(UPDATED_PROGRESS);

        restBatchTaskMockMvc.perform(put("/api/batch-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBatchTask)))
            .andExpect(status().isOk());

        // Validate the BatchTask in the database
        List<BatchTask> batchTaskList = batchTaskRepository.findAll();
        assertThat(batchTaskList).hasSize(databaseSizeBeforeUpdate);
        BatchTask testBatchTask = batchTaskList.get(batchTaskList.size() - 1);
        assertThat(testBatchTask.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBatchTask.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testBatchTask.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testBatchTask.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testBatchTask.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testBatchTask.getProgress()).isEqualTo(UPDATED_PROGRESS);
    }

    @Test
    public void updateNonExistingBatchTask() throws Exception {
        int databaseSizeBeforeUpdate = batchTaskRepository.findAll().size();

        // Create the BatchTask

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBatchTaskMockMvc.perform(put("/api/batch-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(batchTask)))
            .andExpect(status().isBadRequest());

        // Validate the BatchTask in the database
        List<BatchTask> batchTaskList = batchTaskRepository.findAll();
        assertThat(batchTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteBatchTask() throws Exception {
        // Initialize the database
        batchTaskService.save(batchTask);

        int databaseSizeBeforeDelete = batchTaskRepository.findAll().size();

        // Delete the batchTask
        restBatchTaskMockMvc.perform(delete("/api/batch-tasks/{id}", batchTask.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BatchTask> batchTaskList = batchTaskRepository.findAll();
        assertThat(batchTaskList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BatchTask.class);
        BatchTask batchTask1 = new BatchTask();
        batchTask1.setId("id1");
        BatchTask batchTask2 = new BatchTask();
        batchTask2.setId(batchTask1.getId());
        assertThat(batchTask1).isEqualTo(batchTask2);
        batchTask2.setId("id2");
        assertThat(batchTask1).isNotEqualTo(batchTask2);
        batchTask1.setId(null);
        assertThat(batchTask1).isNotEqualTo(batchTask2);
    }
}
