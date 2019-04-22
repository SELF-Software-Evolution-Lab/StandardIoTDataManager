package co.edu.uniandes.xrepo.service.reports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.CloseableIterator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import co.edu.uniandes.xrepo.domain.Sample;
import co.edu.uniandes.xrepo.service.dto.SampleSearchParametersDTO;

@Service
public class ReportService {
    private final Logger log = LoggerFactory.getLogger(ReportService.class);

    private final MongoTemplate mongoTemplate;

    public ReportService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Async
    public void asyncReport(SampleSearchParametersDTO params){
        Query query = new Query(params.asCriteria());
        log.info("Async processing report for query: {}", query.getQueryObject().toJson());
        CloseableIterator<Sample> stream = mongoTemplate.stream(query, Sample.class);
        //stream.forEachRemaining(sample -> );
        stream.close();
    }
}
