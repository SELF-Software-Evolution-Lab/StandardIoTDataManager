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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import co.edu.uniandes.xrepo.domain.enumeration.SubSetType;

/**
 * A SubSet.
 */
@Document(collection = "sub_set")
public class SubSet implements Serializable {

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

    @NotNull
    @Size(max = 2048)
    @Field("file_hdfs_location")
    private List<String> fileHdfsLocation = new ArrayList<>();

    @Field("date_created")
    private Instant dateCreated;

    @Size(max = 2048)
    @Field("download_url")
    private List<String> downloadUrl;

    @NotNull
    @Field("set_type")
    private SubSetType setType;

    @DBRef
    @Field("laboratory")
    @JsonIgnoreProperties("subSets")
    private Laboratory laboratory;

    @DBRef
    @Field("algorithm")
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Algorithm algorithm;

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

    public SubSet name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public SubSet description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getFileHdfsLocation() {
        return fileHdfsLocation;
    }

    public List<String> getFileHdfsLocationAndType() {
        return fileHdfsLocation.stream().map(x -> x + "," + this.setType).collect(Collectors.toList());
    }


    public SubSet fileHdfsLocation(List<String> fileHdfsLocation) {
        this.fileHdfsLocation = fileHdfsLocation;
        return this;
    }

    public void setFileHdfsLocation(List<String> fileHdfsLocation) {
        this.fileHdfsLocation = fileHdfsLocation;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public SubSet dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<String> getDownloadUrl() {
        return downloadUrl;
    }

    public SubSet downloadUrl(List<String> downloadUrl) {
        this.downloadUrl = downloadUrl;
        return this;
    }

    public void setDownloadUrl(List<String> downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public SubSetType getSetType() {
        return setType;
    }

    public SubSet setType(SubSetType setType) {
        this.setType = setType;
        return this;
    }

    public void setSetType(SubSetType setType) {
        this.setType = setType;
    }

    public Laboratory getLaboratory() {
        return laboratory;
    }

    public SubSet laboratory(Laboratory laboratory) {
        this.laboratory = laboratory;
        return this;
    }

    public void setLaboratory(Laboratory laboratory) {
        this.laboratory = laboratory;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public SubSet algorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
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
        SubSet subSet = (SubSet) o;
        if (subSet.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subSet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SubSet{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", fileHdfsLocation='" + getFileHdfsLocation() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", downloadUrl='" + getDownloadUrl() + "'" +
            ", setType='" + getSetType() + "'" +
            "}";
    }
}
