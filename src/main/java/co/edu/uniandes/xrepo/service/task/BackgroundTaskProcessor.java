package co.edu.uniandes.xrepo.service.task;

import co.edu.uniandes.xrepo.domain.BatchTask;
import co.edu.uniandes.xrepo.domain.enumeration.TaskType;

public interface BackgroundTaskProcessor {

    TaskType getType();

    void processTask(BatchTask ptpTask);
}
