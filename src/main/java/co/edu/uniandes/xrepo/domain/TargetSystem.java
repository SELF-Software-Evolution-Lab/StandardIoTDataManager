package co.edu.uniandes.xrepo.domain;


import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import co.edu.uniandes.xrepo.domain.metadata.OperativeRange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A TargetSystem.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Document(collection = "target_system")
@TypeAlias("xrepo:target_system")
public class TargetSystem implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @NotNull
    @Size(max = 30)
    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @NotNull
    @Field("created")
    private Instant created;

    @NotNull
    @Field("created_by")
    private String createdBy;

    @DBRef
    @Field("organization")
    private Organization organization;

    @Field("operativeRanges")
    private List<OperativeRange> operativeRanges= new ArrayList<>();

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

    public TargetSystem name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public TargetSystem description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreated() {
        return created;
    }

    public TargetSystem created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public TargetSystem createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Organization getOrganization() {
        return organization;
    }

    public TargetSystem organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public List<OperativeRange> getOperativeRanges() {
        return operativeRanges;
    }

    public TargetSystem operativeRanges(List<OperativeRange> operativeRanges) {
        this.operativeRanges = operativeRanges;
        return this;
    }

    public void setOperativeRanges(List<OperativeRange> operativeRanges) {
        this.operativeRanges = operativeRanges;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

}
