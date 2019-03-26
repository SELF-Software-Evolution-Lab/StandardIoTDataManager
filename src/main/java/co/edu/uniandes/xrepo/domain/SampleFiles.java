package co.edu.uniandes.xrepo.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.util.Objects;

/**
 * A SampleFiles.
 */
@Document(collection = "sample_files")
public class SampleFiles implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @Field("path")
    private byte[] path;

    @Field("path_content_type")
    private String pathContentType;

    @Field("name")
    private String name;

    @Field("size")
    private Long size;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getPath() {
        return path;
    }

    public SampleFiles path(byte[] path) {
        this.path = path;
        return this;
    }

    public void setPath(byte[] path) {
        this.path = path;
    }

    public String getPathContentType() {
        return pathContentType;
    }

    public SampleFiles pathContentType(String pathContentType) {
        this.pathContentType = pathContentType;
        return this;
    }

    public void setPathContentType(String pathContentType) {
        this.pathContentType = pathContentType;
    }

    public String getName() {
        return name;
    }

    public SampleFiles name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public SampleFiles size(Long size) {
        this.size = size;
        return this;
    }

    public void setSize(Long size) {
        this.size = size;
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
        SampleFiles sampleFiles = (SampleFiles) o;
        if (sampleFiles.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sampleFiles.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SampleFiles{" +
            "id=" + getId() +
            ", path='" + getPath() + "'" +
            ", pathContentType='" + getPathContentType() + "'" +
            ", name='" + getName() + "'" +
            ", size=" + getSize() +
            "}";
    }
}
