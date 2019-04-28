package co.edu.uniandes.xrepo.service.dto;
import java.math.BigDecimal;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import lombok.ToString;

/**
 * A DTO for the Sample entity.
 */
@ToString
public class SampleDTO implements Serializable {

    private String id;

    @NotNull
    private LocalDateTime dateTime;

    @NotNull
    private Instant ts;

    @NotNull
    private String sensorInternalId;

    @NotNull
    private String samplingId;

    private Map<String, BigDecimal> measurements;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Instant getTs() {
        return ts;
    }

    public void setTs(Instant ts) {
        this.ts = ts;
    }

    public String getSensorInternalId() {
        return sensorInternalId;
    }

    public void setSensorInternalId(String sensorInternalId) {
        this.sensorInternalId = sensorInternalId;
    }

    public String getSamplingId() {
        return samplingId;
    }

    public void setSamplingId(String samplingId) {
        this.samplingId = samplingId;
    }

    public Map<String, BigDecimal> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(Map<String, BigDecimal> measurements) {
        this.measurements = measurements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SampleDTO sampleDTO = (SampleDTO) o;
        if (sampleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sampleDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}