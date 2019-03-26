package co.edu.uniandes.xrepo.service.dto;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A Sensor.
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SensorDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @NotNull
    private String internalId;

    @NotNull
    private String sensorType;

    private BigDecimal potentialFreq;

    private BigDecimal samplingFreq;

    private String deviceId;

    private String deviceName;

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public BigDecimal getPotentialFreq() {
        return potentialFreq;
    }

    public void setPotentialFreq(BigDecimal potentialFreq) {
        this.potentialFreq = potentialFreq;
    }

    public BigDecimal getSamplingFreq() {
        return samplingFreq;
    }

    public void setSamplingFreq(BigDecimal samplingFreq) {
        this.samplingFreq = samplingFreq;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensorDTO sensorDTO = (SensorDTO) o;
        return internalId.equals(sensorDTO.internalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(internalId);
    }
}
