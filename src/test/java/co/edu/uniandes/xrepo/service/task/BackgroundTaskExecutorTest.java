package co.edu.uniandes.xrepo.service.task;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import co.edu.uniandes.xrepo.service.task.BackgroundTaskProvider.Task;
import co.edu.uniandes.xrepo.service.task.BackgroundTaskProvider.Task.TaskType;
import lombok.extern.slf4j.Slf4j;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
            public void processTask(Task ptpTask) {
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
            public void processTask(Task ptpTask) {
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
            public void processTask(Task ptpTask) {
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

    private List<Task> buildTasks() {
        return Arrays.asList(new Task(TaskType.UNDEFINED, null)
            , new Task(TaskType.REPORT, null)
            , new Task(TaskType.FILE_LOAD, null)
            , new Task(TaskType.UNDEFINED, null)
            , new Task(TaskType.UNDEFINED, null)
        );
    }
}
