package co.edu.uniandes.xrepo.domain;


import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import co.edu.uniandes.xrepo.domain.metadata.OperativeCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A Sampling.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Document(collection = "sampling")
@TypeAlias("xrepo:sampling")
public class Sampling implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @NotNull
    @Size(max = 30)
    @Field("name")
    private String name;

    @Field("notes")
    private String notes;

    @Field("start_time")
    private Instant startTime;

    @Field("end_time")
    private Instant endTime;

    @NotNull
    @Field("target_system_id")
    private String targetSystemId;

    @DBRef
    @Field("experiment")
    private Experiment experiment;

    @Field("devices")
    private List<Device> devices = new ArrayList<>();

    @Field("sensor")
    private Set<Sensor> sensors = new HashSet<>();

    @Field("device_sensor")
    private Map<String, Set<String>> deviceSensor = new HashMap<>();

    @Field("conditions")
    private List<OperativeCondition> conditions = new ArrayList<>();

    @Field("tags")
    private List<String> tags = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Sampling name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public Sampling notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Sampling startTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public Sampling endTime(Instant endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public Sampling experiment(Experiment experiment) {
        this.experiment = experiment;
        return this;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public Sampling devices(List<Device> devices) {
        this.devices = devices;
        return this;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public List<OperativeCondition> getConditions() {
        return conditions;
    }

    public Sampling conditions(List<OperativeCondition> conditions) {
        this.conditions = conditions;
        return this;
    }

    public void setConditions(List<OperativeCondition> conditions) {
        this.conditions = conditions;
    }

    public Set<Sensor> getSensors() {
        return sensors;
    }

    public Sampling sensors(Set<Sensor> sensors) {
        this.sensors = sensors;
        return this;
    }

    public void setSensors(Set<Sensor> sensors) {
        this.sensors = sensors;
    }

    public List<String> getTags() {
        return tags;
    }

    public Sampling tags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Map<String, Set<String>> getDeviceSensor() {
        return deviceSensor;
    }

    public void setDeviceSensor(Map<String, Set<String>> deviceSensor) {
        this.deviceSensor = deviceSensor;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public String getTargetSystemId() {
        return targetSystemId;
    }

    public void setTargetSystemId(String targetSystemId) {
        this.targetSystemId = targetSystemId;
    }
}
