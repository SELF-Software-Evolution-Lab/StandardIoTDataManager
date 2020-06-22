package co.edu.uniandes.xrepo.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the Laboratory entity.
 */
public class LaboratoryDTO implements Serializable {

    private String id;

    @NotNull
    @Size(max = 30)
    private String name;

    @Size(max = 1024)
    private String description;

    private Instant dateCreated;

    @Size(max = 2048)
    private String shareUrl;

    private Instant shareValidThru;

    private List<String> tags;


    private String samplingId;

    private String samplingName;

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

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public Instant getShareValidThru() {
        return shareValidThru;
    }

    public void setShareValidThru(Instant shareValidThru) {
        this.shareValidThru = shareValidThru;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getSamplingId() {
        return samplingId;
    }

    public void setSamplingId(String samplingId) {
        this.samplingId = samplingId;
    }

    public String getSamplingName() {
        return samplingName;
    }

    public void setSamplingName(String samplingName) {
        this.samplingName = samplingName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LaboratoryDTO laboratoryDTO = (LaboratoryDTO) o;
        if (laboratoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), laboratoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LaboratoryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", shareUrl='" + getShareUrl() + "'" +
            ", shareValidThru='" + getShareValidThru() + "'" +
            ", tags='" + getTags() + "'" +
            ", sampling=" + getSamplingId() +
            ", sampling='" + getSamplingName() + "'" +
            "}";
    }
}
