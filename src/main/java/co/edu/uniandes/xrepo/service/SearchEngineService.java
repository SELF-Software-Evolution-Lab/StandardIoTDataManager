package co.edu.uniandes.xrepo.service;

import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import co.edu.uniandes.xrepo.service.dto.AlgorithmDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;

import co.edu.uniandes.xrepo.domain.BatchTask;
import co.edu.uniandes.xrepo.domain.Sample;
import co.edu.uniandes.xrepo.domain.Sampling;
import co.edu.uniandes.xrepo.domain.enumeration.TaskState;
import co.edu.uniandes.xrepo.domain.enumeration.TaskType;
import co.edu.uniandes.xrepo.domain.metadata.OperativeCondition;
import co.edu.uniandes.xrepo.domain.metadata.OperativeRange;
import co.edu.uniandes.xrepo.repository.ExperimentRepository;
import co.edu.uniandes.xrepo.repository.SamplingRepository;
import co.edu.uniandes.xrepo.security.SecurityUtils;
import co.edu.uniandes.xrepo.service.dto.SampleSearchParametersDTO;
import co.edu.uniandes.xrepo.service.reports.SearchReportTaskProcessor;
import co.edu.uniandes.xrepo.service.util.AsyncDelegator;
import lombok.Getter;
import lombok.Setter;

@Service
public class SearchEngineService {

    private final Logger log = LoggerFactory.getLogger(SearchEngineService.class);

    private final SamplingRepository samplingRepository;

    private final SamplingService samplingService;

    private final ExperimentRepository experimentRepository;

    private final MongoTemplate mongoTemplate;

    private final BatchTaskService batchTaskService;

    private final SearchReportTaskProcessor reportTaskProcessor;

    private final AsyncDelegator asyncDelegator;
    private final int maxRecords;

    public SearchEngineService(SamplingRepository samplingRepository, SamplingService samplingService, ExperimentRepository experimentRepository,
                               MongoTemplate mongoTemplate, BatchTaskService batchTaskService,
                               SearchReportTaskProcessor reportTaskProcessor,
                               AsyncDelegator asyncDelegator,
                               @Value("${xrepo.report-generation-max-records-online}") int maxRecords) {
        this.samplingRepository = samplingRepository;
        this.samplingService = samplingService;
        this.experimentRepository = experimentRepository;
        this.mongoTemplate = mongoTemplate;
        this.batchTaskService = batchTaskService;
        this.reportTaskProcessor = reportTaskProcessor;
        this.asyncDelegator = asyncDelegator;
        this.maxRecords = maxRecords;
    }

    public SearchResponse hdfsFind(SampleSearchParametersDTO searchParametersDTO) throws IOException {
        List<String> fileHdfsLocations = samplingService.getAllFilesFromTargetSystem(searchParametersDTO.getTargetSystemId().get(0));
        LocalDateTime start = searchParametersDTO.getFromDateTime();
        LocalDateTime end = searchParametersDTO.getToDateTime();

        String shellScript = new ClassPathResource("mapreduce-files/ShellSample.sh").getURI().getPath();
        log.info("Start Date Requested", start.toString());
        log.info("End Date Requested", end.toString());
        Process console = Runtime.getRuntime().exec(new String[]{shellScript
                                                    ,"-i",fileHdfsLocations.get(0)
                                                    ,"-s",start.toString()
                                                    ,"-e",end.toString()});
        return new SearchResponse(samplingService.getAllFilesFromTargetSystem(searchParametersDTO.getTargetSystemId().get(0)).size());
    }

    public SearchResponse hdfsFindTask(SampleSearchParametersDTO searchParametersDTO) throws IOException {
        SearchResponse response = new SearchResponse();
        response.setBatchTaskId(deferGeneration(searchParametersDTO,TaskType.HDFS_REPORT));
        return response;
    }

    public SearchResponse hdfsRunMapReduceTask(AlgorithmDTO algorithmDTO) throws IOException {
        SearchResponse response = new SearchResponse();
        response.setBatchTaskId(deferGeneration(algorithmDTO,TaskType.HDFS_MR_ALGORITHM));
        return response;
    }

    public SearchResponse preSearchSamples(SampleSearchParametersDTO params) {

        boolean foundExperiments = foundCandidateExperiments(params);
        boolean foundSamplings = foundCandidateSamplings(params);

        if (params.requireSearchWithTags() && !foundExperiments && !foundSamplings) {
            return new SearchResponse(0);
        }

        if (params.requireSearchWithConditions() && !foundSamplings) {
            return new SearchResponse(0);
        }

        Query query = new Query(params.asCriteria());
        log.info("Requesting count for query: {}", query.getQueryObject().toJson());
        SearchResponse response = new SearchResponse();
        long count = mongoTemplate.count(query, Sample.class);
        response.setCount(count);
        params.setExpectedRecords(count);
        if (count > maxRecords) {
            response.setBatchTaskId(deferGeneration(params, TaskType.REPORT));
        } else {
            response.setBatchTaskId(generateOnline(params, TaskType.REPORT));
        }
        log.info("Count returned {}", count);
        return response;
    }

    private boolean foundCandidateSamplings(SampleSearchParametersDTO params) {
        List<String> tags = params.getTags() == null ? Collections.emptyList() : params.getTags();

        List<OperativeRange> operativeConditions =
            params.getOperativeConditions() == null ? Collections.emptyList() : params.getOperativeConditions();

        List<String> conditionsNames = operativeConditions.stream().map(range -> range.getVarName()).collect(Collectors.toList());

        if (!tags.isEmpty() || !conditionsNames.isEmpty()) {
            List<Sampling> byTagsAndConditions = samplingRepository.findByTagsAndConditions(tags, conditionsNames);

            List<String> samplingsId = byTagsAndConditions.stream()
                .filter(sampling -> conditionsInRange(sampling, operativeConditions))
                .map(sampling -> sampling.getId())
                .collect(Collectors.toList());

            params.getSamplingsId().addAll(samplingsId);
            if (samplingsId.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private boolean conditionsInRange(Sampling sampling, List<OperativeRange> operativeConditions) {

        for (OperativeRange range : operativeConditions) {

            Optional<OperativeCondition> optCondition = sampling.getConditions().stream()
                .filter(cond -> range.getVarName().equals(cond.getVarName()))
                .findFirst();
            if (optCondition.isPresent()) {
                OperativeCondition condition = optCondition.get();
                int btom = range.getMinVal().compareTo(condition.getValue());
                int top = range.getMaxVal().compareTo(condition.getValue());

                if (btom == 1 || top == -1) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean foundCandidateExperiments(SampleSearchParametersDTO params) {
        List<String> tags = params.getTags();
        if (!tags.isEmpty()) {
            List<String> experimentsId = experimentRepository.findWithTags(tags).stream()
                .map(experiment -> experiment.getId())
                .collect(Collectors.toList());

            params.getExperimentsId().addAll(experimentsId);
            if (experimentsId.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private String generateOnline(SampleSearchParametersDTO params, TaskType tskType) {
        BatchTask onlineTask = BatchTask.builder()
            .createDate(Instant.now())
            .progress(0)
            .user(SecurityUtils.getCurrentUserLogin().get())
            .type(tskType)
            .state(TaskState.SUBMITTED)
            .build();
        onlineTask.objectToParameters(params);
        BatchTask saved = batchTaskService.save(onlineTask);
        asyncDelegator.async(() -> reportTaskProcessor.processTask(onlineTask));
        return saved.getId();
    }

    private String deferGeneration(Object params, TaskType tskType) {
        BatchTask deferredTask = BatchTask.builder()
            .createDate(Instant.now())
            .progress(0)
            .user(SecurityUtils.getCurrentUserLogin().get())
            .type(tskType)
            .state(TaskState.PENDING)
            .build();
        deferredTask.objectToParameters(params);
        BatchTask saved = batchTaskService.save(deferredTask);
        return saved.getId();
    }

    @Setter
    @Getter
    public class SearchResponse implements Serializable {

        public SearchResponse() {
        }

        public SearchResponse(long count) {
            this.count = count;
        }

        private String batchTaskId;
        private long count;
    }
}
