package ccbs.dao.core.entity;

import java.math.BigDecimal;

import jakarta.annotation.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RptBP222OTSummary {
  @Generated("org.mybatis.generator.api.MyBatisGenerator")
  private String billOffBelong;

  @Generated("org.mybatis.generator.api.MyBatisGenerator")
  private String buGroupMark;

  @Generated("org.mybatis.generator.api.MyBatisGenerator")
  private String accItem;

  @Generated("org.mybatis.generator.api.MyBatisGenerator")
  private String billMonth;

  @Generated("org.mybatis.generator.api.MyBatisGenerator")
  private BigDecimal sumBillItemAmt;

}