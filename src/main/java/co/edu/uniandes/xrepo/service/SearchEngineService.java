package co.edu.uniandes.xrepo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.CloseableIterator;
import org.springframework.stereotype.Service;

import co.edu.uniandes.xrepo.domain.Sample;
import co.edu.uniandes.xrepo.repository.ExperimentRepository;
import co.edu.uniandes.xrepo.repository.SamplingRepository;
import co.edu.uniandes.xrepo.service.dto.SampleSearchParametersDTO;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SearchEngineService {

    private final SamplingRepository samplingRepository;

    private final ExperimentRepository experimentRepository;

    private final MongoTemplate mongoTemplate;

    public SearchEngineService(SamplingRepository samplingRepository, ExperimentRepository experimentRepository, MongoTemplate mongoTemplate) {
        this.samplingRepository = samplingRepository;
        this.experimentRepository = experimentRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public long preSearchSamples(SampleSearchParametersDTO params) {

        List<String> tags = params.getTags();
        if (!tags.isEmpty()) {
            List<String> samplingsId = samplingRepository.findWithTags(tags).stream()
                .map(sampling -> sampling.getId())
                .collect(Collectors.toList());

            List<String> experimentsId = experimentRepository.findWithTags(tags).stream()
                .map(experiment -> experiment.getId())
                .collect(Collectors.toList());

            params.setSamplingsId(samplingsId);
            params.setExperimentsId(experimentsId);
        }

        Query query = new Query(params.asCriteria());
        return mongoTemplate.count(query, Sample.class);
    }
}
