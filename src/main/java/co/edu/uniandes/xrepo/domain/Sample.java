package co.edu.uniandes.xrepo.domain;


import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Sample.
 */
@Document(collection = "sample")
public class Sample implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @NotNull
    @Field("ts")
    private Instant ts;

    @Field("sensor_internal_id")
    private String sensorInternalId;

    @Field("sampling_id")
    private String samplingId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getTs() {
        return ts;
    }

    public Sample ts(Instant ts) {
        this.ts = ts;
        return this;
    }

    public void setTs(Instant ts) {
        this.ts = ts;
    }

    public String getSensorInternalId() {
        return sensorInternalId;
    }

    public Sample sensorInternalId(String sensorInternalId) {
        this.sensorInternalId = sensorInternalId;
        return this;
    }

    public void setSensorInternalId(String sensorInternalId) {
        this.sensorInternalId = sensorInternalId;
    }

    public String getSamplingId() {
        return samplingId;
    }

    public Sample samplingId(String samplingId) {
        this.samplingId = samplingId;
        return this;
    }

    public void setSamplingId(String samplingId) {
        this.samplingId = samplingId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sample sample = (Sample) o;
        if (sample.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sample.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Sample{" +
            "id=" + getId() +
            ", ts='" + getTs() + "'" +
            ", sensorInternalId='" + getSensorInternalId() + "'" +
            ", samplingId='" + getSamplingId() + "'" +
            "}";
    }
}
