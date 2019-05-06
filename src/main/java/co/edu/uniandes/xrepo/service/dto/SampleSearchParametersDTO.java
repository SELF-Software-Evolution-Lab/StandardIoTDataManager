package co.edu.uniandes.xrepo.service.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;

import co.edu.uniandes.xrepo.domain.metadata.OperativeRange;

public class SampleSearchParametersDTO implements Serializable {

    private List<String> targetSystemId = new ArrayList<>();

    private List<String> experimentsId = new ArrayList<>();

    private List<String> samplingsId = new ArrayList<>();

    private List<String> sensorsId = new ArrayList<>();

    private List<OperativeRange> operativeConditions = new ArrayList<>();

    private LocalDateTime fromDateTime;
    private LocalDateTime toDateTime;

    private List<String> tags = new ArrayList<>();

    private long expectedRecords;

    public Criteria asCriteria() {
        final Criteria mainCriteria = new Criteria();
        targetSystemCriteria(mainCriteria);
        sensorCriteria(mainCriteria);
        tagBasedCriteria(mainCriteria);
        dateRangeCriteria(mainCriteria);
        return mainCriteria;
    }

    private void tagBasedCriteria(Criteria mainCriteria) {
        final List<Criteria> tagBasedConditions = new ArrayList<>(2);
        if (!experimentsId.isEmpty()) {
            tagBasedConditions.add(Criteria.where("experimentId").in(experimentsId));
        }
        if (!samplingsId.isEmpty()) {
            tagBasedConditions.add(Criteria.where("samplingId").in(samplingsId));
        }
        if (!tagBasedConditions.isEmpty()) {
            Criteria criteria = new Criteria().orOperator(tagBasedConditions.toArray(new Criteria[0]));
            mainCriteria.andOperator(criteria);
        }
    }

    private void targetSystemCriteria(Criteria mainCriteria) {
        if (!targetSystemId.isEmpty()) {
            mainCriteria.and("targetSystemId").in(getTargetSystemId());
        }
    }

    private void sensorCriteria(Criteria mainCriteria) {
        if (!sensorsId.isEmpty()) {
            mainCriteria.and("sensorInternalId").in(getSensorsId());
        }
    }

    private void dateRangeCriteria(Criteria mainCriteria) {
        final List<Criteria> rangeConditions = new ArrayList<>(2);
        if (fromDateTime != null) {
            rangeConditions.add(Criteria.where("dateTime").gte(Date.from(getFromDateTime().atZone(ZoneOffset.UTC).toInstant())));
        }
        if (toDateTime != null) {
            rangeConditions.add(Criteria.where("dateTime").lte(Date.from(getToDateTime().atZone(ZoneOffset.UTC).toInstant())));
        }
        if (!rangeConditions.isEmpty()) {
            mainCriteria.andOperator(rangeConditions.toArray(new Criteria[0]));
        }
    }

    public List<String> getTargetSystemId() {
        return targetSystemId;
    }

    public void setTargetSystemId(List<String> targetSystemId) {
        this.targetSystemId = targetSystemId;
    }

    public List<String> getSensorsId() {
        return sensorsId;
    }

    public void setSensorsId(List<String> sensorsId) {
        this.sensorsId = sensorsId;
    }

    public LocalDateTime getFromDateTime() {
        return fromDateTime;
    }

    public void setFromDateTime(LocalDateTime fromDateTime) {
        this.fromDateTime = fromDateTime;
    }

    public LocalDateTime getToDateTime() {
        return toDateTime;
    }

    public void setToDateTime(LocalDateTime toDateTime) {
        this.toDateTime = toDateTime;
    }

    public List<String> getExperimentsId() {
        return experimentsId;
    }

    public void setExperimentsId(List<String> experimentsId) {
        this.experimentsId = experimentsId;
    }

    public List<String> getSamplingsId() {
        return samplingsId;
    }

    public void setSamplingsId(List<String> samplingsId) {
        this.samplingsId = samplingsId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public long getExpectedRecords() {
        return expectedRecords;
    }

    public void setExpectedRecords(long expectedRecords) {
        this.expectedRecords = expectedRecords;
    }

    public List<OperativeRange> getOperativeConditions() {
        return operativeConditions;
    }

    public void setOperativeConditions(List<OperativeRange> operativeConditions) {
        this.operativeConditions = operativeConditions;
    }
}
