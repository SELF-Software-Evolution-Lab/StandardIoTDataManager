package co.edu.uniandes.xrepo.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import co.edu.uniandes.xrepo.domain.enumeration.SubSetType;

/**
 * A DTO for the SubSet entity.
 */
public class SubSetDTO implements Serializable {

    private String id;

    @NotNull
    @Size(max = 30)
    private String name;

    @Size(max = 1024)
    private String description;

    @NotNull
    private List<String> fileHdfsLocation;

    private Instant dateCreated;

    private List<String> downloadUrl;

    @NotNull
    private SubSetType setType;


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

    public List<String> getFileHdfsLocation() {
        return fileHdfsLocation;
    }

    public void setFileHdfsLocation(List<String> fileHdfsLocation) {
        this.fileHdfsLocation = fileHdfsLocation;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<String> getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(List<String> downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public SubSetType getSetType() {
        return setType;
    }

    public void setSetType(SubSetType setType) {
        this.setType = setType;
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

        SubSetDTO subSetDTO = (SubSetDTO) o;
        if (subSetDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subSetDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SubSetDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", fileHdfsLocation='" + getFileHdfsLocation() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", downloadUrl='" + getDownloadUrl() + "'" +
            ", setType='" + getSetType() + "'" +
            ", laboratory=" + getLaboratoryId() +
            ", laboratory='" + getLaboratoryName() + "'" +
            "}";
    }
}
