package ccbs.dao.core.entity;

import java.math.BigDecimal;
import jakarta.annotation.Generated;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RptLogs {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private BigDecimal rptLogsId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String rptCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String startDate;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String startTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String endDate;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String endTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String createEmpid;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String createStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private BigDecimal createCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private BigDecimal errorCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String opType;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String opBatchno;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String paramIntValues;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String paramExtValues;

}