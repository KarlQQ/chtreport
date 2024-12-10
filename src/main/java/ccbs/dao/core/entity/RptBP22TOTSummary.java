package ccbs.dao.core.entity;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RptBP22TOTSummary {

  private String billMonth;
  private String billOffBelong;
  private String accItem1;
  private String accItem2;
  private BigDecimal sumBillItemAmt;
  

}