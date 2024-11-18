package ccbs.dao.core.sql;

import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

import javax.annotation.Generated;
import java.sql.JDBCType;

public final class CommOfficeDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final CommOffice commOffice = new CommOffice();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> office = commOffice.office;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> lifeStart = commOffice.lifeStart;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> lifeEnd = commOffice.lifeEnd;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> partId = commOffice.partId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> offName = commOffice.offName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> businessNo = commOffice.businessNo;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> offZip = commOffice.offZip;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> offAddress = commOffice.offAddress;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> offTelno = commOffice.offTelno;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> offFax = commOffice.offFax;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> offEmail = commOffice.offEmail;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> billTelno = commOffice.billTelno;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> boss = commOffice.boss;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> manager = commOffice.manager;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> offCity = commOffice.offCity;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> offSw = commOffice.offSw;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> offBill = commOffice.offBill;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> offProc = commOffice.offProc;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> offBelong = commOffice.offBelong;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> offAdmin = commOffice.offAdmin;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> admin = commOffice.admin;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> admProc = commOffice.admProc;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> areano = commOffice.areano;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> servTelno = commOffice.servTelno;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> servFax = commOffice.servFax;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> mailtelno = commOffice.mailtelno;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> mailfax = commOffice.mailfax;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> wordha = commOffice.wordha;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> wordhc = commOffice.wordhc;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> wordhe = commOffice.wordhe;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> wordhf = commOffice.wordhf;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> wordhi = commOffice.wordhi;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> errFax = commOffice.errFax;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class CommOffice extends AliasableSqlTable<CommOffice> {
        public final SqlColumn<String> office = column("OFFICE", JDBCType.CHAR);

        public final SqlColumn<String> lifeStart = column("LIFE_START", JDBCType.CHAR);

        public final SqlColumn<String> lifeEnd = column("LIFE_END", JDBCType.CHAR);

        public final SqlColumn<String> partId = column("PART_ID", JDBCType.CHAR);

        public final SqlColumn<String> offName = column("OFF_NAME", JDBCType.CHAR);

        public final SqlColumn<String> businessNo = column("BUSINESS_NO", JDBCType.CHAR);

        public final SqlColumn<String> offZip = column("OFF_ZIP", JDBCType.CHAR);

        public final SqlColumn<String> offAddress = column("OFF_ADDRESS", JDBCType.CHAR);

        public final SqlColumn<String> offTelno = column("OFF_TELNO", JDBCType.CHAR);

        public final SqlColumn<String> offFax = column("OFF_FAX", JDBCType.CHAR);

        public final SqlColumn<String> offEmail = column("OFF_EMAIL", JDBCType.CHAR);

        public final SqlColumn<String> billTelno = column("BILL_TELNO", JDBCType.CHAR);

        public final SqlColumn<String> boss = column("BOSS", JDBCType.CHAR);

        public final SqlColumn<String> manager = column("MANAGER", JDBCType.CHAR);

        public final SqlColumn<String> offCity = column("OFF_CITY", JDBCType.CHAR);

        public final SqlColumn<String> offSw = column("OFF_SW", JDBCType.CHAR);

        public final SqlColumn<String> offBill = column("OFF_BILL", JDBCType.CHAR);

        public final SqlColumn<String> offProc = column("OFF_PROC", JDBCType.CHAR);

        public final SqlColumn<String> offBelong = column("OFF_BELONG", JDBCType.CHAR);

        public final SqlColumn<String> offAdmin = column("OFF_ADMIN", JDBCType.CHAR);

        public final SqlColumn<String> admin = column("ADMIN", JDBCType.CHAR);

        public final SqlColumn<String> admProc = column("ADM_PROC", JDBCType.CHAR);

        public final SqlColumn<String> areano = column("AREANO", JDBCType.CHAR);

        public final SqlColumn<String> servTelno = column("SERV_TELNO", JDBCType.CHAR);

        public final SqlColumn<String> servFax = column("SERV_FAX", JDBCType.CHAR);

        public final SqlColumn<String> mailtelno = column("MAILTELNO", JDBCType.CHAR);

        public final SqlColumn<String> mailfax = column("MAILFAX", JDBCType.CHAR);

        public final SqlColumn<String> wordha = column("WORDHA", JDBCType.CHAR);

        public final SqlColumn<String> wordhc = column("WORDHC", JDBCType.CHAR);

        public final SqlColumn<String> wordhe = column("WORDHE", JDBCType.CHAR);

        public final SqlColumn<String> wordhf = column("WORDHF", JDBCType.CHAR);

        public final SqlColumn<String> wordhi = column("WORDHI", JDBCType.CHAR);

        public final SqlColumn<String> errFax = column("ERR_FAX", JDBCType.CHAR);

        public CommOffice() {
            super("COMM_OFFICE", CommOffice::new);
        }
    }
}