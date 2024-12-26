
package ccbs.template;

import ccbs.data.entity.RptBillMain;
import ccbs.data.entity.Rqbp019Type3Dto;
import ccbs.template.Rqbp019TemplaeTxt.FormData.FormDataBuilder;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class Rqbp019TemplaeTxt {
  public String toFixedLengthString(Rqbp019Type3Dto rqbp019Type3DTO) {
    return String.format(
        "%-12s%-60s%-10s%-5s%-9s%-4s%12s%-2s%-5s%-10s%-10s%-7s%-1s%-7s%-1s%-1s%-1s%-1s%-1s%-1s%s",
        val(rqbp019Type3DTO.getBpgnerpTel()), val(rqbp019Type3DTO.getBpgnerpCname()),
        val(rqbp019Type3DTO.getBpgnerpIdno()), val(rqbp019Type3DTO.getBpgnerpMonth()),
        val(rqbp019Type3DTO.getBillAmt().toPlainString()), val(rqbp019Type3DTO.getBillOff()),
        val(rqbp019Type3DTO.getBillTel()), val(rqbp019Type3DTO.getBillIdMark()),
        val(rqbp019Type3DTO.getBillMonth()), val(rqbp019Type3DTO.getBillAmt().toPlainString()),
        val(rqbp019Type3DTO.getBillIdno()), val(rqbp019Type3DTO.getPayLimit()),
        val(rqbp019Type3DTO.getTelStatus()), val(rqbp019Type3DTO.getDrDate()),
        val(rqbp019Type3DTO.getRemark()), val(rqbp019Type3DTO.getGreed()),
        val(rqbp019Type3DTO.getPayType()), val(rqbp019Type3DTO.getBillCycle()),
        val(rqbp019Type3DTO.getComboAccNoMark()), val(rqbp019Type3DTO.getDebtMark()),
        System.lineSeparator());
  }

  private String val(String val) {
    return StringUtils.hasText(val) ? val : "";
  }

  public String toFixedLengthString(RptBillMain rptBillMain) {
    return toFixedLengthString(
        FormData.builder()
            .result("Y")
            .billOff(rptBillMain.getBillOff())
            .billTel(rptBillMain.getBillTel())
            .billIdMark(rptBillMain.getBillIdMark())
            .billMonth(rptBillMain.getBillMonth())
            .billAmt(rptBillMain.getBillAmt())
            .idno(rptBillMain.getBillIdno())
            .payLimit(rptBillMain.getPayLimit())
            .telStatus(rptBillMain.getTelStatus())
            .drDate(rptBillMain.getDrDate())
            .remark(rptBillMain.getRemark())
            .greed(rptBillMain.getGreed())
            .payType(rptBillMain.getPayType())
            .billCycle(rptBillMain.getBillCycle())
            .comboAccNoMark(StringUtils.hasText(rptBillMain.getComboAccNo()) ? "Y" : "N")
            .debtMark(rptBillMain.getDebtMark())
            .billIdno(rptBillMain.getBillIdno())
            .build());
  }

  public String toFixedLengthString(FormData record) {
    return String.format(
        "%-16s%-5s%-10s%-1s%-6s%-4s%-4s%-12s%-2s%-5s%-10s%-10s%-7s%-1s%-7s%-1s%-1s%-1s%-1s%-1s%-1s%s",
        padRight(record.getTela(), 16), padRight(record.getBillym(), 5),
        padRight(record.getIdno(), 10), padRight(record.getResult(), 1),
        padRight(record.getEmpId(), 6), padRight(record.getEmpIdOff(), 4),
        padRight(record.getBillOff(), 4), padLeft(record.getBillTel(), 12),
        padRight(record.getBillIdMark(), 1), padLeft(record.getBillMonth(), 5),
        padLeft(record.getBillAmt() != null ? record.getBillAmt().toPlainString() : "", 10),
        padRight(record.getBillIdno(), 10), padRight(record.getPayLimit(), 7),
        padRight(record.getTelStatus(), 1), padRight(record.getDrDate(), 1),
        padRight(record.getRemark(), 1), padRight(record.getGreed(), 1),
        padRight(record.getPayType(), 1), padRight(record.getBillCycle(), 1),
        padRight(record.getComboAccNoMark(), 1), padRight(record.getDebtMark(), 1),
        System.lineSeparator());
  }

  private String padRight(String input, int length) {
    return String.format("%1$-" + length + "s", input != null ? input : "");
  }

  private String padLeft(String input, int length) {
    return String.format("%1$" + length + "s", input != null ? input : "");
  }

  public FormDataBuilder fromFixedLengthString(String input) {
    return FormData.builder()
        .tela(input.length() > 15 ? input.substring(0, 16).trim() : null)
        .billym(input.length() > 20 ? input.substring(16, 21) : null)
        .idno(input.length() > 30 ? input.substring(21, 31) : null)
        .result(input.length() > 31 ? input.substring(31, 32) : null)
        .empId(input.length() > 37 ? input.substring(32, 38) : null)
        .empIdOff(input.length() > 41 ? input.substring(38, 42).trim() : null)
        .billOff(input.length() > 45 ? input.substring(42, 46) : null)
        .billTel(input.length() > 57 ? input.substring(46, 58) : null)
        .billIdMark(input.length() > 59 ? input.substring(58, 60) : null)
        .billMonth(input.length() > 64 ? input.substring(60, 65) : null)
        .billAmt(input.length() > 74 && StringUtils.hasText(input.substring(65, 75))
                ? new BigDecimal(input.substring(65, 75).trim())
                : null)
        .billIdno(input.length() > 84 ? input.substring(75, 85) : null)
        .payLimit(input.length() > 91 ? input.substring(85, 92) : null)
        .telStatus(input.length() > 92 ? input.substring(92, 93) : null)
        .drDate(input.length() > 99 ? input.substring(93, 100) : null)
        .remark(input.length() > 100 ? input.substring(100, 101) : null)
        .greed(input.length() > 101 ? input.substring(101, 102) : null)
        .payType(input.length() > 102 ? input.substring(102, 103) : null)
        .billCycle(input.length() > 103 ? input.substring(103, 104) : null)
        .comboAccNoMark(input.length() > 104 ? input.substring(104, 105) : null)
        .debtMark(input.length() >= 105 ? input.substring(105, 106) : null);
  }

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
