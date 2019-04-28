package co.edu.uniandes.xrepo.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import co.edu.uniandes.xrepo.domain.BatchTask;
import co.edu.uniandes.xrepo.domain.Sample;
import co.edu.uniandes.xrepo.domain.enumeration.TaskState;
import co.edu.uniandes.xrepo.domain.enumeration.TaskType;
import co.edu.uniandes.xrepo.repository.ExperimentRepository;
import co.edu.uniandes.xrepo.repository.SamplingRepository;
import co.edu.uniandes.xrepo.security.SecurityUtils;
import co.edu.uniandes.xrepo.service.dto.SampleSearchParametersDTO;
import co.edu.uniandes.xrepo.service.reports.SearchReportTaskProcessor;
import co.edu.uniandes.xrepo.service.util.AsyncDelegator;

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

    public long preSearchSamples(SampleSearchParametersDTO params) {

        List<String> tags = params.getTags();
        if (!tags.isEmpty()) {
            List<String> samplingsId = samplingRepository.findWithTags(tags).stream()
                .map(sampling -> sampling.getId())
                .collect(Collectors.toList());

            List<String> experimentsId = experimentRepository.findWithTags(tags).stream()
                .map(experiment -> experiment.getId())
                .collect(Collectors.toList());

            params.getSamplingsId().addAll(samplingsId);
            params.getExperimentsId().addAll(experimentsId);
        }

        Query query = new Query(params.asCriteria());
        log.info("Requesting count for query: {}", query.getQueryObject().toJson());
        long count = mongoTemplate.count(query, Sample.class);
        params.setExpectedRecords(count);
        if (count > maxRecords) {
            deferReport(params);
        } else {
            generateOnlineReport(params);
        }
        log.info("Count returned {}", count);
        return count;
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
}
