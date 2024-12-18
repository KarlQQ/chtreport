package ccbs.dao.core.entity;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RptBPOWESummary {

  private String ranking;
  private String billOffBelong;
  private String billOff;
  private String billTel;
  private String billIdno;
  private String billMonth;
  private String billId;
  private String drDate;
  private BigDecimal totalBillAmt;

}