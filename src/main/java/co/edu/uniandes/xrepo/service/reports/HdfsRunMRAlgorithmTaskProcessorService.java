package co.edu.uniandes.xrepo.service.reports;

import co.edu.uniandes.xrepo.domain.BatchTask;
import co.edu.uniandes.xrepo.domain.enumeration.TaskType;
import co.edu.uniandes.xrepo.service.BatchTaskService;
import co.edu.uniandes.xrepo.service.LaboratoryService;
import co.edu.uniandes.xrepo.service.SubSetService;
import co.edu.uniandes.xrepo.service.dto.SubSetDTO;
import co.edu.uniandes.xrepo.service.dto.AlgorithmDTO;
import co.edu.uniandes.xrepo.service.task.BackgroundTaskProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static co.edu.uniandes.xrepo.domain.enumeration.TaskState.*;
import static java.lang.Thread.sleep;

@Service
public class HdfsRunMRAlgorithmTaskProcessorService implements BackgroundTaskProcessor {

    private final Logger log = LoggerFactory.getLogger(HdfsSearchReportTaskProcessorService.class);
    private static final ObjectMapper jsonMapper = new ObjectMapper();
    public static final DateTimeFormatter TIME_FORMATTER_FILE = DateTimeFormatter.ofPattern("yyMMdd.HHmmssn")
        .withZone(ZoneId.systemDefault());
    public static final DateTimeFormatter TIME_FORMATTER_DATE = DateTimeFormatter.ofPattern("yy-MM-dd")
        .withZone(ZoneId.systemDefault());
    public static final DateTimeFormatter TIME_FORMATTER_TIME = DateTimeFormatter.ofPattern("HH-mm-ss-n")
        .withZone(ZoneId.systemDefault());

    private final BatchTaskService batchTaskService;

    private final LaboratoryService laboratoryService;
    private final SubSetService subSetService;
    private final String resultsLocation;

    public HdfsRunMRAlgorithmTaskProcessorService(BatchTaskService batchTaskService, LaboratoryService samplingService
        , SubSetService subSetService
        , @Value("${xrepo.mr-results-hdfs-location}") String resultsLocation) {
        this.subSetService = subSetService;
        this.resultsLocation = resultsLocation;
        log.info("Creating HDFS search processor service");
        this.batchTaskService = batchTaskService;
        this.laboratoryService = samplingService;
    }

    @Override
    public TaskType getType() {
        return TaskType.HDFS_MR_ALGORITHM;
    }

    @Override
    public void processTask(BatchTask task) {
        log.info("Starting HDFS MR Algorithm for task {}", task);

        try {
            Instant processStart = Instant.now();
            batchTaskService.save(task.startDate(processStart).state(PROCESSING));
            AlgorithmDTO params = extractSearchParams(task);

            List<String> fileHdfsLocations = laboratoryService.findAllLaboratorySourceFilesUrl(params.getLaboratoryId());

            if (fileHdfsLocations.isEmpty()){
                log.warn("No data found to Run Map Reduce {}", params);
                batchTaskService.save(
                    task.progress(0).state(ERROR).endDate(processStart)
                        .description("No data found to generate search report")
                );
                return;
            }

            String shellScript = new ClassPathResource("mapreduce-files/RunRemoteMRAlgorithm.sh").getURI().getPath();

            log.info("Processing HDFS algorithm with id {}", params.getId());

            List<Process> consoleTracker = new ArrayList<>();
            List<String> resultLocations = new ArrayList<>();

            for(String hdfsFile : fileHdfsLocations){
                Process console = Runtime.getRuntime().exec(new String[]{shellScript
                    ,"-i",hdfsFile
                    ,"-m",params.getMapperFileUrl()
                    ,"-r",params.getReducerFileUrl()
                    ,"-o",getOutputPath(hdfsFile, params.getLaboratoryId())
                    ,"-d",TIME_FORMATTER_DATE.format(processStart)
                    ,"-t",TIME_FORMATTER_TIME.format(processStart)});

                consoleTracker.add(console);
                resultLocations.add(getOutputPath(hdfsFile, params.getLaboratoryId(), processStart));
            }
            monitorAndSaveProgress(consoleTracker,task,batchTaskService);
            createSubSet(params, resultLocations);
            batchTaskService.save(task.progress(100).state(COMPLETED).endDate(Instant.now()));
        } catch (Exception e) {
            batchTaskService.save(
                task.progress(0).state(ERROR).endDate(Instant.now())
                    .description("Internal server error: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    private void createSubSet(AlgorithmDTO sourceAlgorithm,List<String> hdfsLocations){
        SubSetDTO subsetDTO = new SubSetDTO();
        subsetDTO.setName("Results-"+sourceAlgorithm.getName());
        subsetDTO.setDescription("Results of runnung " + sourceAlgorithm.getName() + " on the sampling data");
        subsetDTO.setFileHdfsLocation(hdfsLocations);
        subsetDTO.setDateCreated(Instant.now());
        subsetDTO.setDownloadUrl(hdfsLocations);
        subsetDTO.setSetType(sourceAlgorithm.getSetType());
        subsetDTO.setLaboratoryId(sourceAlgorithm.getLaboratoryId());
        subsetDTO.setLaboratoryName(sourceAlgorithm.getLaboratoryName());
        //save the succesfull run timestamp
        sourceAlgorithm.setLastSuccessfulRun(Instant.now());

        //save the subset
        subSetService.saveOnAlgorithm(subsetDTO, sourceAlgorithm);
    }

    private AlgorithmDTO extractSearchParams(BatchTask task) {
        try {
            jsonMapper.registerModule(new JavaTimeModule());
            return jsonMapper
                .readValue(task.getParameters().toJson(), AlgorithmDTO.class);
        } catch (IOException e) {
            log.error("Search parameters couldn't be processed from task information ", e);
            throw new UncheckedIOException(e);
        }

    }

    static void monitorAndSaveProgress(List<Process> consoleProcesses, BatchTask task, BatchTaskService batchTaskService) throws InterruptedException {
        long completedMapReduce = consoleProcesses.size();
        while (completedMapReduce != 0){
            sleep(1 * 1000);
            if (consoleProcesses.stream().filter(s -> s.isAlive()).count() != completedMapReduce){
                completedMapReduce = consoleProcesses.stream().filter(s -> s.isAlive()).count();
                batchTaskService.save(task.progress((int)((consoleProcesses.size() - completedMapReduce)*100/consoleProcesses.size())));
            }
        }
    }

    private String getFileName(String hdfsPath){
        String[] components = hdfsPath.split("/");
        return components[components.length-1];
    }

    private String getOutputPath(String inputPath, String labId){
        return resultsLocation + "/" + getFileName(inputPath) + "-" + labId;
    }

    private String getOutputPath(String inputPath, String labId, Instant processInstant){
        return getOutputPath(inputPath, labId)
            + "/" + TIME_FORMATTER_DATE.format(processInstant)
            + "/" + TIME_FORMATTER_TIME.format(processInstant);
    }
}
