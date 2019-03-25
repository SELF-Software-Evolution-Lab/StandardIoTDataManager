package co.edu.uniandes.xrepo.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the Experiment entity.
 */
public class ExperimentDTO implements Serializable {

    private String id;

    @NotNull
    @Size(max = 30)
    private String name;

    private String description;

    private String notes;

    private Instant created;

    private String createdBy;

    private String systemId;

    private String systemName;

    private List<String> tags;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String targetSystemId) {
        this.systemId = targetSystemId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String targetSystemName) {
        this.systemName = targetSystemName;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExperimentDTO experimentDTO = (ExperimentDTO) o;
        if (experimentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), experimentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExperimentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", notes='" + getNotes() + "'" +
            ", created='" + getCreated() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", system=" + getSystemId() +
            ", system='" + getSystemName() + "'" +
            "}";
    }
}
