package ccbs.dao.core.entity;

import javax.annotation.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RptAccount {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String billOffBelong;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String billOff;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String billMonth;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String debtMark;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String taxPrintId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String rptCustType;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String buGroupMark;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String accItem;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String rcvItem;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String billItemCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long billItemAmt;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long billItemTaxAmt;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long billItemSaleAmt;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long billItemZeroTaxAmt;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long billItemNoTaxAmt;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long billItemFreeTaxAmt;
}