package ccbs.model.bp01;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Bp01f0013DTO {
  private String accItem;

  public String accName;
  
  private String billYear;

  private BigDecimal billItemAmtSum;

  private Long count;
}