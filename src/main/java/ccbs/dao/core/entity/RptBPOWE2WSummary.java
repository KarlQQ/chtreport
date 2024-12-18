package ccbs.dao.core.entity;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RptBPOWE2WSummary {

  private String ranking;
  private String billOffBelong;
  private String billOff;
  private String billTel;
  private String billIdno;
  private String drDate;
  private BigDecimal totalBillAmt;

}