package co.edu.uniandes.xrepo.service.task;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Hides access to Background Tasks repository
 */
@Service
public class BackgroundTaskProvider {

    public BackgroundTaskProvider() {

    }

    public void markTaskAsSubmitted(Task task) {

    }

    public void markTaskAsError(Task errorTask) {

    }

    public List<Task> listPendingTasks() {
        return Collections.emptyList();
    }

    @Getter
    @AllArgsConstructor
    public static class Task {
        public enum TaskType {
            UNDEFINED, REPORT, FILE_LOAD
        }

        TaskType type;
        String parameters;
    }
}
