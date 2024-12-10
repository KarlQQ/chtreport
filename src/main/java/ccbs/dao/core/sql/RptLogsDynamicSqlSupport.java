package ccbs.dao.core.sql;

import java.math.BigDecimal;
import java.sql.JDBCType;
import jakarta.annotation.Generated;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class RptLogsDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final RptLogs rptLogs = new RptLogs();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<BigDecimal> rptLogsId = rptLogs.rptLogsId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> rptCode = rptLogs.rptCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> startDate = rptLogs.startDate;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> startTime = rptLogs.startTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> endDate = rptLogs.endDate;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> endTime = rptLogs.endTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> createEmpid = rptLogs.createEmpid;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> createStatus = rptLogs.createStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<BigDecimal> createCount = rptLogs.createCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<BigDecimal> errorCount = rptLogs.errorCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> opType = rptLogs.opType;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> opBatchno = rptLogs.opBatchno;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> paramIntValues = rptLogs.paramIntValues;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> paramExtValues = rptLogs.paramExtValues;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class RptLogs extends AliasableSqlTable<RptLogs> {
        public final SqlColumn<BigDecimal> rptLogsId = column("RPT_LOGS_ID", JDBCType.NUMERIC);

        public final SqlColumn<String> rptCode = column("RPT_CODE", JDBCType.NVARCHAR);

        public final SqlColumn<String> startDate = column("START_DATE", JDBCType.NVARCHAR);

        public final SqlColumn<String> startTime = column("START_TIME", JDBCType.NVARCHAR);

        public final SqlColumn<String> endDate = column("END_DATE", JDBCType.NVARCHAR);

        public final SqlColumn<String> endTime = column("END_TIME", JDBCType.NVARCHAR);

        public final SqlColumn<String> createEmpid = column("CREATE_EMPID", JDBCType.NVARCHAR);

        public final SqlColumn<String> createStatus = column("CREATE_STATUS", JDBCType.NVARCHAR);

        public final SqlColumn<BigDecimal> createCount = column("CREATE_COUNT", JDBCType.NUMERIC);

        public final SqlColumn<BigDecimal> errorCount = column("ERROR_COUNT", JDBCType.NUMERIC);

        public final SqlColumn<String> opType = column("OP_TYPE", JDBCType.CHAR);

        public final SqlColumn<String> opBatchno = column("OP_BATCHNO", JDBCType.NUMERIC);

        public final SqlColumn<String> paramIntValues = column("PARAM_INT_VALUES", JDBCType.NVARCHAR);

        public final SqlColumn<String> paramExtValues = column("PARAM_EXT_VALUES", JDBCType.NVARCHAR);

        public RptLogs() {
            super("RPT_LOGS", RptLogs::new);
        }
    }
}