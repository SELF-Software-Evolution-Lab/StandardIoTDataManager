package co.edu.uniandes.xrepo.service.task;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import co.edu.uniandes.xrepo.domain.BatchTask;
import co.edu.uniandes.xrepo.domain.enumeration.TaskType;

@Service
public class BackgroundTaskExecutor {

    private final Logger log = LoggerFactory.getLogger(BackgroundTaskExecutor.class);

    private final ExecutorService executorService;
    private final List<BackgroundTaskProcessor> taskProcessors;
    private final BackgroundTaskProvider provider;
    private final BackgroundTaskProcessor processorNotFound;

    public BackgroundTaskExecutor(List<BackgroundTaskProcessor> taskProcessors,
                                  BackgroundTaskProvider provider,
                                  @Value("${xrepo.background-task-pool-size}") int parallelExecutionTasks) {

        log.info("Creating BackgroundTaskExecutor with {} threads using the following processors {}", parallelExecutionTasks, taskProcessors);
        this.executorService = Executors.newFixedThreadPool(parallelExecutionTasks);
        this.taskProcessors = taskProcessors;
        this.provider = provider;
        processorNotFound = this.taskProcessors.stream()
            .filter(processor -> processor.getType().equals(TaskType.UNDEFINED))
            .findFirst().get();
    }

    @Scheduled(fixedDelay = 30000)
    public void processPendingTasks() {
        log.info("Start looking for pending tasks");
        provider.listPendingTasks().stream()
            .map(this::sendToPool)
            .forEach(provider::markTaskAsSubmitted);
    }

    private BatchTask sendToPool(final BatchTask pendingToBeProcessed) {
        log.info("Sending task to pool executor: {}", pendingToBeProcessed);
        executorService.submit(
            () -> selectProcessor(pendingToBeProcessed)
                .processTask(pendingToBeProcessed)
        );
        return pendingToBeProcessed;
    }

    private BackgroundTaskProcessor selectProcessor(final BatchTask pendingToBeProcessed) {
        return taskProcessors.stream()
            .filter(processor -> pendingToBeProcessed.getType().equals(processor.getType()))
            .findFirst()
            .orElse(processorNotFound);
    }
}
