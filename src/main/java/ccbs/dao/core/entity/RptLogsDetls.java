package ccbs.dao.core.entity;

import java.math.BigDecimal;
import javax.annotation.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RptLogsDetls {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String rptFileName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private BigDecimal rptLogsId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String billOff;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String rptTimes;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String billMonth;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String billCycle;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String rptDate;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String rptQuarter;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String offAdmin;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String rptFilePath;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private BigDecimal rptFileCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private BigDecimal rptFileAmt;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String rptSecretMark;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String rptLogsStatus;
    public boolean isRptFileAmtNull() {
        return rptFileAmt == null;
    }
}