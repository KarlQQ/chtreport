package ccbs.dao.core.entity;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RptBPZ10Summary {

  private String billItemName;
  private String billOffBelong;
  private String billOff;
  private String billTel;
  private String billSubOff;
  private String billSubTel;
  private String billMonth;
  private BigDecimal billAmt;
  private String billItemCode;
  private BigDecimal billItemAmt;

}