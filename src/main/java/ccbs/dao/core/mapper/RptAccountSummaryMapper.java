package ccbs.dao.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import java.util.List;
import ccbs.dao.core.entity.RptAccountSummary;
import ccbs.dao.core.entity.RptBP2230D4Summary;
import ccbs.dao.core.entity.RptBP2230D5Summary;
import ccbs.dao.core.entity.RptBP2240D1Summary;
import ccbs.dao.core.constants.DateTypeConstants;

@Mapper
public interface RptAccountSummaryMapper {

  @SelectProvider(type = SqlProvider.class, method = "selectRptAccountSummary")
  List<RptAccountSummary> selectRptAccountSummary(@Param("currentDate") String currentDate,
      @Param("offType") String offType, @Param("dateType") String dateType);

  @SelectProvider(type = SqlProvider.class, method = "selectBP2240D1Summary")
  List<RptBP2240D1Summary> selectBP2240D1Summary(@Param("currentDate") String currentDate);

  @SelectProvider(type = SqlProvider.class, method = "selectBP2230D4Summary")
  List<RptBP2230D4Summary> selectBP2230D4Summary(@Param("currentDate") String currentDate);

  @SelectProvider(type = SqlProvider.class, method = "selectBP2230D5Summary")
  List<RptBP2230D5Summary> selectBP2230D5Summary(@Param("currentDate") String currentDate);

  class SqlProvider {
    public String selectRptAccountSummary(@Param("currentDate") String currentDate, @Param("offType") String offType,
        @Param("dateType") String dateType) {
      StringBuilder sql = new StringBuilder();
      // 選擇所需的欄位
      sql.append("SELECT TRIM(a.bill_off_belong) AS billOffBelong, ");
      sql.append(
          "COALESCE(SUM(CASE WHEN a.debt_mark NOT IN ('Q', 'Y') THEN a.bill_item_amt ELSE 0 END), 0) AS nonBadDebt, ");
      sql.append("COALESCE(SUM(CASE WHEN a.debt_mark IN ('Q', 'Y') THEN a.bill_item_amt ELSE 0 END), 0) AS badDebt, ");
      sql.append("COALESCE(SUM(a.bill_item_amt), 0) AS subtotal ");
      sql.append("FROM (select * from rpt_account a0 ");

      // 如果 offType 是 "B"，則查詢行動業務營運處
      if (offType != null && !offType.isEmpty() && offType.equals("B")) {
        sql.append(
            "JOIN (SELECT DISTINCT bill_item_code FROM rpt_item_type_detl WHERE bill_item_type = 'G3') detl ON a0.bill_item_code = detl.bill_item_code ");
      }

      if (offType != null && !offType.isEmpty() && offType.equals("D")) {
        if (dateType.equals(DateTypeConstants.ACU_X_2M)) {
          // 統計執行日前2個月的月份
          sql.append("WHERE TO_CHAR(TO_DATE(TO_NUMBER(SUBSTR(a0.bill_month, 1, 3)) + 1911 || SUBSTR(a0.bill_month, 4, 2), 'YYYYMM'), 'YYYYMM') = TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), -2), 'YYYYMM') ");
        }
      } else if (dateType != null && !dateType.isEmpty()) {
        if (dateType.equals(DateTypeConstants.ACU_X_2M)) {
          // 統計至執行日的前2個月
          sql.append("WHERE TO_CHAR(TO_DATE(TO_NUMBER(SUBSTR(a0.bill_month, 1, 3)) + 1911 || SUBSTR(a0.bill_month, 4, 2), 'YYYYMM'), 'YYYYMM') <= TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), -2), 'YYYYMM') ");
        } else if (dateType.equals(DateTypeConstants.ACU_X_1M)) {
          // 統計至執行日的前1個月
          sql.append("WHERE TO_CHAR(TO_DATE(TO_NUMBER(SUBSTR(a0.bill_month, 1, 3)) + 1911 || SUBSTR(a0.bill_month, 4, 2), 'YYYYMM'), 'YYYYMM') <= TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), -1), 'YYYYMM') ");
        } else if (dateType.equals(DateTypeConstants.SUM_X_1Y)) {
          // 單獨統計執行日的前1年該年度
          sql.append(
              "WHERE TO_CHAR(TO_DATE(TO_NUMBER(SUBSTR(a0.bill_month, 1, 3)) + 1911 || SUBSTR(a0.bill_month, 4, 2), 'YYYYMM'), 'YYYYMM') LIKE TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), -12), 'YYYY') || '%' ");
        } else if (dateType.equals(DateTypeConstants.ACU_X_2Y)) {
          // 統計至執行日的前2年
          sql.append(
              "WHERE TO_CHAR(TO_DATE(TO_NUMBER(SUBSTR(a0.bill_month, 1, 3)) + 1911 || SUBSTR(a0.bill_month, 4, 2), 'YYYYMM'), 'YYYYMM') <= TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), -24), 'YYYY') || '12' ");
        } else if (dateType.equals(DateTypeConstants.SUM_X_1Y_11)) {
          // 單獨統計執行日的前1年度的十一月
          sql.append(
              "WHERE TO_CHAR(TO_DATE(TO_NUMBER(SUBSTR(a0.bill_month, 1, 3)) + 1911 || SUBSTR(a0.bill_month, 4, 2), 'YYYYMM'), 'YYYYMM') = TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), -12), 'YYYY') || '11' ");
        } else if (dateType.equals(DateTypeConstants.SUM_X_1Y_12)) {
          // 單獨統計執行日的前1年度的十二月
          sql.append(
              "WHERE TO_CHAR(TO_DATE(TO_NUMBER(SUBSTR(a0.bill_month, 1, 3)) + 1911 || SUBSTR(a0.bill_month, 4, 2), 'YYYYMM'), 'YYYYMM') = TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), -12), 'YYYY') || '12' ");
        }
      }

      // 如果 offType 是 "C"，則排除特定代收業務
      if (offType != null && !offType.isEmpty() && offType.equals("C")) {
        sql.append("AND a0.rcv_item not in ('5609-9299A','5609-9299B') ");
      }

      sql.append(") a ");

      // 分組和排序
      sql.append("GROUP BY a.bill_off_belong ");
      sql.append("ORDER BY a.bill_off_belong");

      return sql.toString();
    }

    public String selectBP2240D1Summary(@Param("currentDate") String currentDate) {
      StringBuilder sql = new StringBuilder();
      sql.append("SELECT ");
      sql.append("a.bu_group_mark AS buGroupMark, ");
      sql.append("b.bill_off_belong AS billOffBelong, ");
      sql.append("NVL(SUM(CASE WHEN b.acc_item = '2825-2402' THEN b.bill_item_amt ELSE 0 END), 0) AS aCol, ");
      sql.append("NVL(SUM(CASE WHEN b.acc_item = '2825-2403' THEN b.bill_item_amt ELSE 0 END), 0) AS bCol, ");
      sql.append("NVL(SUM(CASE WHEN b.acc_item = '2825-2404' THEN b.bill_item_amt ELSE 0 END), 0) AS cCol, ");
      sql.append("NVL(SUM(CASE WHEN b.acc_item = '2825-2405' THEN b.bill_item_amt ELSE 0 END), 0) AS dCol, ");
      sql.append("NVL(SUM(CASE WHEN b.acc_item = '2825-2406' THEN b.bill_item_amt ELSE 0 END), 0) AS eCol, ");
      sql.append("NVL(SUM(CASE WHEN b.acc_item = '1816-2401' THEN b.bill_item_amt ELSE 0 END), 0) AS fCol, ");
      sql.append("NVL(SUM(CASE WHEN b.acc_item = '1816-2402A' THEN b.bill_item_amt ELSE 0 END), 0) AS gCol, ");
      sql.append("NVL(SUM(CASE WHEN b.acc_item = '2253-02EA' THEN b.bill_item_amt ELSE 0 END), 0) AS hCol, ");
      sql.append("NVL(SUM(CASE WHEN b.acc_item = '2253-02EC' THEN b.bill_item_amt ELSE 0 END), 0) AS iCol, ");
      sql.append("NVL(SUM(CASE WHEN b.rcv_item = '1178-29' THEN b.bill_item_amt ELSE 0 END), 0) AS jCol ");
      sql.append(
          "FROM (SELECT DISTINCT BU_GROUP_MARK FROM rpt_account) a LEFT JOIN rpt_account b ON a.bu_group_mark = b.bu_group_mark ");
      sql.append(
          "AND (b.acc_item IN ('2825-2402', '2825-2403', '2825-2404', '2825-2405', '2825-2406', '1816-2401', '1816-2402A', '2253-02EA', '2253-02EC') or b.rcv_item IN ('1178-29')) ");
      sql.append("AND b.bu_group_mark IN ('A', 'B', 'C') ");
      sql.append(
          " AND TO_NUMBER(SUBSTR(b.bill_month, 1, 3)) = TO_NUMBER(TO_CHAR(TO_DATE(#{currentDate}, 'YYYYMM'), 'YYYY')) - 1911 \n");
      sql.append(
          " AND TO_NUMBER(SUBSTR(b.bill_month, 4, 2)) = TO_NUMBER(TO_CHAR(TO_DATE(#{currentDate}, 'YYYYMM'), 'MM')) ");
      sql.append("GROUP BY ");
      sql.append("a.bu_group_mark, b.bill_off_belong ");
      sql.append("ORDER BY ");
      sql.append("a.bu_group_mark, b.bill_off_belong ");

      return sql.toString();
    }

    private String generateYearCTE(String groupBy, int startYear, int endYear) {
      StringBuilder sql = new StringBuilder();
      for (int year = startYear; year <= endYear; year++) {
        sql.append("year_").append(year).append(" AS ( \n");
        sql.append("    SELECT "+groupBy+", \n");
        sql.append(
            "           COALESCE(SUM(CASE WHEN ra.debt_mark IN ('Q', 'Y') THEN ra.bill_item_amt ELSE 0 END), 0) AS year")
            .append(year).append("BadDebt, \n");
        sql.append(
            "           COALESCE(SUM(CASE WHEN ra.debt_mark NOT IN ('Q', 'Y') THEN ra.bill_item_amt ELSE 0 END), 0) AS year")
            .append(year).append("NonBadDebt \n");
        sql.append("    FROM RPT_ACCOUNT ra \n");
        sql.append(
            "    WHERE TO_NUMBER(SUBSTR(ra.bill_month, 1, 3)) = TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), -")
            .append(year * 12).append("), 'YYYY')) - 1911 \n");
        sql.append("    GROUP BY "+groupBy+" \n");
        sql.append("), \n");
      }
      // 移除最後一個 CTE 的逗號
      sql.setLength(sql.length() - 3);
      sql.append("\n");
      return sql.toString();
    }

    private String generateMonthCTE(String groupBy, String cteName, String dateOffset, String debtCondition) {
      StringBuilder sql = new StringBuilder();
      sql.append(cteName).append(" AS ( \n");
      sql.append("    SELECT "+groupBy+", \n");
      sql.append(
          "           COALESCE(SUM(CASE WHEN ra.debt_mark IN ('Q', 'Y') THEN ra.bill_item_amt ELSE 0 END), 0) AS ")
          .append(cteName).append("BadDebt, \n");
      sql.append(
          "           COALESCE(SUM(CASE WHEN ra.debt_mark NOT IN ('Q', 'Y') THEN ra.bill_item_amt ELSE 0 END), 0) AS ")
          .append(cteName).append("NonBadDebt \n");
      sql.append("    FROM RPT_ACCOUNT ra \n");
      String conditionStr = "="; 
      if (cteName == "futureMonths") {
        conditionStr = ">=";
      }
      sql.append(
          "    WHERE TO_NUMBER(SUBSTR(ra.bill_month, 1, 3)) "+conditionStr+" TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), ")
          .append(dateOffset).append("), 'YYYY')) - 1911 \n");
      sql.append(
          "          AND TO_NUMBER(SUBSTR(ra.bill_month, 4, 2)) "+conditionStr+" TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), ")
          .append(dateOffset).append("), 'MM')) \n");
      sql.append("    GROUP BY "+groupBy+" \n");
      sql.append("), \n");
      return sql.toString();
    }

    private String generateSelectBody() {
      StringBuilder sql = new StringBuilder();
      sql.append("    COALESCE(cm.currentMonthBadDebt, 0) AS currentMonthBadDebt, \n");
      sql.append("    COALESCE(cm.currentMonthNonBadDebt, 0) AS currentMonthNonBadDebt, \n");
      sql.append("    COALESCE(fm.futureMonthsBadDebt, 0) AS futureMonthsBadDebt, \n");
      sql.append("    COALESCE(fm.futureMonthsNonBadDebt, 0) AS futureMonthsNonBadDebt, \n");

      for (int year = 1; year <= 11; year++) {
        sql.append("    COALESCE(y").append(year).append(".year").append(year).append("BadDebt, 0) AS year")
            .append(year).append("BadDebt, \n");
        sql.append("    COALESCE(y").append(year).append(".year").append(year).append("NonBadDebt, 0) AS year")
            .append(year).append("NonBadDebt, \n");
      }

      StringBuilder totalBadDebt = new StringBuilder("COALESCE(cm.currentMonthBadDebt, 0) + COALESCE(fm.futureMonthsBadDebt, 0)");
      StringBuilder totalNonBadDebt = new StringBuilder("COALESCE(cm.currentMonthNonBadDebt, 0) + COALESCE(fm.futureMonthsNonBadDebt, 0)");

      for (int year = 1; year <= 11; year++) {
        totalBadDebt.append(" + COALESCE(y").append(year).append(".year").append(year).append("BadDebt, 0)");
        totalNonBadDebt.append(" + COALESCE(y").append(year).append(".year").append(year).append("NonBadDebt, 0)");
      }

      sql.append(totalBadDebt).append(" AS totalBadDebt, \n");
      sql.append(totalNonBadDebt).append(" AS totalNonBadDebt \n");
      return sql.toString();
    }

    public String selectBP2230D4Summary(@Param("currentDate") String currentDate) {
      StringBuilder sql = new StringBuilder();

      sql.append("WITH \n");
      sql.append(generateMonthCTE("ACC_ITEM", "currentMonth", "0", "currentMonth"));
      sql.append(generateMonthCTE("ACC_ITEM","futureMonths", "1", "futureMonths"));

      // 使用新方法生成 year_1 到 year_11 的 CTE
      sql.append(generateYearCTE("ACC_ITEM", 1, 11));

      sql.append("SELECT \n");
      sql.append("    ratd.BILL_ACC_ITEM AS accItem, \n");
      sql.append("    ratd.BILL_OVD_ITEM AS ovdItem, \n");
      sql.append("    ratd.BILL_ACC_NAME AS accName, \n");
      sql.append(generateSelectBody());

      sql.append("\n");

      sql.append("FROM \n");
      sql.append("    RPT_ACC_TYPE_DETL ratd \n");
      sql.append("LEFT JOIN currentMonth cm ON ratd.BILL_ACC_ITEM = cm.ACC_ITEM \n");
      sql.append("LEFT JOIN futureMonths fm ON ratd.BILL_ACC_ITEM = fm.ACC_ITEM \n");

      // 添加 year_1 到 year_11 的 JOIN
      for (int year = 1; year <= 11; year++) {
        sql.append("LEFT JOIN year_").append(year).append(" y").append(year).append(" ON ratd.BILL_ACC_ITEM = y")
            .append(year).append(".ACC_ITEM \n");
      }

      sql.append("ORDER BY \n");
      sql.append("    accItem");

      String finalSql = sql.toString();
      return finalSql;
    }



    public String selectBP2230D5Summary(@Param("currentDate") String currentDate) {
      StringBuilder sql = new StringBuilder();

      sql.append("WITH \n");
      sql.append(generateMonthCTE("BILL_OFF_BELONG","currentMonth", "0", "currentMonth"));
      sql.append(generateMonthCTE("BILL_OFF_BELONG","futureMonths", "1", "futureMonths"));

      // 使用新方法生成 year_1 到 year_11 的 CTE
      sql.append(generateYearCTE("BILL_OFF_BELONG", 1, 11));

      sql.append("SELECT \n");
      sql.append("    COALESCE(cm.BILL_OFF_BELONG, fm.BILL_OFF_BELONG");

      for (int year = 1; year <= 11; year++) {
        sql.append(", y").append(year).append(".BILL_OFF_BELONG");
      }

      sql.append(") AS billOffBelong, \n");
      sql.append(generateSelectBody());

      sql.append("\n");

      sql.append("FROM \n");
      sql.append("    currentMonth cm \n");
      sql.append("FULL OUTER JOIN futureMonths fm ON cm.BILL_OFF_BELONG = fm.BILL_OFF_BELONG \n");

      for (int year = 1; year <= 11; year++) {
        sql.append("FULL OUTER JOIN year_").append(year).append(" y").append(year)
            .append(" ON COALESCE(cm.BILL_OFF_BELONG, fm.BILL_OFF_BELONG");

        for (int prevYear = 1; prevYear < year; prevYear++) {
          sql.append(", y").append(prevYear).append(".BILL_OFF_BELONG");
        }

        sql.append(") = y").append(year).append(".BILL_OFF_BELONG \n");
      }

      sql.append("ORDER BY \n");
      sql.append("    billOffBelong");

      String finalSql = sql.toString();
      return finalSql;
    }
  }

}