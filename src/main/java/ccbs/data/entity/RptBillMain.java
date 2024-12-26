package ccbs.data.entity;

import ccbs.data.entity.RptBillMain.CompositeKey;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@Entity
@IdClass(CompositeKey.class)
@Table(name = "RPT_BILL_MAIN")
@Data
public class RptBillMain {
  @Column(name = "BILL_ID_MARK", length = 1) private String billIdMark;
  @Column(name = "BILL_OFF_BELONG", length = 4) private String billOffBelong;
  @Id @Column(name = "BILL_OFF", length = 4) private String billOff;
  @Id @Column(name = "BILL_TEL", length = 20) private String billTel;
  @Id @Column(name = "BILL_MONTH", length = 5) private String billMonth;
  @Id @Column(name = "BILL_ID", length = 2) private String billId;
  @Column(name = "BILL_CYCLE", length = 1) private String billCycle;
  @Column(name = "BILL_IDNO", length = 10) private String billIdno;
  @Column(name = "PAYLIMIT", length = 8) private String payLimit;
  @Column(name = "DELAY_DATE_CNT", precision = 5, scale = 0) private Integer delayDateCnt;
  @Column(name = "COMBO_ACC_NO", length = 15) private String comboAccNo;
  @Column(name = "BANK_NO", length = 17) private String bankNo;
  @Column(name = "PAYTYPE", length = 1) private String payType;
  @Column(name = "BILL_AMT", precision = 10, scale = 0) private BigDecimal billAmt;
  @Column(name = "TAX_AMT", precision = 9, scale = 0) private BigDecimal taxAmt;
  @Column(name = "SALE_AMT", precision = 10, scale = 0) private BigDecimal saleAmt;
  @Column(name = "ZERO_TAX_AMT", precision = 10, scale = 0) private BigDecimal zeroTaxAmt;
  @Column(name = "NO_TAX_AMT", precision = 10, scale = 0) private BigDecimal noTaxAmt;
  @Column(name = "FREE_TAX_AMT", precision = 10, scale = 0) private BigDecimal freeTaxAmt;
  @Column(name = "PREPAY_AMT", precision = 10, scale = 0) private BigDecimal prepayAmt;
  @Column(name = "TAX_PRINT_ID", length = 1) private String taxPrintId;
  @Column(name = "GREED", length = 1) private String greed;
  @Column(name = "CREDIT", length = 1) private String credit;
  @Column(name = "PRIVATE_MAK", length = 1) private String privateMak;
  @Column(name = "BU_GROUP_MARK", length = 1) private String buGroupMark;
  @Column(name = "RPT_CUST_TYPE", length = 1) private String rptCustType;
  @Column(name = "REMARK", length = 1) private String remark;
  @Column(name = "REMARK_TERM", length = 4) private String remarkTerm;
  @Column(name = "DEBT_MARK", length = 1) private String debtMark;
  @Column(name = "DEBT_DATE", length = 8) private String debtDate;
  @Column(name = "TEL_STATUS", length = 1) private String telStatus;
  @Column(name = "DR_DATE", length = 8) private String drDate;
  @Column(name = "SUS_CNT", length = 1) private String susCnt;
  @Column(name = "SUS_DATE1", length = 8) private String susDate1;
  @Column(name = "SUS_DATE2", length = 8) private String susDate2;
  @Column(name = "DELAY_LMT_PGM", length = 4) private String delayLmtPgm;
  @Column(name = "FALSE_MARK", length = 1) private String falseMark;
  @Column(name = "BANK_MARK", length = 1) private String bankMark;
  @Column(name = "RECEIPT_NO", length = 13) private String receiptNo;
  @Column(name = "INV_CARRIER_NO", length = 30) private String invCarrierNo;
  @Column(name = "RATE_BEGIN_DATE", length = 8) private String rateBeginDate;
  @Column(name = "RATE_END_DATE", length = 8) private String rateEndDate;
  @Column(name = "CHG_DATE", length = 8) private String chgDate;
  @Column(name = "SPLIT_MARK", length = 1) private String splitMark;
  @Column(name = "DISB_MARK", length = 1) private String disbMark;
  @Column(name = "LOSE_LAWSUIT_MARK", length = 1) private String loseLawsuitMark;
  @Column(name = "PAY_DATE", length = 8) private String payDate;
  @Column(name = "BILL_OPID", length = 1) private String billOpid;
  @Column(name = "DISB_DATE", length = 8) private String disbDate;
  @Column(name = "BILL_TELA", length = 24) private String billTela;

  @Data
  public static class CompositeKey implements Serializable {
    private String billOff;
    private String billTel;
    private String billMonth;
    private String billId;
  }
}
