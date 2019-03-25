package co.edu.uniandes.xrepo.domain;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A Device.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @NotNull
    @Field("internal_id")
    private String internalId;

    @NotNull
    @Size(max = 30)
    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("specs")
    private Map<String, String> specs = new HashMap<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
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

    public Map<String, String> getSpecs() {
        return specs;
    }

    public Device specs(Map<String, String> specs) {
        this.specs = specs;
        return this;
    }

    public void setSpecs(Map<String, String> specs) {
        this.specs = specs;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return internalId.equals(device.internalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(internalId);
    }
}
