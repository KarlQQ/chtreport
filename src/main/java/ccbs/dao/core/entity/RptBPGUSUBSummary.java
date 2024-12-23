package ccbs.dao.core.entity;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RptBPGUSUBSummary {

  private String billIdno;
  private String billMonth;
  private String billId;
  private String billCycle;
  private String billOff;
  private String billTel;
  private BigDecimal totalAmt;
  private String dueId;

}