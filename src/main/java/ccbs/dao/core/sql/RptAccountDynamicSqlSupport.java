package ccbs.dao.core.sql;

import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

import javax.annotation.Generated;
import java.sql.JDBCType;

public final class RptAccountDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final RptAccount rptAccount = new RptAccount();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> billOffBelong = rptAccount.billOffBelong;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> billOff = rptAccount.billOff;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> billMonth = rptAccount.billMonth;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> debtMark = rptAccount.debtMark;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> taxPrintId = rptAccount.taxPrintId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> rptCustType = rptAccount.rptCustType;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> buGroupMark = rptAccount.buGroupMark;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> accItem = rptAccount.accItem;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> rcvItem = rptAccount.rcvItem;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> billItemCode = rptAccount.billItemCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> billItemAmt = rptAccount.billItemAmt;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> billItemTaxAmt = rptAccount.billItemTaxAmt;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> billItemSaleAmt = rptAccount.billItemSaleAmt;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> billItemZeroTaxAmt = rptAccount.billItemZeroTaxAmt;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> billItemNoTaxAmt = rptAccount.billItemNoTaxAmt;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> billItemFreeTaxAmt = rptAccount.billItemFreeTaxAmt;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class RptAccount extends AliasableSqlTable<RptAccount> {
        public final SqlColumn<String> billOffBelong = column("BILL_OFF_BELONG", JDBCType.VARCHAR);

        public final SqlColumn<String> billOff = column("BILL_OFF", JDBCType.VARCHAR);

        public final SqlColumn<String> billMonth = column("BILL_MONTH", JDBCType.VARCHAR);

        public final SqlColumn<String> debtMark = column("DEBT_MARK", JDBCType.VARCHAR);

        public final SqlColumn<String> taxPrintId = column("TAX_PRINT_ID", JDBCType.VARCHAR);

        public final SqlColumn<String> rptCustType = column("RPT_CUST_TYPE", JDBCType.VARCHAR);

        public final SqlColumn<String> buGroupMark = column("BU_GROUP_MARK", JDBCType.VARCHAR);

        public final SqlColumn<String> accItem = column("ACC_ITEM", JDBCType.VARCHAR);

        public final SqlColumn<String> rcvItem = column("RCV_ITEM", JDBCType.VARCHAR);

        public final SqlColumn<String> billItemCode = column("BILL_ITEM_CODE", JDBCType.VARCHAR);

        public final SqlColumn<Long> billItemAmt = column("BILL_ITEM_AMT", JDBCType.BIGINT);

        public final SqlColumn<Long> billItemTaxAmt = column("BILL_ITEM_TAX_AMT", JDBCType.BIGINT);

        public final SqlColumn<Long> billItemSaleAmt = column("BILL_ITEM_SALE_AMT", JDBCType.BIGINT);

        public final SqlColumn<Long> billItemZeroTaxAmt = column("BILL_ITEM_ZERO_TAX_AMT", JDBCType.BIGINT);

        public final SqlColumn<Long> billItemNoTaxAmt = column("BILL_ITEM_NO_TAX_AMT", JDBCType.BIGINT);

        public final SqlColumn<Long> billItemFreeTaxAmt = column("BILL_ITEM_FREE_TAX_AMT", JDBCType.BIGINT);

        public RptAccount() {
            super("RPT_ACCOUNT", RptAccount::new);
        }
    }
}