package co.edu.uniandes.xrepo.service.reports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.CloseableIterator;
import org.springframework.stereotype.Service;

import co.edu.uniandes.xrepo.domain.Sample;
import co.edu.uniandes.xrepo.service.dto.SampleSearchParametersDTO;
import co.edu.uniandes.xrepo.service.task.BackgroundTaskProcessor;
import co.edu.uniandes.xrepo.service.task.BackgroundTaskProvider;
import co.edu.uniandes.xrepo.service.task.BackgroundTaskProvider.Task.TaskType;

@Service
public class ReportTaskProcessor implements BackgroundTaskProcessor {
    private final Logger log = LoggerFactory.getLogger(ReportTaskProcessor.class);

    private final MongoTemplate mongoTemplate;

    public ReportTaskProcessor(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void asyncReport(SampleSearchParametersDTO params) {
        Query query = new Query(params.asCriteria());
        log.info("Async processing report for query: {}", query.getQueryObject().toJson());
        CloseableIterator<Sample> stream = mongoTemplate.stream(query, Sample.class);
        //stream.forEachRemaining(sample -> );
        stream.close();
    }

    @Override
    public TaskType getType() {
        return TaskType.REPORT;
    }

    @Override
    public void processTask(BackgroundTaskProvider.Task ptpTask) {
        log.info("Starting ReportService for task {}", ptpTask);
    }
}
