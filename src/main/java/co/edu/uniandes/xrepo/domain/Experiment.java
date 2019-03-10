package co.edu.uniandes.xrepo.domain;


import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Experiment.
 */
@Document(collection = "experiment")
public class Experiment implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @NotNull
    @Size(max = 30)
    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("notes")
    private String notes;

    @NotNull
    @Field("created")
    private Instant created;

    @NotNull
    @Field("created_by")
    private String createdBy;

    @DBRef
    @Field("system")
    private TargetSystem system;

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

    public Experiment name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Experiment description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public Experiment notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreated() {
        return created;
    }

    public Experiment created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Experiment createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public TargetSystem getSystem() {
        return system;
    }

    public Experiment system(TargetSystem targetSystem) {
        this.system = targetSystem;
        return this;
    }

    public void setSystem(TargetSystem targetSystem) {
        this.system = targetSystem;
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
        Experiment experiment = (Experiment) o;
        if (experiment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), experiment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Experiment{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", notes='" + getNotes() + "'" +
            ", created='" + getCreated() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
