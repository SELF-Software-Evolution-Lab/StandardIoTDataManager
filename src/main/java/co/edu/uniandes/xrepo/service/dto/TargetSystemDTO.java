package co.edu.uniandes.xrepo.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import co.edu.uniandes.xrepo.domain.metadata.OperativeRange;
import lombok.ToString;

/**
 * A DTO for the TargetSystem entity.
 */
@ToString
public class TargetSystemDTO implements Serializable {

    private String id;

    @NotNull
    @Size(max = 30)
    private String name;

    private String description;

    private Instant created;

    private String createdBy;

    private String organizationId;

    private String organizationName;

    private List<OperativeRange> operativeRanges = new ArrayList<>();

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

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public List<OperativeRange> getOperativeRanges() {
        return operativeRanges;
    }

    public void setOperativeRanges(List<OperativeRange> operativeRanges) {
        this.operativeRanges = operativeRanges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TargetSystemDTO targetSystemDTO = (TargetSystemDTO) o;
        if (targetSystemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), targetSystemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
