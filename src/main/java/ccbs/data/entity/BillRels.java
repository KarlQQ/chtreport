package ccbs.data.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.Data;

@IdClass(ccbs.data.entity.BillRels.CompositeKey.class)
@Entity
@Table(name = "BILL_RELS", schema = "BPUSER")
@Data
public class BillRels {
  @Column(name = "BILL_OFF_BELONG", length = 4) private String billOffBelong;

  @Id @Column(name = "BILL_TELA", length = 24) private String billTela;

  @Column(name = "BILL_OFF", length = 4) private String billOff;

  @Column(name = "BILL_TEL", length = 20) private String billTel;

  @Id @Column(name = "BILL_MONTH", length = 5) private String billMonth;

  @Column(name = "BILL_ID", length = 2) private String billId;

  @Column(name = "BILL_AMT", precision = 10, scale = 0) private BigDecimal billAmt;

  @Id @Column(name = "SUB_TELA", length = 24) private String subTela;

  @Column(name = "SUB_OFF", length = 4) private String subOff;

  @Column(name = "SUB_TEL", length = 20) private String subTel;

  @Column(name = "SUB_MONTH", length = 5) private String subMonth;

  @Column(name = "SUB_ID", length = 2) private String subId;

  @Column(name = "SUB_AMT", precision = 10, scale = 0) private BigDecimal subAmt;

  @Data
  public static class CompositeKey implements Serializable {
    private String billTela;
    private String billMonth;
    private String subTela;
  }
}
