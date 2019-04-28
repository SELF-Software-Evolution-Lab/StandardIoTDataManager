package co.edu.uniandes.xrepo.service.task;

import co.edu.uniandes.xrepo.domain.enumeration.TaskType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import co.edu.uniandes.xrepo.domain.BatchTask;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BackgroundTaskExecutorTest {


    @Test
    public void executorCallProcessorsByTaskType() throws Exception {
        //Given
        CountDownLatch latch = new CountDownLatch(4);
        BackgroundTaskProvider taskProvider = mock(BackgroundTaskProvider.class);
        when(taskProvider.listPendingTasks()).thenReturn(buildTasks());
        BackgroundTaskProcessor undefinedProcessor = spy(new BackgroundTaskProcessor() {

            @Override
            public TaskType getType() {
                return TaskType.UNDEFINED;
            }

            @Override
            public void processTask(BatchTask ptpTask) {
                latch.countDown();
                log.info("" + Thread.currentThread().getName());
            }
        });
        BackgroundTaskProcessor reportProcessor = spy(new BackgroundTaskProcessor() {

            @Override
            public TaskType getType() {
                return TaskType.REPORT;
            }

            @Override
            public void processTask(BatchTask ptpTask) {
                latch.countDown();
                log.info("" + Thread.currentThread().getName());
            }
        });
        BackgroundTaskProcessor fileLoadProcessor = spy(new BackgroundTaskProcessor() {

            @Override
            public TaskType getType() {
                return TaskType.FILE_LOAD;
            }

            @Override
            public void processTask(BatchTask ptpTask) {
                latch.countDown();
                log.info("" + Thread.currentThread().getName());
            }
        });

        List<BackgroundTaskProcessor> processors = Arrays.asList(undefinedProcessor, reportProcessor, fileLoadProcessor);

        // When
        new BackgroundTaskExecutor(processors, taskProvider, 2).processPendingTasks();
        latch.await(5, TimeUnit.SECONDS);

        // Then
        verify(reportProcessor, times(1)).processTask(any());
        verify(fileLoadProcessor, times(1)).processTask(any());
        verify(undefinedProcessor, times(3)).processTask(any());
    }

    private List<BatchTask> buildTasks() {
        return Arrays.asList(new BatchTask(TaskType.UNDEFINED)
            , new BatchTask(TaskType.REPORT)
            , new BatchTask(TaskType.FILE_LOAD)
            , new BatchTask(TaskType.UNDEFINED)
            , new BatchTask(TaskType.UNDEFINED)
        );
    }
}
