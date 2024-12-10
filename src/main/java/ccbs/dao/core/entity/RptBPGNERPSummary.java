package ccbs.dao.core.entity;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RptBPGNERPSummary {

  private String billOff;
  private String billTel;
  private String billMonth;
  private String billIdno;
  private BigDecimal billAmt;

}