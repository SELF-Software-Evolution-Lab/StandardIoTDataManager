package co.edu.uniandes.xrepo.service.task;

import co.edu.uniandes.xrepo.domain.BatchTask;
import co.edu.uniandes.xrepo.domain.enumeration.TypeTask;

public interface BackgroundTaskProcessor {

    TypeTask getType();

    void processTask(BatchTask ptpTask);
}
