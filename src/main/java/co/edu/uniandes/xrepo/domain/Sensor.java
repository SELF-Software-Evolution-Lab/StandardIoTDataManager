package co.edu.uniandes.xrepo.domain;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Sensor.
 */
@Document(collection = "sensor")
public class Sensor implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @NotNull
    @Field("internal_id")
    private String internalId;

    @NotNull
    @Field("sensor_type")
    private String sensorType;

    @Field("potential_freq")
    private BigDecimal potentialFreq;

    @Field("sampling_freq")
    private BigDecimal samplingFreq;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInternalId() {
        return internalId;
    }

    public Sensor internalId(String internalId) {
        this.internalId = internalId;
        return this;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public String getSensorType() {
        return sensorType;
    }

    public Sensor sensorType(String sensorType) {
        this.sensorType = sensorType;
        return this;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public BigDecimal getPotentialFreq() {
        return potentialFreq;
    }

    public Sensor potentialFreq(BigDecimal potentialFreq) {
        this.potentialFreq = potentialFreq;
        return this;
    }

    public void setPotentialFreq(BigDecimal potentialFreq) {
        this.potentialFreq = potentialFreq;
    }

    public BigDecimal getSamplingFreq() {
        return samplingFreq;
    }

    public Sensor samplingFreq(BigDecimal samplingFreq) {
        this.samplingFreq = samplingFreq;
        return this;
    }

    public void setSamplingFreq(BigDecimal samplingFreq) {
        this.samplingFreq = samplingFreq;
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
        Sensor sensor = (Sensor) o;
        if (sensor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sensor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Sensor{" +
            "id=" + getId() +
            ", internalId='" + getInternalId() + "'" +
            ", sensorType='" + getSensorType() + "'" +
            ", potentialFreq=" + getPotentialFreq() +
            ", samplingFreq=" + getSamplingFreq() +
            "}";
    }
}
