package co.edu.uniandes.xrepo.domain.metadata;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class OperativeCondition {
   private String varName;
   private String unit;
   private BigDecimal value;
}
