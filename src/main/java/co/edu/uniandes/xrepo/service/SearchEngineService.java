package co.edu.uniandes.xrepo.service;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

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

    private final ExperimentRepository experimentRepository;

    private final MongoTemplate mongoTemplate;

    private final BatchTaskService batchTaskService;

    private final SearchReportTaskProcessor reportTaskProcessor;

    private final AsyncDelegator asyncDelegator;
    private final int maxRecords;

    public SearchEngineService(SamplingRepository samplingRepository, ExperimentRepository experimentRepository,
                               MongoTemplate mongoTemplate, BatchTaskService batchTaskService,
                               SearchReportTaskProcessor reportTaskProcessor,
                               AsyncDelegator asyncDelegator,
                               @Value("${xrepo.report-generation-max-records-online}") int maxRecords) {
        this.samplingRepository = samplingRepository;
        this.experimentRepository = experimentRepository;
        this.mongoTemplate = mongoTemplate;
        this.batchTaskService = batchTaskService;
        this.reportTaskProcessor = reportTaskProcessor;
        this.asyncDelegator = asyncDelegator;
        this.maxRecords = maxRecords;
    }

    public SearchResponse preSearchSamples(SampleSearchParametersDTO params) {

        addCandidateExperiments(params);
        addCandidateSamplings(params);

        Query query = new Query(params.asCriteria());
        log.info("Requesting count for query: {}", query.getQueryObject().toJson());
        SearchResponse response = new SearchResponse();
        long count = mongoTemplate.count(query, Sample.class);
        response.setCount(count);
        params.setExpectedRecords(count);
        if (count > maxRecords) {
            response.setBatchTaskId(deferReport(params));
        } else {
            response.setBatchTaskId(generateOnlineReport(params));
        }
        log.info("Count returned {}", count);
        return response;
    }

    private void addCandidateSamplings(SampleSearchParametersDTO params) {
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
        }
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

    private void addCandidateExperiments(SampleSearchParametersDTO params) {
        List<String> tags = params.getTags();
        if (!tags.isEmpty()) {
            List<String> experimentsId = experimentRepository.findWithTags(tags).stream()
                .map(experiment -> experiment.getId())
                .collect(Collectors.toList());

            params.getExperimentsId().addAll(experimentsId);
        }
    }

    private String generateOnlineReport(SampleSearchParametersDTO params) {
        BatchTask onlineTask = BatchTask.builder()
            .createDate(Instant.now())
            .progress(0)
            .user(SecurityUtils.getCurrentUserLogin().get())
            .type(TaskType.REPORT)
            .state(TaskState.SUBMITTED)
            .build();
        onlineTask.objectToParameters(params);
        BatchTask saved = batchTaskService.save(onlineTask);
        asyncDelegator.async(() -> reportTaskProcessor.processTask(onlineTask));
        return saved.getId();
    }

    private String deferReport(SampleSearchParametersDTO params) {
        BatchTask deferredTask = BatchTask.builder()
            .createDate(Instant.now())
            .progress(0)
            .user(SecurityUtils.getCurrentUserLogin().get())
            .type(TaskType.REPORT)
            .state(TaskState.PENDING)
            .build();
        deferredTask.objectToParameters(params);
        BatchTask saved = batchTaskService.save(deferredTask);
        return saved.getId();
    }

    @Setter
    @Getter
    public class SearchResponse implements Serializable {
        private String batchTaskId;
        private long count;
    }
}
