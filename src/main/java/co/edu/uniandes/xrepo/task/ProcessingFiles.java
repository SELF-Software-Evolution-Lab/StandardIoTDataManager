package co.edu.uniandes.xrepo.task;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.uniandes.xrepo.domain.BatchTask;
import co.edu.uniandes.xrepo.domain.enumeration.TaskState;
import co.edu.uniandes.xrepo.domain.enumeration.TaskType;
import co.edu.uniandes.xrepo.service.BatchTaskService;
import co.edu.uniandes.xrepo.service.SampleService;
import co.edu.uniandes.xrepo.service.dto.SampleDTO;
import co.edu.uniandes.xrepo.service.dto.SamplesFilesParametersDTO;
import co.edu.uniandes.xrepo.service.task.BackgroundTaskProcessor;
import co.edu.uniandes.xrepo.service.util.AsyncDelegator;

@Service
public class ProcessingFiles implements BackgroundTaskProcessor {

    private final Logger log = LoggerFactory.getLogger(ProcessingFiles.class);
    private final SampleService sampleService;
    private final BatchTaskService batchTaskService;
    private final AsyncDelegator asyncDelegator;
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.nnnnnnnnn");
    private int totalLines = 0;
    private int processedLines = 0;
    private int processedLinesOk;


    public ProcessingFiles(BatchTaskService batchTaskService, AsyncDelegator asyncDelegator, SampleService sampleService) {
        this.batchTaskService = batchTaskService;
        this.asyncDelegator = asyncDelegator;
        this.sampleService = sampleService;
    }

    @Override
    public void processTask(BatchTask task) {
        try {
            SamplesFilesParametersDTO parameters = extractSamplesFileParams(task);
            totalLines = countLines(parameters.getFilePath());
            if (totalLines < 0) {
                parameters.setTotalLines(totalLines);
                task.endDate(Instant.now()).state(TaskState.ERROR).objectToParameters(parameters);
                batchTaskService.save(task);
                return;
            }
            parameters.setTotalLines(totalLines);
            task.startDate(Instant.now()).state(TaskState.PROCESSING).objectToParameters(parameters);
            batchTaskService.save(task);
            processFile(parameters, task);

            parameters.setProcessedLines(processedLines);
            parameters.setProcessedLinesOk(processedLinesOk);

            task.endDate(Instant.now()).state(TaskState.COMPLETED).progress(100).objectToParameters(parameters);
            batchTaskService.save(task);

        } catch (Exception e) {
            log.error(e.getMessage());
            task.endDate(Instant.now()).state(TaskState.ERROR);
            batchTaskService.save(task);
        }

    }

    private void processFile(SamplesFilesParametersDTO params, BatchTask task) {
        Path file = Paths.get(params.getFilePath());
        try (Stream<String> lines = Files.lines(file, StandardCharsets.UTF_8)) {
            lines.forEach(s -> processFileLine(s, task));
        } catch (IOException ioe) {
            log.error("Unexpected error handling samples file", ioe);
        }
    }

    private void processFileLine(String line, BatchTask task) {
        SampleDTO sample = parseLineToSample(line);
        if (sample == null) {
            return;
        }

        try {
            sampleService.save(sample);
            processedLinesOk++;
        } catch (Exception e) {
            log.error("Error saving sample {}", e.getMessage());

        }
        if ((++processedLines % 1000) == 0) {
            saveTaskProgress(totalLines, processedLines, task);
        }
    }


    public int countLines(String path) {
        LineNumberReader reader;
        try {
            reader = new LineNumberReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            log.error("Error count lines {}", e.getMessage());
            return (-1);
        }

        try {
            while ((reader.readLine()) != null) ;
            int cnt = reader.getLineNumber();
            reader.close();
            return cnt;
        } catch (IOException e) {
            log.error("Error count lines {}", e.getMessage());
            return (-2);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("Error count lines {}", e.getMessage());
                }
            }
        }
    }

    private SampleDTO parseLineToSample(String line) {

        try {
            SampleDTO sample = new SampleDTO();
            String[] columns = line.split(",");


            Iterator<String> iterator = Arrays.stream(columns).iterator();
            // Sampling Id
            String samplingId = iterator.next();
            sample.setSamplingId(samplingId);

            // DateTime
            String datetime = iterator.next();
            sample.setDateTime(LocalDateTime.parse(datetime, dateTimeformatter));

            // Time stamp
            String timestamp = iterator.next();
            sample.setTs(new SampleDTO.SampleInstant(Long.parseLong(timestamp) / 10000000,
                (Long.parseLong(timestamp) % 10000000) * 100));

            // Sensor Id
            String sensorId = iterator.next();
            sample.setSensorInternalId(sensorId);

            // measurements
            Map<String, BigDecimal> measurements = new HashMap<>();
            sample.setMeasurements(measurements);
            while (iterator.hasNext()) {
                String variable = iterator.next();
                String value = iterator.next();
                measurements.put(variable, new BigDecimal(value));
            }

            return sample;
        } catch (Exception ex) {
            log.error("Error Parse Line to Sample", ex);
            return null;
        }
    }

    @Override
    public TaskType getType() {
        return TaskType.FILE_LOAD;
    }


    private SamplesFilesParametersDTO extractSamplesFileParams(BatchTask task) {
        try {
            return jsonMapper
                .readValue(task.getParameters().toJson(), SamplesFilesParametersDTO.class);
        } catch (IOException e) {
            log.error("Samples file parameters couldn't be processed from task information ", e);
            throw new UncheckedIOException(e);
        }

    }

    private void saveTaskProgress(final int totalLines, final int processedLiness, final BatchTask task) {
        asyncDelegator.async(() -> {
            batchTaskService.save(task.progress((processedLiness * 100) / totalLines));
        });
    }
}
