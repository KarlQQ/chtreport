package ccbs.dao.core.entity;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RptBPGNIDSummary {

  private String billIdno;
  private BigDecimal totalAmt;

}