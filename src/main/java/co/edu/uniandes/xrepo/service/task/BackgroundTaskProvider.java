package co.edu.uniandes.xrepo.service.task;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.uniandes.xrepo.domain.BatchTask;
import co.edu.uniandes.xrepo.domain.enumeration.TaskState;
import co.edu.uniandes.xrepo.service.BatchTaskService;

/**
 * Hides access to Background Tasks repository
 */
@Service
public class BackgroundTaskProvider {

    BatchTaskService taskBatchService;

    public BackgroundTaskProvider(BatchTaskService taskBatchService) {
        this.taskBatchService = taskBatchService;
    }

    public void markTaskAsSubmitted(BatchTask task) {
        task.setState(TaskState.SUBMITTED);
        taskBatchService.save(task);
    }

    public void markTaskAsError(BatchTask errorTask) {
        errorTask.setState(TaskState.ERROR);
        taskBatchService.save(errorTask);
    }

    public List<BatchTask> listPendingTasks() {
        return taskBatchService.findAllPending();
    }
}
