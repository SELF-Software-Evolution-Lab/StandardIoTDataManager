package co.edu.uniandes.xrepo.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import co.edu.uniandes.xrepo.domain.Device;
import co.edu.uniandes.xrepo.domain.Sensor;
import co.edu.uniandes.xrepo.domain.metadata.OperativeCondition;

/**
 * A DTO for the Sampling entity.
 */
public class SamplingDTO implements Serializable {

    private String id;

    @NotNull
    @Size(max = 30)
    private String name;

    private String notes;

    private Instant startTime;

    private Instant endTime;

    private String experimentId;

    private String experimentName;

    private List<String> fileUris;

    private List<String> tags;

    private List<Device> devices;

    private List<SensorDTO> sensors;

    private List<OperativeCondition> conditions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }

    public String getExperimentName() {
        return experimentName;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getFileUris() { return fileUris; }

    public void setFileUris(List<String> fileUris) { this.fileUris = fileUris; }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public List<OperativeCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<OperativeCondition> conditions) {
        this.conditions = conditions;
    }

    public List<SensorDTO> getSensors() {
        return sensors;
    }

    public void setSensors(List<SensorDTO> sensors) {
        this.sensors = sensors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SamplingDTO samplingDTO = (SamplingDTO) o;
        if (samplingDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), samplingDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SamplingDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", notes='" + getNotes() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", experiment=" + getExperimentId() +
            ", experiment='" + getExperimentName() + "'" +
            "}";
    }
}
