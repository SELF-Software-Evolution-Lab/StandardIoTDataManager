package co.edu.uniandes.xrepo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;

import co.edu.uniandes.xrepo.domain.enumeration.TypeTask;

import co.edu.uniandes.xrepo.domain.enumeration.StateTask;

/**
 * A BatchTask.
 */
@Document(collection = "batch_task")
public class BatchTask implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public BatchTask() {
    }
    public BatchTask(TypeTask type) {
        this.type = type;
    }
    @Id
    private String id;

    @Field("description")
    private String description;

    @Field("type")
    private TypeTask type;

    @Field("state")
    private StateTask state;

    @Field("create_date")
    private ZonedDateTime createDate;

    @Field("start_date")
    private ZonedDateTime startDate;

    @Field("end_date")
    private ZonedDateTime endDate;

    @Field("progress")
    private Integer progress;

    @DBRef
    @Field("user")
    @JsonIgnoreProperties("batchTasks")
    private User user;

    /**
     * K: Parameter Name
     * V: Parameter Value
     * {"param1" -> "value1",
     * "param2" -> "value2",
     * "param1" -> "value3",}
     */
    @Field("parameters")
    private Map<String, String> parameters;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public BatchTask description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeTask getType() {
        return type;
    }

    public BatchTask type(TypeTask type) {
        this.type = type;
        return this;
    }

    public void setType(TypeTask type) {
        this.type = type;
    }

    public StateTask getState() {
        return state;
    }

    public BatchTask state(StateTask state) {
        this.state = state;
        return this;
    }

    public void setState(StateTask state) {
        this.state = state;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public BatchTask createDate(ZonedDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }
    
    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public BatchTask startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public BatchTask endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getProgress() {
        return progress;
    }

    public BatchTask progress(Integer progress) {
        this.progress = progress;
        return this;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public User getUser() {
        return user;
    }

    public BatchTask user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BatchTask batchTask = (BatchTask) o;
        if (batchTask.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), batchTask.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BatchTask{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", state='" + getState() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", progress=" + getProgress() +
            "}";
    }
}
