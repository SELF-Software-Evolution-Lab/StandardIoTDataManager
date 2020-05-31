package co.edu.uniandes.xrepo.service.reports;

import co.edu.uniandes.xrepo.domain.BatchTask;
import co.edu.uniandes.xrepo.domain.enumeration.TaskType;
import co.edu.uniandes.xrepo.service.BatchTaskService;
import co.edu.uniandes.xrepo.service.LaboratoryService;
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

@Service
public class HdfsRunMRAlgorithmTaskProcessorService implements BackgroundTaskProcessor, SearchReportFileLocator {

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
    private final String reportLocation;

    public HdfsRunMRAlgorithmTaskProcessorService(BatchTaskService batchTaskService, LaboratoryService samplingService
        , @Value("${xrepo.report-generation-location}") String reportLocation) {
        log.info("Creating HDFS search processor service");
        this.batchTaskService = batchTaskService;
        this.laboratoryService = samplingService;
        this.reportLocation = reportLocation;
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

            List<String> fileHdfsLocations = laboratoryService.findAllSharedFiles(params.getLaboratoryId());

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

            for(String hdfsFile : fileHdfsLocations){
                Process console = Runtime.getRuntime().exec(new String[]{shellScript
                    ,"-i",hdfsFile
                    ,"-m",params.getMapperFileUrl()
                    ,"-r",params.getReducerFileUrl()
                    ,"-o",getFileName(hdfsFile)
                    ,"-d",TIME_FORMATTER_DATE.format(processStart)
                    ,"-t",TIME_FORMATTER_TIME.format(processStart)});
                consoleTracker.add(console);
            }
            //wait for all the console command to finish to write the report
            //better than wait for console, so all HDFS search can be made in parallel
            Boolean running = true;
            while (running){
                if (!consoleTracker.stream().anyMatch(s -> s.isAlive())){
                    running = false;
                }
            }
            writeReport(fileHdfsLocations, TIME_FORMATTER_DATE.format(processStart)
                ,TIME_FORMATTER_TIME.format(processStart), task);
            batchTaskService.save(task.progress(100).state(COMPLETED).endDate(Instant.now()));
        } catch (Exception e) {
            batchTaskService.save(
                task.progress(0).state(ERROR).endDate(Instant.now())
                    .description("Internal server error: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    private void writeReport(List<String> fileLocations, String savedDate, String savedTime, BatchTask task) throws IOException {
        Path path = Paths.get(reportLocation, buildReportName(task));
        log.info("HDFS Algortithm Report to create at {}", path);
        File file = path.toFile();
        file.createNewFile();
        log.info("HDFS Algortithm Report created at {}", path);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            log.info("Start writing Algortithm report");
            for (String hdfsFile : fileLocations){
                writer.write("Processed: " + hdfsFile + ", Output location: "
                    + "/user/hadoop/" + getFileName(hdfsFile) + "/" + savedDate + "/" + savedTime);
            }
            log.info("Report generated successfully {}", file.getName());
        } catch (IOException e) {
            file.delete();
            throw new UncheckedIOException(e);
        }
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

    private String getFileName(String hdfsPath){
        String[] components = hdfsPath.split("/");
        return components[components.length-1];
    }
}
