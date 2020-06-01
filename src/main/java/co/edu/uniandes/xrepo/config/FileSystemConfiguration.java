package co.edu.uniandes.xrepo.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class FileSystemConfiguration {

    @Value("${xrepo.report-generation-location}")
    private String reportLocation;

    @Value("${xrepo.samples-files-location}")
    private String samplesFilesLocation;

    @Value("${xrepo.remove-prefix-hdfs}")
    private String removePrefixHdfs;

    @Bean
    @Profile("prod")
    public CommandLineRunner checkFileSystemProd(ApplicationContext ctx) {
        return args -> {
            log.info("Configured locations for files \n{} \n{}", reportLocation, samplesFilesLocation);
            Path reports = Paths.get(reportLocation);
            Path samples = Paths.get(samplesFilesLocation);
            log.info("Checking file mount locations \n{} \n{}", reports, samples);
            if (!reports.toFile().exists()) {
                throw new IllegalStateException(reports + " doesn't exist, file mount is not configured");
            }
            if (!samples.toFile().exists()) {
                throw new IllegalStateException(samples + " doesn't exist, file mount is not configured");
            }
            log.info("Mount locations verified");
        };
    }

    @Bean
    @Profile("dev")
    public CommandLineRunner checkFileSystemDev(ApplicationContext ctx) {
        return args -> {
            Path reports = Paths.get(reportLocation);
            Path samples = Paths.get(samplesFilesLocation);
            log.info("DEV - Checking file mount locations \n{} \n{}", reports, samples);
            Files.createDirectories(reports);
            Files.createDirectories(samples);
            log.info("DEV - Folders created \n{} \n{}", reports, samples);
        };
    }
}
