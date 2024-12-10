package ccbs.dao.core.sql;

import java.math.BigDecimal;
import java.sql.JDBCType;
import jakarta.annotation.Generated;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class BillRelsDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final BillRels billRels = new BillRels();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> billOff = billRels.billOff;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> billTel = billRels.billTel;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> billMonth = billRels.billMonth;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> billId = billRels.billId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> subOff = billRels.subOff;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> subTel = billRels.subTel;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> subMonth = billRels.subMonth;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> subId = billRels.subId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> billOffBelong = billRels.billOffBelong;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> billTela = billRels.billTela;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<BigDecimal> billAmt = billRels.billAmt;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> subTela = billRels.subTela;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<BigDecimal> subAmt = billRels.subAmt;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class BillRels extends AliasableSqlTable<BillRels> {
        public final SqlColumn<String> billOff = column("BILL_OFF", JDBCType.CHAR);

        public final SqlColumn<String> billTel = column("BILL_TEL", JDBCType.NVARCHAR);

        public final SqlColumn<String> billMonth = column("BILL_MONTH", JDBCType.CHAR);

        public final SqlColumn<String> billId = column("BILL_ID", JDBCType.CHAR);

        public final SqlColumn<String> subOff = column("SUB_OFF", JDBCType.CHAR);

        public final SqlColumn<String> subTel = column("SUB_TEL", JDBCType.NVARCHAR);

        public final SqlColumn<String> subMonth = column("SUB_MONTH", JDBCType.CHAR);

        public final SqlColumn<String> subId = column("SUB_ID", JDBCType.CHAR);

        public final SqlColumn<String> billOffBelong = column("BILL_OFF_BELONG", JDBCType.CHAR);

        public final SqlColumn<String> billTela = column("BILL_TELA", JDBCType.NVARCHAR);

        public final SqlColumn<BigDecimal> billAmt = column("BILL_AMT", JDBCType.NUMERIC);

        public final SqlColumn<String> subTela = column("SUB_TELA", JDBCType.NVARCHAR);

        public final SqlColumn<BigDecimal> subAmt = column("SUB_AMT", JDBCType.NUMERIC);

        public BillRels() {
            super("BILL_RELS", BillRels::new);
        }
    }
}