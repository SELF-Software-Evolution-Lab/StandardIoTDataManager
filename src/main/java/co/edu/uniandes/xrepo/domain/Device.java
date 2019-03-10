package co.edu.uniandes.xrepo.domain;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Device.
 */
@Document(collection = "device")
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @NotNull
    @Field("internal_id")
    private String internalId;

    @NotNull
    @Size(max = 30)
    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @DBRef
    @Field("sensor")
    private Set<Sensor> sensors = new HashSet<>();
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

    public Device internalId(String internalId) {
        this.internalId = internalId;
        return this;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public String getName() {
        return name;
    }

    public Device name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Device description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Sensor> getSensors() {
        return sensors;
    }

    public Device sensors(Set<Sensor> sensors) {
        this.sensors = sensors;
        return this;
    }

    public Device addSensor(Sensor sensor) {
        this.sensors.add(sensor);
        sensor.setDevice(this);
        return this;
    }

    public Device removeSensor(Sensor sensor) {
        this.sensors.remove(sensor);
        sensor.setDevice(null);
        return this;
    }

    public void setSensors(Set<Sensor> sensors) {
        this.sensors = sensors;
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
        Device device = (Device) o;
        if (device.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), device.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Device{" +
            "id=" + getId() +
            ", internalId='" + getInternalId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
