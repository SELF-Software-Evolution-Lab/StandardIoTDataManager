package co.edu.uniandes.xrepo.repository;

import co.edu.uniandes.xrepo.domain.BatchTask;
import co.edu.uniandes.xrepo.domain.enumeration.TaskState;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the BatchTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BatchTaskRepository extends MongoRepository<BatchTask, String> {

    List<BatchTask> findByState(TaskState state);

}
