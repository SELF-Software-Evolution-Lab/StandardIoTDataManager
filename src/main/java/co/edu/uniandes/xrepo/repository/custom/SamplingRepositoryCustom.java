package co.edu.uniandes.xrepo.repository.custom;

import java.util.List;

import co.edu.uniandes.xrepo.domain.Sampling;

public interface SamplingRepositoryCustom {

    List<Sampling> findByTagsAndConditions(List<String> tags, List<String> conditionsName);
}
