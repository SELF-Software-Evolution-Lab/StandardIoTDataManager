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
@Getter
@Setter
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
