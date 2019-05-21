package co.edu.uniandes.xrepo.repository.custom;

import java.util.Collections;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.xrepo.domain.Sampling;

@Repository
public class SamplingRepositoryImpl implements SamplingRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    public SamplingRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Sampling> findByTagsAndConditions(List<String> tags, List<String> conditionsName) {
        if (tags.isEmpty() && conditionsName.isEmpty()) {
            return Collections.emptyList();
        }

        Criteria criteria = new Criteria();
        if (!tags.isEmpty()) {
            criteria.and("tags").all(tags);
        }

        if (!conditionsName.isEmpty()) {
            criteria.and("conditions.varName").all(conditionsName);
        }
        return mongoTemplate.find(new Query(criteria), Sampling.class);
    }
}
