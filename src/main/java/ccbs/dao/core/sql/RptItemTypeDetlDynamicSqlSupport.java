package ccbs.dao.core.sql;

import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

import jakarta.annotation.Generated;
import java.sql.JDBCType;

public final class RptItemTypeDetlDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final RptItemTypeDetl rptItemTypeDetl = new RptItemTypeDetl();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> billItemCode = rptItemTypeDetl.billItemCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> billItemType = rptItemTypeDetl.billItemType;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class RptItemTypeDetl extends AliasableSqlTable<RptItemTypeDetl> {
        public final SqlColumn<String> billItemCode = column("BILL_ITEM_CODE", JDBCType.VARCHAR);

        public final SqlColumn<String> billItemType = column("BILL_ITEM_TYPE", JDBCType.VARCHAR);

        public RptItemTypeDetl() {
            super("RPT_ITEM_TYPE_DETL", RptItemTypeDetl::new);
        }
    }
}