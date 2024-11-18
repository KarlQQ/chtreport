package ccbs.data.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "BILL_DEVICE", schema = "BPUSER")
@Data
public class BillDevice {
  @Column(name = "BILL_OFF_BELONG", length = 4) private String billOffBelong;

  @Id @Column(name = "BILL_TELA", length = 24) private String billTela;

  @Column(name = "BILL_OFF", length = 4) private String billOff;

  @Column(name = "BILL_TEL", length = 20) private String billTel;

  @Column(name = "BILL_IDNO", length = 10) private String billIdNo;

  @Column(name = "CUST_IDNO", length = 10) private String custIdNo;

  @Column(name = "BILL_OPID", length = 1) private String billOpId;

  @Column(name = "TEL_STATUS", length = 1) private String telStatus;

  @Column(name = "REMIND_CNT", precision = 2, scale = 0) private BigDecimal remindCnt;

  @Column(name = "SUS_CNT", length = 1) private String susCnt;

  @Column(name = "SUS_DATE", length = 8) private String susDate;

  @Column(name = "SUS_DATE1", length = 8) private String susDate1;

  @Column(name = "SUS_DATE2", length = 8) private String susDate2;

  @Column(name = "DR_DATE", length = 8) private String drDate;

  @Column(name = "DISB_MARK", length = 1) private String disbMark;

  @Column(name = "DISB_DATE", length = 8) private String disbDate;

  @Column(name = "DISB_LETTER_MARK", length = 1) private String disbLetterMark;

  @Column(name = "DISB_LETTER_DATE", length = 8) private String disbLetterDate;

  @Column(name = "DIST_DATE", length = 8) private String distDate;

  @Column(name = "DIST_LETTER_MARK", length = 1) private String distLetterMark;

  @Column(name = "DIST_LETTER_DATE", length = 8) private String distLetterDate;

  @Column(name = "FALSE_MARK", length = 1) private String falseMark;

  @Column(name = "FALSE_DATE", length = 8) private String falseDate;

  @Column(name = "UPDATE_DATE", length = 8) private String updateDate;

  @Column(name = "UPDATE_PGMID", length = 20) private String updatePgmId;
}
