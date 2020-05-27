package co.edu.uniandes.xrepo.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Algorithm entity.
 */
public class AlgorithmDTO implements Serializable {

    private String id;

    @NotNull
    @Size(max = 30)
    private String name;

    @Size(max = 1024)
    private String description;

    private String mapperText;

    @Size(max = 2048)
    private String mapperFileUrl;

    private String reducerText;

    @Size(max = 2048)
    private String reducerFileUrl;

    private Instant dateCreated;

    private Instant lastSuccessfulRun;


    private String laboratoryId;

    private String laboratoryName;

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

    public String getMapperText() {
        return mapperText;
    }

    public void setMapperText(String mapperText) {
        this.mapperText = mapperText;
    }

    public String getMapperFileUrl() {
        return mapperFileUrl;
    }

    public void setMapperFileUrl(String mapperFileUrl) {
        this.mapperFileUrl = mapperFileUrl;
    }

    public String getReducerText() {
        return reducerText;
    }

    public void setReducerText(String reducerText) {
        this.reducerText = reducerText;
    }

    public String getReducerFileUrl() {
        return reducerFileUrl;
    }

    public void setReducerFileUrl(String reducerFileUrl) {
        this.reducerFileUrl = reducerFileUrl;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getLastSuccessfulRun() {
        return lastSuccessfulRun;
    }

    public void setLastSuccessfulRun(Instant lastSuccessfulRun) {
        this.lastSuccessfulRun = lastSuccessfulRun;
    }

    public String getLaboratoryId() {
        return laboratoryId;
    }

    public void setLaboratoryId(String laboratoryId) {
        this.laboratoryId = laboratoryId;
    }

    public String getLaboratoryName() {
        return laboratoryName;
    }

    public void setLaboratoryName(String laboratoryName) {
        this.laboratoryName = laboratoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AlgorithmDTO algorithmDTO = (AlgorithmDTO) o;
        if (algorithmDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), algorithmDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AlgorithmDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", mapperText='" + getMapperText() + "'" +
            ", mapperFileUrl='" + getMapperFileUrl() + "'" +
            ", reducerText='" + getReducerText() + "'" +
            ", reducerFileUrl='" + getReducerFileUrl() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", lastSuccessfulRun='" + getLastSuccessfulRun() + "'" +
            ", laboratory=" + getLaboratoryId() +
            ", laboratory='" + getLaboratoryName() + "'" +
            "}";
    }
}