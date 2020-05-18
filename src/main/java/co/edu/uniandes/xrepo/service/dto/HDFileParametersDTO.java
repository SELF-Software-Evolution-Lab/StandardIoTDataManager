package co.edu.uniandes.xrepo.service.dto;


import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import co.edu.uniandes.xrepo.domain.Device;
import co.edu.uniandes.xrepo.domain.Sensor;
import co.edu.uniandes.xrepo.domain.metadata.OperativeCondition;


public class HDFileParametersDTO {
    private String samplingId;
    private String fileUri;

    public String getSamplingId() {
        return samplingId;
    }

    public void setSamplingId(String samplingId) {
        this.samplingId = samplingId;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    @Override
    public String toString() {
        return "HDFileParametersDTO{" +
            "samplingId=" + getSamplingId() +
            ", fileUri='" + getFileUri() + "'" +
            "}";
    }
}
