package co.edu.uniandes.xrepo.domain;

import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.uniandes.xrepo.domain.enumeration.TaskState;
import co.edu.uniandes.xrepo.domain.enumeration.TaskType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

/**
 * A BatchTask.
 */
@Builder
@AllArgsConstructor
@Document(collection = "batch_task")
@TypeAlias("xrepo:batch_task")
public class BatchTask implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final ObjectMapper mapper = new ObjectMapper();

    public BatchTask() {
    }

    public BatchTask(TaskType type) {
        this.type = type;
    }

    @Id
    private String id;

    @Field("description")
    private String description;

    @Field("type")
    private TaskType type;

    @Field("state")
    private TaskState state;

    @Field("create_date")
    private Instant createDate;

    @Field("start_date")
    private Instant startDate;

    @Field("end_date")
    private Instant endDate;

    @Field("progress")
    private Integer progress;

    @Field("user")
    private String user;

    @Field("parameters")
    private org.bson.Document parameters;

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

    public TaskType getType() {
        return type;
    }

    public BatchTask type(TaskType type) {
        this.type = type;
        return this;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public TaskState getState() {
        return state;
    }

    public BatchTask state(TaskState state) {
        this.state = state;
        return this;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public BatchTask createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public BatchTask startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public BatchTask endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Instant endDate) {
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

    public String getUser() {
        return user;
    }

    public BatchTask user(String user) {
        this.user = user;
        return this;
    }

    public void setUser(String user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public org.bson.Document getParameters() {
        return parameters;
    }

    public void setParameters(org.bson.Document parameters) {
        this.parameters = parameters;
    }

    public void objectToParameters(Object parameters) {
        try {
            mapper.registerModule(new JavaTimeModule());
            String json = mapper.writeValueAsString(parameters);
            setParameters(org.bson.Document.parse(json));
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public <T> T parametersToObject(Class<T> parameters) {
        try {
            return mapper.readValue(getParameters().toJson(), parameters);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
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
