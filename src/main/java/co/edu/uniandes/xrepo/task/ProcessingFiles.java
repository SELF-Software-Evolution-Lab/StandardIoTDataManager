package co.edu.uniandes.xrepo.task;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import co.edu.uniandes.xrepo.domain.BatchTask;
import co.edu.uniandes.xrepo.domain.Sample;
import co.edu.uniandes.xrepo.domain.SamplesFiles;
import co.edu.uniandes.xrepo.domain.enumeration.StateTask;
import co.edu.uniandes.xrepo.domain.enumeration.TypeTask;
import co.edu.uniandes.xrepo.repository.SampleRepository;
import co.edu.uniandes.xrepo.repository.SamplesFilesRepository;
import co.edu.uniandes.xrepo.service.BatchTaskService;
import co.edu.uniandes.xrepo.service.task.BackgroundTaskProcessor;

//@Component
@Service
public class ProcessingFiles implements BackgroundTaskProcessor {

    private final Logger log = LoggerFactory.getLogger(ProcessingFiles.class);
    private final SamplesFilesRepository samplesFilesRepository;
    private final SampleRepository sampleRepository;
    private final BatchTaskService batchTaskService;
    public ProcessingFiles(SamplesFilesRepository samplesFilesRepository, SampleRepository sampleRepository, BatchTaskService batchTaskService) {
        this.samplesFilesRepository = samplesFilesRepository;
        this.sampleRepository = sampleRepository;
        this.batchTaskService = batchTaskService;
    }

    // @Scheduled(fixedRate = 60000)
    public void scheduleTaskWithFixedRate() {

        List<SamplesFiles> samplesFiles = samplesFilesRepository.findByState(1);

        if ((samplesFiles != null) && !samplesFiles.isEmpty()) {
            SamplesFiles sampleFile = samplesFiles.get(0);
            sampleFile.setUpdateDateTime(LocalDate.now());
            sampleFile.setState(2);
            samplesFilesRepository.save(sampleFile);

            proccesFile(sampleFile.getPath());

            sampleFile.setUpdateDateTime(LocalDate.now());
            sampleFile.setState(3);
            samplesFilesRepository.save(sampleFile);
        }
        log.debug("Processing Files :: Execution Time - {} {}", LocalDateTime.now(), samplesFiles);
    }

    private void proccesFile(String path) {

        Path file = Paths.get(path);
        Stream<String> lines = null;
        try {
            lines = Files.lines(file, StandardCharsets.UTF_8);

            for (String line : (Iterable<String>) lines::iterator) {
                Sample sample = parseLineToSample(line);
                if (sample != null)
                    sampleRepository.save(sample);
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (lines != null)
                lines.close();
        }

    }

    private Sample parseLineToSample(String line) {

        Sample sample = null;
        String[] columns = line.split(",");

        try {
            if (columns.length > 4) {
                sample = new Sample();
                // Sampling Id
                sample.setSamplingId(columns[0]);
                // Time stamp
                sample.setTs(Instant.ofEpochSecond(Long.parseLong(columns[2]) / 10000000,
                        (Long.parseLong(columns[2]) % 10000000) * 100));
                // Sensor Id
                sample.setSensorInternalId(columns[3]);
                // measurements
                Map<String, BigDecimal> measurements = new HashMap<String, BigDecimal>();
                int totalmeasurements = (columns.length - 4) / 2;
                int posColumn = 4;
                for (int i = 0; i < totalmeasurements; i++, posColumn += 2) {
                    measurements.put(columns[posColumn], BigDecimal.valueOf(Double.valueOf(columns[posColumn + 1])));
                }

                sample.setMeasurements(measurements);
            }
        } catch (Exception ex) {
            log.debug("Error Parse Line to Sample {}", ex.getMessage());
        }

        return (sample);
    }

    @Override
    public TypeTask getType() {
        return TypeTask.FILE_LOAD;
    }

    @Override
    public void processTask(BatchTask ptpTask) {
        ptpTask.setStartDate(ZonedDateTime.now());
        ptpTask.setState(StateTask.PROCESSING);
        batchTaskService.save(ptpTask);
        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ptpTask.setEndDate(ZonedDateTime.now());
        ptpTask.setState(StateTask.COMPLETED);
        batchTaskService.save(ptpTask);
    }
}
