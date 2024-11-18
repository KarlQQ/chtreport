package ccbs.dao.core.entity;

import java.math.BigDecimal;
import javax.annotation.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RptList {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private BigDecimal rptListId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String rptProgName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String progCodeType;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String opType;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String systemId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String rptCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String funCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String rptName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String rptFileType;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String fileNameRegular;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String rptFilePath;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String rptStartYm;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String rptEndYm;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String createDate;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String updateDate;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String createEmpid;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String rptCodeStatus;
}