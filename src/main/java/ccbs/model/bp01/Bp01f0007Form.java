
package ccbs.model.bp01;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Bp01f0007Form {
  @Builder.Default private List<FormData> fromDataList = new ArrayList<>();

  @Data
  @Builder
  @AllArgsConstructor
  public static class FormData {
    private String tela;
    private String billym;
    private String idno;
    private String result;
    private String empId;
    private String empIdOff;
    private String billOff;
    private String billTel;
    private String billIdMark;
    private String billMonth;
    private BigDecimal billAmt;
    private String billIdno;
    private String payLimit;
    private String telStatus;
    private String drDate;
    private String remark;
    private String greed;
    private String payType;
    private String billCycle;
    private String comboAccNoMark;
    private String debtMark;
  }
}
