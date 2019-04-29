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
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uniandes.xrepo.domain.BatchTask;
import co.edu.uniandes.xrepo.domain.Sample;
import co.edu.uniandes.xrepo.domain.enumeration.TaskState;
import co.edu.uniandes.xrepo.domain.enumeration.TaskType;
import co.edu.uniandes.xrepo.repository.SampleRepository;
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

    private void proccesFile(SamplesFilesParametersDTO params, BatchTask task) {

        Path file = Paths.get(params.getFilePath());
        Stream<String> lines = null;
        try {
            lines = Files.lines(file, StandardCharsets.UTF_8);

            for (String line : (Iterable<String>) lines::iterator) {
                SampleDTO sample = parseLineToSample(line);
                if (sample != null) {
                    try {
                        sampleService.save(sample);
                        processedLinesOk++;    
                    } catch (Exception e) {
                        log.error("Error saving sample {}", e.getMessage());
                    }
                }
                if (( ++processedLines % 1000 ) == 0) {
                    saveTaskProgress(totalLines, processedLines, task);
                }
            }

        } catch (IOException ioe) {
            log.error(ioe.getMessage());
        } finally {
            if (lines != null)
                lines.close();
        }

    }

    public int countLines(String path) {
        LineNumberReader reader;
        try {
            reader = new LineNumberReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            log.error("Error count lines {}", e.getMessage());
            return(-1);
        }

        try {
            while ((reader.readLine()) != null);
            int cnt = reader.getLineNumber(); 
            reader.close();
            return cnt;
        } catch (IOException e) {
            log.error("Error count lines {}", e.getMessage());
            return(-2);
        }
        finally
        {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("Error count lines {}", e.getMessage());
                }
            }
        }
    }
    
    private SampleDTO parseLineToSample(String line) {

        SampleDTO sample = null;
        String[] columns = line.split(",");

        try {
            if (columns.length > 4) {
                sample = new SampleDTO();
                // Sampling Id
                sample.setSamplingId(columns[0]);
                
                // DateTime
                sample.setDateTime(LocalDateTime.parse(columns[1], dateTimeformatter));

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
    public TaskType getType() {
        return TaskType.FILE_LOAD;
    }

    @Override
    public void processTask(BatchTask task) {
        try {
            SamplesFilesParametersDTO parameters = extractSamplesFileParams(task);
            totalLines = countLines(parameters.getFilePath());
            if(totalLines < 0)
            {
                parameters.setTotalLines(totalLines);
                task.endDate(Instant.now()).state(TaskState.ERROR).objectToParameters(parameters);
                batchTaskService.save(task);  
                return;
            }
            parameters.setTotalLines(totalLines);
            task.startDate(Instant.now()).state(TaskState.PROCESSING).objectToParameters(parameters);
            batchTaskService.save(task);
            proccesFile(parameters, task);
            
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
