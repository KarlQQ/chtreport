package ccbs.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityResult;
import jakarta.persistence.FieldResult;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SqlResultSetMapping;
import java.math.BigDecimal;
import lombok.Data;
import org.springframework.util.StringUtils;

@Entity
@NamedNativeQuery(name = "FindbyBillOpidWithCname",
    query =
        "SELECT r.BILL_TEL as BPGNERP_TEL, b.CUST_NAME as BPGNERP_CNAME, r.BILL_IDNO as BPGNERP_IDNO, r.BILL_MONTH as BPGNERP_MONTH, r.BILL_AMT as BPGNERP_AMT, r.* "
        + "FROM RPT_BILL_MAIN r "
        + "JOIN BILL_DEVICE_REG b ON r.BILL_IDNO = b.IDNO "
        + "WHERE r.BILL_OPID = :billOpid",
    resultSetMapping = "Rqbp019Type3DtoMapping")
@NamedNativeQuery(name = "FindbyBillOpidAndRegexpLikeBillTelaWithCname",
    query =
        "SELECT r.BILL_TEL as BPGNERP_TEL, b.CUST_NAME as BPGNERP_CNAME, r.BILL_IDNO as BPGNERP_IDNO, r.BILL_MONTH as BPGNERP_MONTH, r.BILL_AMT as BPGNERP_AMT, r.* "
        + "FROM RPT_BILL_MAIN r "
        + "JOIN BILL_DEVICE_REG b ON r.BILL_IDNO = b.IDNO "
        + "WHERE r.BILL_OPID = :billOpid "
        + "AND REGEXP_LIKE(r.BILL_TELA, :pattern)",
    resultSetMapping = "Rqbp019Type3DtoMapping")
@SqlResultSetMapping(name = "Rqbp019Type3DtoMapping",
    entities =
    {
      @EntityResult(entityClass = Rqbp019Type3Dto.class, fields = {
        @FieldResult(column = "BPGNERP_TEL", name = "bpgnerpTel")
        , @FieldResult(column = "BPGNERP_CNAME", name = "bpgnerpCname"),
            @FieldResult(column = "BPGNERP_IDNO", name = "bpgnerpIdno"),
            @FieldResult(column = "BPGNERP_MONTH", name = "bpgnerpMonth"),
            @FieldResult(column = "BPGNERP_AMT", name = "bpgnerpAmt"),
            @FieldResult(column = "BILL_OFF", name = "billOff"),
            @FieldResult(column = "BILL_TEL", name = "billTel"),
            @FieldResult(column = "BILL_ID_MARK", name = "billIdMark"),
            @FieldResult(column = "BILL_MONTH", name = "billMonth"),
            @FieldResult(column = "BILL_AMT", name = "billAmt"),
            @FieldResult(column = "BILL_IDNO", name = "billIdno"),
            @FieldResult(column = "PAYLIMIT", name = "payLimit"),
            @FieldResult(column = "TEL_STATUS", name = "telStatus"),
            @FieldResult(column = "DR_DATE", name = "drDate"),
            @FieldResult(column = "REMARK", name = "remark"),
            @FieldResult(column = "GREED", name = "greed"),
            @FieldResult(column = "PAYTYPE", name = "payType"),
            @FieldResult(column = "BILL_CYCLE", name = "billCycle"),
            @FieldResult(column = "COMBO_ACC_NO", name = "comboAccNo"),
            @FieldResult(column = "DEBT_MARK", name = "debtMark")
      })
    })
@Data
public class Rqbp019Type3Dto {
  @Id private String bpgnerpTel; // BPGNERP_TEL
  @Id private String bpgnerpCname; // BPGNERP_CNAME
  @Id private String bpgnerpIdno; // BPGNERP_IDNO
  @Id private String bpgnerpMonth; // BPGNERP_MONTH
  private BigDecimal bpgnerpAmt = BigDecimal.ZERO; // BPGNERP_AMT
  private String billOff; // BILL_OFF
  private String billTel; // BILL_TEL
  private String billIdMark; // BILL_ID_MARK
  private String billMonth; // BILL_MONTH
  private BigDecimal billAmt = BigDecimal.ZERO; // BILL_AMT
  private String billIdno; // BILL_IDNO
  private String payLimit; // PAYLIMIT
  private String telStatus; // TEL_STATUS
  private String drDate; // DR_DATE
  private String remark; // REMARK
  private String greed; // GREED
  private String payType; // PAYTYPE
  private String billCycle; // BILL_CYCLE
  private String comboAccNo; // COMBO_ACC_NO_MARK
  private String debtMark; // DEBT_MARK

  public String getComboAccNoMark() {
    return StringUtils.hasText(this.comboAccNo) ? "Y" : "N";
  }
}
