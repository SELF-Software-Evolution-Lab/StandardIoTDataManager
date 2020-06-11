package co.edu.uniandes.xrepo.domain.metadata;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class OperativeRange implements Serializable {

    private String varName;
    private String unit;
    private BigDecimal minVal;
    private BigDecimal maxVal;

    public String getVarName() {
        return varName;
    }

    public String getVarNameUpCased(){
        return varName.substring(0,1).toUpperCase() + varName.substring(1);
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getMinVal() {
        return minVal;
    }

    public void setMinVal(BigDecimal minVal) {
        this.minVal = minVal;
    }

    public BigDecimal getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(BigDecimal maxVal) {
        this.maxVal = maxVal;
    }

}
