package ccbs.dao.core.sql;

import java.math.BigDecimal;
import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class RptListDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final RptList rptList = new RptList();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<BigDecimal> rptListId = rptList.rptListId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> rptProgName = rptList.rptProgName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> progCodeType = rptList.progCodeType;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> opType = rptList.opType;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> systemId = rptList.systemId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> rptCode = rptList.rptCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> funCode = rptList.funCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> rptName = rptList.rptName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> rptFileType = rptList.rptFileType;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> fileNameRegular = rptList.fileNameRegular;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> rptFilePath = rptList.rptFilePath;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> rptStartYm = rptList.rptStartYm;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> rptEndYm = rptList.rptEndYm;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> createDate = rptList.createDate;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> updateDate = rptList.updateDate;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> createEmpid = rptList.createEmpid;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> rptCodeStatus = rptList.rptCodeStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class RptList extends AliasableSqlTable<RptList> {
        public final SqlColumn<BigDecimal> rptListId = column("RPT_LIST_ID", JDBCType.NUMERIC);

        public final SqlColumn<String> rptProgName = column("RPT_PROG_NAME", JDBCType.NVARCHAR);

        public final SqlColumn<String> progCodeType = column("PROG_CODE_TYPE", JDBCType.NVARCHAR);

        public final SqlColumn<String> opType = column("OP_TYPE", JDBCType.CHAR);

        public final SqlColumn<String> systemId = column("SYSTEM_ID", JDBCType.NVARCHAR);

        public final SqlColumn<String> rptCode = column("RPT_CODE", JDBCType.NVARCHAR);

        public final SqlColumn<String> funCode = column("FUN_CODE", JDBCType.NVARCHAR);

        public final SqlColumn<String> rptName = column("RPT_NAME", JDBCType.NVARCHAR);

        public final SqlColumn<String> rptFileType = column("RPT_FILE_TYPE", JDBCType.NVARCHAR);

        public final SqlColumn<String> fileNameRegular = column("FILE_NAME_REGULAR", JDBCType.NVARCHAR);

        public final SqlColumn<String> rptFilePath = column("RPT_FILE_PATH", JDBCType.NVARCHAR);

        public final SqlColumn<String> rptStartYm = column("RPT_START_YM", JDBCType.CHAR);

        public final SqlColumn<String> rptEndYm = column("RPT_END_YM", JDBCType.CHAR);

        public final SqlColumn<String> createDate = column("CREATE_DATE", JDBCType.NVARCHAR);

        public final SqlColumn<String> updateDate = column("UPDATE_DATE", JDBCType.NVARCHAR);

        public final SqlColumn<String> createEmpid = column("CREATE_EMPID", JDBCType.NVARCHAR);

        public final SqlColumn<String> rptCodeStatus = column("RPT_CODE_STATUS", JDBCType.CHAR);

        public RptList() {
            super("RPT_LIST", RptList::new);
        }
    }
}