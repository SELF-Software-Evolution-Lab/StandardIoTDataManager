package co.edu.uniandes.xrepo.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
