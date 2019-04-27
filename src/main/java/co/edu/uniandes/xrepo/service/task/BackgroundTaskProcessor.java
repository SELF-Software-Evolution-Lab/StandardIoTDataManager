package co.edu.uniandes.xrepo.service.task;


import co.edu.uniandes.xrepo.service.task.BackgroundTaskProvider.Task;
import co.edu.uniandes.xrepo.service.task.BackgroundTaskProvider.Task.TaskType;

public interface BackgroundTaskProcessor {

    TaskType getType();

    void processTask(Task ptpTask);
}
