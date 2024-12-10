package ccbs.dao.core.entity;

import java.math.BigDecimal;

import jakarta.annotation.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RptAccountSummary {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String offName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String billOffBelong;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private BigDecimal nonBadDebt;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private BigDecimal badDebt;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private BigDecimal subtotal;

}