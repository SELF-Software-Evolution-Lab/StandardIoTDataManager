package co.edu.uniandes.xrepo.service.reports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

@Service
public class SearchReportTaskProcessor implements BackgroundTaskProcessor, SearchReportFileLocator {
    public static final DateTimeFormatter TIME_FORMATTER_FILE = DateTimeFormatter.ofPattern("YYMMDD.HHmmssn")
        .withZone(ZoneId.systemDefault());

    private final Logger log = LoggerFactory.getLogger(SearchReportTaskProcessor.class);
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    private final MongoTemplate mongoTemplate;
    private final BatchTaskService batchTaskService;
    private final String reportLocation;

    public SearchReportTaskProcessor(MongoTemplate mongoTemplate,
                                     BatchTaskService batchTaskService,
                                     @Value("${xrepo.report-generation-location}") String reportLocation) {
        this.mongoTemplate = mongoTemplate;
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
            log.warn("No data found to generate search report {}", params);
            batchTaskService.save(
                task.progress(0).state(ERROR).endDate(Instant.now())
                    .description("No data found to generate search report")
            );
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
        Path path = Paths.get(reportLocation, buildReportName(task));
        log.info("Report to create {}", path);
        File file = path.toFile();
        file.createNewFile();
        log.info("Report created {}", path);
        final long expectedRecords = params.getExpectedRecords();
        final AtomicLong current = new AtomicLong(0);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            log.info("Start reading records");
            while (stream.hasNext()) {
                writer.write(new SampleCsvAdapter(stream.next()).toCsvRecord() + "\n");
                saveTaskProgress(expectedRecords, current.addAndGet(1), task);
            }
            log.info("Report generated successfully {}", file.getName());
        } catch (IOException e) {
            file.delete();
            throw new UncheckedIOException(e);
        }
    }

    private void saveTaskProgress(final long expectedRecords, final long currentRecord, final BatchTask task) {
        try {
            Long progress = Long.valueOf(currentRecord * 100 / expectedRecords);
            if (progress % 4 == 0 || progress % 5 == 0) {
                batchTaskService.save(task.progress(progress.intValue()));
            }
        } catch (Exception e) {
            log.error("Error updating task progress", e);
        }
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

    public File locateReportFile(BatchTask task) {
        Path path = Paths.get(reportLocation, buildReportName(task));
        return path.toFile();
    }

    private String buildReportName(BatchTask task) {
        String id = task.getId();
        String createDate = TIME_FORMATTER_FILE.format(task.getCreateDate());
        String startDate = TIME_FORMATTER_FILE.format(task.getStartDate());
        return id + "_" + createDate + "_" + startDate + ".csv";
    }
}
