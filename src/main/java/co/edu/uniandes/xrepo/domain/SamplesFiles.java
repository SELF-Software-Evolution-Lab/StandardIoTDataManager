package co.edu.uniandes.xrepo.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A SamplesFiles.
 */
@Document(collection = "samples_files")
public class SamplesFiles implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("path")
    private String path;

    @Field("content_type")
    private String contentType;

    @Field("size")
    private BigDecimal size;

    @Field("state")
    private Integer state;

    @Field("result")
    private String result;

    @Field("create_date_time")
    private LocalDate createDateTime;

    @Field("update_date_time")
    private LocalDate updateDateTime;

    @Field("records_processed")
    private Integer recordsProcessed;

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

    public SamplesFiles name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public SamplesFiles path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContentType() {
        return contentType;
    }

    public SamplesFiles contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public BigDecimal getSize() {
        return size;
    }

    public SamplesFiles size(BigDecimal size) {
        this.size = size;
        return this;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public Integer getState() {
        return state;
    }

    public SamplesFiles state(Integer state) {
        this.state = state;
        return this;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getResult() {
        return result;
    }

    public SamplesFiles result(String result) {
        this.result = result;
        return this;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDate getCreateDateTime() {
        return createDateTime;
    }

    public SamplesFiles createDateTime(LocalDate createDateTime) {
        this.createDateTime = createDateTime;
        return this;
    }

    public void setCreateDateTime(LocalDate createDateTime) {
        this.createDateTime = createDateTime;
    }

    public LocalDate getUpdateDateTime() {
        return updateDateTime;
    }

    public SamplesFiles updateDateTime(LocalDate updateDateTime) {
        this.updateDateTime = updateDateTime;
        return this;
    }

    public void setUpdateDateTime(LocalDate updateDateTime) {
        this.updateDateTime = updateDateTime;
    }
    
    public Integer getRecordsProcessed() {
        return recordsProcessed;
    }

    public SamplesFiles recordsProcessed(Integer recordsProcessed) {
        this.recordsProcessed = recordsProcessed;
        return this;
    }

    public void setRecordsProcessed(Integer recordsProcessed) {
        this.recordsProcessed = recordsProcessed;
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
        SamplesFiles samplesFiles = (SamplesFiles) o;
        if (samplesFiles.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), samplesFiles.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SamplesFiles{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", path='" + getPath() + "'" +
            ", contentType='" + getContentType() + "'" +
            ", size=" + getSize() +
            ", state=" + getState() +
            ", result='" + getResult() + "'" +
            ", createDateTime='" + getCreateDateTime() + "'" +
            ", updateDateTime='" + getUpdateDateTime() + "'" +
            "}";
    }
}
