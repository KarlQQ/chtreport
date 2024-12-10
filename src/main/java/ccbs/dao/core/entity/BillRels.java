package ccbs.dao.core.entity;

import java.math.BigDecimal;
import jakarta.annotation.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BillRels {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String billOff;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String billTel;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String billMonth;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String billId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String subOff;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String subTel;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String subMonth;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String subId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String billOffBelong;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String billTela;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private BigDecimal billAmt;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String subTela;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private BigDecimal subAmt;
}