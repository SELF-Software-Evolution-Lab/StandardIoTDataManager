package co.edu.uniandes.xrepo.service.reports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.CloseableIterator;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.uniandes.xrepo.domain.BatchTask;
import co.edu.uniandes.xrepo.domain.Sample;
import static co.edu.uniandes.xrepo.domain.enumeration.TaskState.COMPLETED;
import static co.edu.uniandes.xrepo.domain.enumeration.TaskState.ERROR;
import static co.edu.uniandes.xrepo.domain.enumeration.TaskState.PROCESSING;
import co.edu.uniandes.xrepo.domain.enumeration.TaskType;
import co.edu.uniandes.xrepo.service.BatchTaskService;
import co.edu.uniandes.xrepo.service.dto.SampleSearchParametersDTO;
import co.edu.uniandes.xrepo.service.task.BackgroundTaskProcessor;
import co.edu.uniandes.xrepo.service.util.AsyncDelegator;

@Service
public class SearchReportTaskProcessor implements BackgroundTaskProcessor {
    private final Logger log = LoggerFactory.getLogger(SearchReportTaskProcessor.class);
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    private final MongoTemplate mongoTemplate;
    private final AsyncDelegator asyncDelegator;
    private final BatchTaskService batchTaskService;
    private final String reportLocation;

    public SearchReportTaskProcessor(MongoTemplate mongoTemplate,
                                     AsyncDelegator asyncDelegator,
                                     BatchTaskService batchTaskService,
                                     @Value("${xrepo.report-generation-location}") String reportLocation) {
        this.mongoTemplate = mongoTemplate;
        this.asyncDelegator = asyncDelegator;
        this.batchTaskService = batchTaskService;
        this.reportLocation = reportLocation;
    }

    @Override
    public TaskType getType() {
        return TaskType.REPORT;
    }

    @Override
    public void processTask(BatchTask task) {
        log.info("Starting ReportService for task {}", task);

        batchTaskService.save(task.startDate(Instant.now()).state(PROCESSING));

        SampleSearchParametersDTO params = extractSearchParams(task);

        Query query = new Query(params.asCriteria());
        log.info("Processing report for query: {}", query.getQueryObject().toJson());
        CloseableIterator<Sample> stream = mongoTemplate.stream(query, Sample.class);

        if (!stream.hasNext()) {
            log.error("No data found to generate search report {}", params);
            return;
        }

        try {
            writeReport(params, stream, task);
            batchTaskService.save(task.progress(100).state(COMPLETED).endDate(Instant.now()));
        } catch (IOException e) {
            log.error("Unexpected error handling report file", e);
            batchTaskService.save(task.progress(0).description(e.getMessage()).state(ERROR).endDate(Instant.now()));
        }

        stream.close();
    }

    private void writeReport(SampleSearchParametersDTO params,
                             CloseableIterator<Sample> stream, BatchTask task) throws IOException {
        File file = Paths.get(reportLocation, "report.csv").toFile();
        file.createNewFile();
        final long expectedRecords = params.getExpectedRecords();
        final AtomicLong current = new AtomicLong(0);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            while (stream.hasNext()) {
                writer.write(new SampleCsvAdapter(stream.next()).toCsvRecord());
                saveTaskProgress(expectedRecords, current.addAndGet(1), task);
            }
        } catch (IOException e) {
            file.delete();
            throw new UncheckedIOException(e);
        }
    }

    private void saveTaskProgress(final long expectedRecords, final long addAndGet, final BatchTask task) {
        asyncDelegator.async(() -> {
            Long l = Long.valueOf(addAndGet * 100 / expectedRecords);
            batchTaskService.save(task.progress(l.intValue()));
        });
    }

    private SampleSearchParametersDTO extractSearchParams(BatchTask task) {
        try {
            return jsonMapper
                .readValue(task.getParameters().toJson(), SampleSearchParametersDTO.class);
        } catch (IOException e) {
            log.error("Search parameters couldn't be processed from task information ", e);
            throw new UncheckedIOException(e);
        }

    }
}
