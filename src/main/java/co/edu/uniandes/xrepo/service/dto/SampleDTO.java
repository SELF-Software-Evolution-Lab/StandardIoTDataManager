package co.edu.uniandes.xrepo.service.dto;
import java.math.BigDecimal;

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
    private SampleInstant ts;

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

    public SampleInstant getTs() {
        return ts;
    }

    public void setTs(SampleInstant ts) {
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

    public static final class SampleInstant{
        /**
         * The number of epochSeconds from the epoch of 1970-01-01T00:00:00Z.
         */
        private long epochSeconds;
        /**
         * The number of nanoseconds, later along the time-line, from the epochSeconds field.
         * This is always positive, and never exceeds 999,999,999.
         */
        private long nanos;

        public SampleInstant() {
        }

        public SampleInstant(long epochSeconds, long nanos) {
            this.epochSeconds = epochSeconds;
            this.nanos = nanos;
        }

        public long getEpochSeconds() {
            return epochSeconds;
        }

        public void setEpochSeconds(long epochSeconds) {
            this.epochSeconds = epochSeconds;
        }

        public long getNanos() {
            return nanos;
        }

        public void setNanos(long nanos) {
            this.nanos = nanos;
        }
    }

}
