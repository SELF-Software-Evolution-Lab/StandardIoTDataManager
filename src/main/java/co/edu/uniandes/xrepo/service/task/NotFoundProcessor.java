package co.edu.uniandes.xrepo.service.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uniandes.xrepo.service.task.BackgroundTaskProvider.Task;
import co.edu.uniandes.xrepo.service.task.BackgroundTaskProvider.Task.TaskType;

@Service
public class NotFoundProcessor implements BackgroundTaskProcessor {

    private final Logger log = LoggerFactory.getLogger(NotFoundProcessor.class);

    private final BackgroundTaskProvider taskProvider;

    public NotFoundProcessor(BackgroundTaskProvider taskProvider) {
        this.taskProvider = taskProvider;
    }

    @Override
    public TaskType getType() {
        return TaskType.UNDEFINED;
    }

    @Override
    public void processTask(Task ptpTask) {
        log.error("Pending task cannot be processed, no processor found for type {}", ptpTask.getType());
        taskProvider.markTaskAsError(ptpTask);
    }


}
