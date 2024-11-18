package ccbs.dao.core.sql;

import java.math.BigDecimal;
import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class RptLogsDetlsDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final RptLogsDetls rptLogsDetls = new RptLogsDetls();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> rptFileName = rptLogsDetls.rptFileName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<BigDecimal> rptLogsId = rptLogsDetls.rptLogsId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> billOff = rptLogsDetls.billOff;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> rptTimes = rptLogsDetls.rptTimes;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> billMonth = rptLogsDetls.billMonth;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> billCycle = rptLogsDetls.billCycle;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> rptDate = rptLogsDetls.rptDate;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> rptQuarter = rptLogsDetls.rptQuarter;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> offAdmin = rptLogsDetls.offAdmin;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> rptFilePath = rptLogsDetls.rptFilePath;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<BigDecimal> rptFileCount = rptLogsDetls.rptFileCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<BigDecimal> rptFileAmt = rptLogsDetls.rptFileAmt;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> rptSecretMark = rptLogsDetls.rptSecretMark;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> rptLogsStatus = rptLogsDetls.rptLogsStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class RptLogsDetls extends AliasableSqlTable<RptLogsDetls> {
        public final SqlColumn<String> rptFileName = column("RPT_FILE_NAME", JDBCType.NVARCHAR);

        public final SqlColumn<BigDecimal> rptLogsId = column("RPT_LOGS_ID", JDBCType.NUMERIC);

        public final SqlColumn<String> billOff = column("BILL_OFF", JDBCType.CHAR);

        public final SqlColumn<String> rptTimes = column("RPT_TIMES", JDBCType.CHAR);

        public final SqlColumn<String> billMonth = column("BILL_MONTH", JDBCType.CHAR);

        public final SqlColumn<String> billCycle = column("BILL_CYCLE", JDBCType.CHAR);

        public final SqlColumn<String> rptDate = column("RPT_DATE", JDBCType.NVARCHAR);

        public final SqlColumn<String> rptQuarter = column("RPT_QUARTER", JDBCType.CHAR);

        public final SqlColumn<String> offAdmin = column("OFF_ADMIN", JDBCType.CHAR);

        public final SqlColumn<String> rptFilePath = column("RPT_FILE_PATH", JDBCType.NVARCHAR);

        public final SqlColumn<BigDecimal> rptFileCount = column("RPT_FILE_COUNT", JDBCType.NUMERIC);

        public final SqlColumn<BigDecimal> rptFileAmt = column("RPT_FILE_AMT", JDBCType.NUMERIC);

        public final SqlColumn<String> rptSecretMark = column("RPT_SECRET_MARK", JDBCType.CHAR);

        public final SqlColumn<String> rptLogsStatus = column("RPT_LOGS_STATUS", JDBCType.CHAR);

        public RptLogsDetls() {
            super("RPT_LOGS_DETLS", RptLogsDetls::new);
        }
    }
}