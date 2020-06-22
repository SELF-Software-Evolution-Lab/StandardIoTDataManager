package co.edu.uniandes.xrepo.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import co.edu.uniandes.xrepo.domain.enumeration.SubSetType;

/**
 * A Algorithm.
 */
@Document(collection = "algorithm")
public class Algorithm implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @NotNull
    @Size(max = 30)
    @Field("name")
    private String name;

    @Size(max = 1024)
    @Field("description")
    private String description;

    @Field("mapper_text")
    private String mapperText;

    @Size(max = 2048)
    @Field("mapper_file_url")
    private String mapperFileUrl;

    @Field("reducer_text")
    private String reducerText;

    @Size(max = 2048)
    @Field("reducer_file_url")
    private String reducerFileUrl;

    @Field("date_created")
    private Instant dateCreated;

    @Field("last_successful_run")
    private Instant lastSuccessfulRun;

    @NotNull
    @Field("set_type")
    private SubSetType setType;

    @DBRef
    @Field("laboratory")
    @JsonIgnoreProperties("algorithms")
    private Laboratory laboratory;

    @DBRef
    @Field("subSet")
    private Set<SubSet> subSets = new HashSet<>();
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

    public Algorithm name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Algorithm description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMapperText() {
        return mapperText;
    }

    public Algorithm mapperText(String mapperText) {
        this.mapperText = mapperText;
        return this;
    }

    public void setMapperText(String mapperText) {
        this.mapperText = mapperText;
    }

    public String getMapperFileUrl() {
        return mapperFileUrl;
    }

    public Algorithm mapperFileUrl(String mapperFileUrl) {
        this.mapperFileUrl = mapperFileUrl;
        return this;
    }

    public void setMapperFileUrl(String mapperFileUrl) {
        this.mapperFileUrl = mapperFileUrl;
    }

    public String getReducerText() {
        return reducerText;
    }

    public Algorithm reducerText(String reducerText) {
        this.reducerText = reducerText;
        return this;
    }

    public void setReducerText(String reducerText) {
        this.reducerText = reducerText;
    }

    public String getReducerFileUrl() {
        return reducerFileUrl;
    }

    public Algorithm reducerFileUrl(String reducerFileUrl) {
        this.reducerFileUrl = reducerFileUrl;
        return this;
    }

    public void setReducerFileUrl(String reducerFileUrl) {
        this.reducerFileUrl = reducerFileUrl;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public Algorithm dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getLastSuccessfulRun() {
        return lastSuccessfulRun;
    }

    public Algorithm lastSuccessfulRun(Instant lastSuccessfulRun) {
        this.lastSuccessfulRun = lastSuccessfulRun;
        return this;
    }

    public void setLastSuccessfulRun(Instant lastSuccessfulRun) {
        this.lastSuccessfulRun = lastSuccessfulRun;
    }

    public SubSetType getSetType() {
        return setType;
    }

    public Algorithm setType(SubSetType setType) {
        this.setType = setType;
        return this;
    }

    public void setSetType(SubSetType setType) {
        this.setType = setType;
    }

    public Laboratory getLaboratory() {
        return laboratory;
    }

    public Algorithm laboratory(Laboratory laboratory) {
        this.laboratory = laboratory;
        return this;
    }

    public void setLaboratory(Laboratory laboratory) {
        this.laboratory = laboratory;
    }

    public Set<SubSet> getSubSets() {
        return subSets;
    }

    public Algorithm subSets(Set<SubSet> subSets) {
        this.subSets = subSets;
        return this;
    }

    public Algorithm addSubSet(SubSet subSet) {
        this.subSets.add(subSet);
        subSet.setAlgorithm(this);
        return this;
    }

    public Algorithm removeSubSet(SubSet subSet) {
        this.subSets.remove(subSet);
        subSet.setAlgorithm(null);
        return this;
    }

    public void setSubSets(Set<SubSet> subSets) {
        this.subSets = subSets;
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
        Algorithm algorithm = (Algorithm) o;
        if (algorithm.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), algorithm.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Algorithm{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", mapperText='" + getMapperText() + "'" +
            ", mapperFileUrl='" + getMapperFileUrl() + "'" +
            ", reducerText='" + getReducerText() + "'" +
            ", reducerFileUrl='" + getReducerFileUrl() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", lastSuccessfulRun='" + getLastSuccessfulRun() + "'" +
            ", setType='" + getSetType() + "'" +
            "}";
    }
}
