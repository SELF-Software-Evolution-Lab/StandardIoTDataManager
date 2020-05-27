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

/**
 * A Laboratory.
 */
@Document(collection = "laboratory")
public class Laboratory implements Serializable {

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

    @Field("date_created")
    private Instant dateCreated;

    @Size(max = 2048)
    @Field("share_url")
    private String shareUrl;

    @Field("share_valid_thru")
    private Instant shareValidThru;

    @DBRef
    @Field("algorithm")
    private Set<Algorithm> algorithms = new HashSet<>();
    @DBRef
    @Field("subSet")
    private Set<SubSet> subSets = new HashSet<>();
    @DBRef
    @Field("sampling")
    @JsonIgnoreProperties("laboratories")
    private Sampling sampling;

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

    public Laboratory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Laboratory description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public Laboratory dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public Laboratory shareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
        return this;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public Instant getShareValidThru() {
        return shareValidThru;
    }

    public Laboratory shareValidThru(Instant shareValidThru) {
        this.shareValidThru = shareValidThru;
        return this;
    }

    public void setShareValidThru(Instant shareValidThru) {
        this.shareValidThru = shareValidThru;
    }

    public Set<Algorithm> getAlgorithms() {
        return algorithms;
    }

    public Laboratory algorithms(Set<Algorithm> algorithms) {
        this.algorithms = algorithms;
        return this;
    }

    public Laboratory addAlgorithm(Algorithm algorithm) {
        this.algorithms.add(algorithm);
        algorithm.setLaboratory(this);
        return this;
    }

    public Laboratory removeAlgorithm(Algorithm algorithm) {
        this.algorithms.remove(algorithm);
        algorithm.setLaboratory(null);
        return this;
    }

    public void setAlgorithms(Set<Algorithm> algorithms) {
        this.algorithms = algorithms;
    }

    public Set<SubSet> getSubSets() {
        return subSets;
    }

    public Laboratory subSets(Set<SubSet> subSets) {
        this.subSets = subSets;
        return this;
    }

    public Laboratory addSubSet(SubSet subSet) {
        this.subSets.add(subSet);
        subSet.setLaboratory(this);
        return this;
    }

    public Laboratory removeSubSet(SubSet subSet) {
        this.subSets.remove(subSet);
        subSet.setLaboratory(null);
        return this;
    }

    public void setSubSets(Set<SubSet> subSets) {
        this.subSets = subSets;
    }

    public Sampling getSampling() {
        return sampling;
    }

    public Laboratory sampling(Sampling sampling) {
        this.sampling = sampling;
        return this;
    }

    public void setSampling(Sampling sampling) {
        this.sampling = sampling;
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
        Laboratory laboratory = (Laboratory) o;
        if (laboratory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), laboratory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Laboratory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", shareUrl='" + getShareUrl() + "'" +
            ", shareValidThru='" + getShareValidThru() + "'" +
            "}";
    }
}
