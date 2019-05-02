package co.edu.uniandes.xrepo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.xrepo.domain.BatchTask;
import co.edu.uniandes.xrepo.domain.enumeration.TaskState;
import co.edu.uniandes.xrepo.domain.enumeration.TaskType;

/**
 * Spring Data MongoDB repository for the BatchTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BatchTaskRepository extends MongoRepository<BatchTask, String> {

    List<BatchTask> findByState(TaskState state);

    Page<BatchTask> findByTypeAndUser(TaskType type, String user, Pageable pageable);
}
