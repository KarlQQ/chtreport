package ccbs.model.bp01;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Bp01f0003DTO {
  private String billOffBelong;

  private String rptCustType;

  public String billItemType;

  private BigDecimal billItemAmtSum;

  private Long count;
}