package ccbs.dao.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import java.util.List;

import ccbs.dao.core.entity.EMPData;
import ccbs.dao.core.entity.RptAccountSummary;
import ccbs.dao.core.entity.RptBP2230D4Summary;
import ccbs.dao.core.entity.RptBP2230D5Summary;
import ccbs.dao.core.entity.RptBP2230D6Summary;
import ccbs.dao.core.entity.RptBP2240D1Summary;
import ccbs.dao.core.entity.RptBP22TOTSummary;
import ccbs.dao.core.entity.RptBPGNERPSummary;
import ccbs.dao.core.entity.RptBPGNIDSummary;
import ccbs.dao.core.entity.RptBPGUSUBSummary;
import ccbs.dao.core.entity.RptBPOWE2WSummary;
import ccbs.dao.core.entity.RptBPOWESummary;
import ccbs.dao.core.entity.RptBPZ10Summary;
import ccbs.dao.core.entity.RptBP222OTSummary;
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

  @SelectProvider(type = SqlProvider.class, method = "selectBP2230D6Summary")
  List<RptBP2230D6Summary> selectBP2230D6Summary(@Param("currentDate") String currentDate);

  @SelectProvider(type = SqlProvider.class, method = "selectBP222OTSummary")
  List<RptBP222OTSummary> selectBP222OTSummary(@Param("currentDate") String currentDate,
      @Param("rptType") String rptType);

  @SelectProvider(type = SqlProvider.class, method = "selectBP22TOTSummary")
  List<RptBP22TOTSummary> selectBP22TOTSummary(@Param("currentDate") String currentDate);

  @SelectProvider(type = SqlProvider.class, method = "selectBPGNERPSummary")
  List<RptBPGNERPSummary> selectBPGNERPSummary(@Param("type3") String type3);
  
  @SelectProvider(type = SqlProvider.class, method = "selectBPOWE2WSummary")
  List<RptBPOWE2WSummary> selectBPOWE2WSummary(
    @Param("currentDate") String currentDate,
    @Param("keepCnt") String keepCnt, 
    @Param("inputOPID") String inputOPID, 
    @Param("inputSTATUS") String inputSTATUS);

  @SelectProvider(type = SqlProvider.class, method = "selectBPOWESummary")
  List<RptBPOWESummary> selectBPOWESummary(
    @Param("currentDate") String currentDate,
    @Param("keepCnt") String keepCnt, 
    @Param("inputOPID") String inputOPID, 
    @Param("inputSTATUS") String inputSTATUS);

  @SelectProvider(type = SqlProvider.class, method = "selectBPZ10Summary")
  List<RptBPZ10Summary> selectBPZ10Summary(
    @Param("currentDate") String currentDate,
    @Param("itemType") String itemType);

  @SelectProvider(type = SqlProvider.class, method = "selectBPGUSUBSummary")
  List<RptBPGUSUBSummary> selectBPGUSUBSummary(
    @Param("billIdnoList") String billIdnoList,
    @Param("itemType") String itemType);

  @SelectProvider(type = SqlProvider.class, method = "selectBPGNIDSummary")
  RptBPGNIDSummary selectBPGNIDSummary(
    @Param("currentDate") String currentDate,
    @Param("billIdno") String billIdno);

  @SelectProvider(type = SqlProvider.class, method = "selectEMPData")
  EMPData selectEMPData(
    @Param("empId") String empId);

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
          sql.append("WHERE TO_NUMBER(SUBSTR(a0.bill_month, 1, 3)) = TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), -2), 'YYYY')) - 1911 ");
          sql.append("AND TO_NUMBER(SUBSTR(a0.bill_month, 4, 2)) <= TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), -2), 'MM'))");
        } else if (dateType.equals(DateTypeConstants.ACU_X_1M)) {
          // 統計至執行日的前1個月
          sql.append("WHERE TO_NUMBER(SUBSTR(a0.bill_month, 1, 3)) = TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), -1), 'YYYY')) - 1911 ");
          sql.append("AND TO_NUMBER(SUBSTR(a0.bill_month, 4, 2)) <= TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), -1), 'MM'))");
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
        sql.append("AND a0.acc_item not in ('5609-9299A','5609-9299B') ");
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
      sql.append("NVL(SUM(CASE WHEN b.rcv_item = '2825-2402' THEN b.bill_item_amt ELSE 0 END), 0) AS aCol, ");
      sql.append("NVL(SUM(CASE WHEN b.rcv_item = '2825-2403' THEN b.bill_item_amt ELSE 0 END), 0) AS bCol, ");
      sql.append("NVL(SUM(CASE WHEN b.rcv_item = '2825-2404' THEN b.bill_item_amt ELSE 0 END), 0) AS cCol, ");
      sql.append("NVL(SUM(CASE WHEN b.rcv_item = '2825-2405' THEN b.bill_item_amt ELSE 0 END), 0) AS dCol, ");
      sql.append("NVL(SUM(CASE WHEN b.rcv_item = '2825-2406' THEN b.bill_item_amt ELSE 0 END), 0) AS eCol, ");
      sql.append("NVL(SUM(CASE WHEN b.rcv_item = '1816-2401' THEN b.bill_item_amt ELSE 0 END), 0) AS fCol, ");
      sql.append("NVL(SUM(CASE WHEN b.rcv_item = '1816-2402A' THEN b.bill_item_amt ELSE 0 END), 0) AS gCol, ");
      sql.append("NVL(SUM(CASE WHEN b.rcv_item = '2253-02EA' THEN b.bill_item_amt ELSE 0 END), 0) AS hCol, ");
      sql.append("NVL(SUM(CASE WHEN b.rcv_item = '2253-02EC' THEN b.bill_item_amt ELSE 0 END), 0) AS iCol, ");
      sql.append("NVL(SUM(CASE WHEN b.acc_item in ('1178-29N', '1178-29S') THEN b.bill_item_amt ELSE 0 END), 0) AS jCol ");
      sql.append(
          "FROM (SELECT DISTINCT BU_GROUP_MARK FROM rpt_account) a LEFT JOIN rpt_account b ON a.bu_group_mark = b.bu_group_mark ");
      sql.append(
          "AND (b.rcv_item IN ('2825-2402', '2825-2403', '2825-2404', '2825-2405', '2825-2406', '1816-2401', '1816-2402A', '2253-02EA', '2253-02EC') or b.acc_item IN ('1178-29N', '1178-29S')) ");
      sql.append("AND b.bu_group_mark IN ('A', 'B', 'C') ");
      sql.append(
          " AND TO_NUMBER(SUBSTR(b.bill_month, 1, 5)) = TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), -6), 'YYYYMM')) - 191100 \n");
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

    private String generateSelectBody(Boolean isD4) {
      StringBuilder sql = new StringBuilder();
      
      if (isD4) {
        sql.append("    SUM(COALESCE(cm.currentMonthBadDebt, 0)) AS currentMonthBadDebt, \n");
        sql.append("    SUM(COALESCE(cm.currentMonthNonBadDebt, 0)) AS currentMonthNonBadDebt, \n");
        sql.append("    SUM(COALESCE(fm.futureMonthsBadDebt, 0)) AS futureMonthsBadDebt, \n");
        sql.append("    SUM(COALESCE(fm.futureMonthsNonBadDebt, 0)) AS futureMonthsNonBadDebt, \n");
  
        for (int year = 1; year <= 11; year++) {
          sql.append("    SUM(COALESCE(y").append(year).append(".year").append(year).append("BadDebt, 0)) AS year")
              .append(year).append("BadDebt, \n");
          sql.append("    SUM(COALESCE(y").append(year).append(".year").append(year).append("NonBadDebt, 0)) AS year")
              .append(year).append("NonBadDebt, \n");
        }
  
        StringBuilder totalBadDebt = new StringBuilder("SUM(COALESCE(cm.currentMonthBadDebt, 0) + COALESCE(fm.futureMonthsBadDebt, 0)");
        StringBuilder totalNonBadDebt = new StringBuilder("SUM(COALESCE(cm.currentMonthNonBadDebt, 0) + COALESCE(fm.futureMonthsNonBadDebt, 0)");
  
        for (int year = 1; year <= 11; year++) {
          totalBadDebt.append(" + COALESCE(y").append(year).append(".year").append(year).append("BadDebt, 0)");
          totalNonBadDebt.append(" + COALESCE(y").append(year).append(".year").append(year).append("NonBadDebt, 0)");
        }

        sql.append(totalBadDebt).append(") AS totalBadDebt, \n");
        sql.append(totalNonBadDebt).append(") AS totalNonBadDebt \n");
      } else {
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
      }

      return sql.toString();
    }

    public String selectBP2230D4Summary(@Param("currentDate") String currentDate) {
      StringBuilder sql = new StringBuilder();

      sql.append("WITH \n");
      sql.append("unmatched_items AS ( \n");
      sql.append("    SELECT DISTINCT ACC_ITEM \n");
      sql.append("    FROM RPT_ACCOUNT ra \n");
      sql.append("    WHERE NOT EXISTS ( \n");
      sql.append("        SELECT 1 \n");
      sql.append("        FROM RPT_ACC_TYPE_DETL ratd \n");
      sql.append("        WHERE ra.ACC_ITEM = ratd.BILL_ACC_ITEM OR ra.ACC_ITEM = ratd.BILL_OVD_ITEM \n");
      sql.append("    ) \n");
      sql.append("), \n");
      sql.append("combined_items AS ( \n");
      sql.append("    SELECT TRIM(LEAST(BILL_ACC_ITEM, BILL_OVD_ITEM)) AS ACC_ITEM_1, \n");
      sql.append("           GREATEST(BILL_ACC_ITEM, BILL_OVD_ITEM) AS ACC_ITEM_2, \n");
      sql.append("           BILL_ACC_NAME, \n");
      sql.append("           'N' AS is_unmatched \n");
      sql.append("    FROM RPT_ACC_TYPE_DETL \n");
      sql.append("    WHERE TRIM(BILL_ACC_ITEM) IS NOT NULL \n");
      sql.append("    GROUP BY LEAST(BILL_ACC_ITEM, BILL_OVD_ITEM), GREATEST(BILL_ACC_ITEM, BILL_OVD_ITEM), BILL_ACC_NAME \n");
      sql.append("    UNION ALL \n");
      sql.append("    SELECT ACC_ITEM, NULL, 'OTHER', 'Y' \n");
      sql.append("    FROM unmatched_items \n");
      sql.append("), \n");
      sql.append(generateMonthCTE("ACC_ITEM", "currentMonth", "0", "currentMonth"));
      sql.append(generateMonthCTE("ACC_ITEM","futureMonths", "1", "futureMonths"));

      // 使用新方法生成 year_1 到 year_11 的 CTE
      sql.append(generateYearCTE("ACC_ITEM", 1, 11));

      sql.append("SELECT \n");
      sql.append("    CASE WHEN ratd.is_unmatched = 'Y' THEN 'OTHER' ELSE ratd.ACC_ITEM_1 END AS accItem, \n");
      sql.append("    ratd.ACC_ITEM_2 AS ovdItem, \n");
      sql.append("    MAX(ratd.BILL_ACC_NAME) AS accName, \n");
      sql.append(generateSelectBody(true));

      sql.append("\n");

      sql.append("FROM \n");
      sql.append("    combined_items ratd \n");
      sql.append("LEFT JOIN currentMonth cm ON ratd.ACC_ITEM_1 = cm.ACC_ITEM OR ratd.ACC_ITEM_2 = cm.ACC_ITEM \n");
      sql.append("LEFT JOIN futureMonths fm ON ratd.ACC_ITEM_1 = fm.ACC_ITEM OR ratd.ACC_ITEM_2 = fm.ACC_ITEM  \n");

      // 添加 year_1 到 year_11 的 JOIN
      for (int year = 1; year <= 11; year++) {
        sql.append("LEFT JOIN year_").append(year).append(" y").append(year).append(" ON ratd.ACC_ITEM_1 = y")
            .append(year).append(".ACC_ITEM OR ratd.ACC_ITEM_2 = y").append(year).append(".ACC_ITEM \n");
      }

      sql.append("GROUP BY \n");
      sql.append("    CASE WHEN ratd.is_unmatched = 'Y' THEN 'OTHER' ELSE ratd.ACC_ITEM_1 END, \n");
      sql.append("    ratd.ACC_ITEM_2 \n");

      sql.append("ORDER BY \n");
      sql.append("    CASE WHEN accItem = 'OTHER' THEN 1 ELSE 0 END, \n");
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
      sql.append(generateSelectBody(false));

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

    public String selectBP2230D6Summary(@Param("currentDate") String currentDate) {
      StringBuilder sql = new StringBuilder();

      sql.append("WITH \n");
      sql.append("unmatched_items AS ( \n");
      sql.append("    SELECT DISTINCT ACC_ITEM \n");
      sql.append("    FROM RPT_ACCOUNT ra \n");
      sql.append("    WHERE NOT EXISTS ( \n");
      sql.append("        SELECT 1 \n");
      sql.append("        FROM RPT_ACC_TYPE_DETL ratd \n");
      sql.append("        WHERE ra.ACC_ITEM = ratd.BILL_ACC_ITEM OR ra.ACC_ITEM = ratd.BILL_OVD_ITEM \n");
      sql.append("    ) \n");
      sql.append("), \n");
      sql.append("combined_items AS ( \n");
      sql.append("    SELECT TRIM(LEAST(BILL_ACC_ITEM, BILL_OVD_ITEM)) AS ACC_ITEM_1, \n");
      sql.append("           GREATEST(BILL_ACC_ITEM, BILL_OVD_ITEM) AS ACC_ITEM_2, \n");
      sql.append("           BILL_ACC_NAME, \n");
      sql.append("           'N' AS is_unmatched \n");
      sql.append("    FROM RPT_ACC_TYPE_DETL \n");
      sql.append("    WHERE TRIM(BILL_ACC_ITEM) IS NOT NULL \n");
      sql.append("    GROUP BY LEAST(BILL_ACC_ITEM, BILL_OVD_ITEM), GREATEST(BILL_ACC_ITEM, BILL_OVD_ITEM), BILL_ACC_NAME \n");
      sql.append("    UNION ALL \n");
      sql.append("    SELECT ACC_ITEM, NULL, 'OTHER', 'Y' \n");
      sql.append("    FROM unmatched_items \n");
      sql.append("), \n");
      sql.append("monthly AS (\n");
      sql.append("    SELECT ACC_ITEM,\n");
      sql.append("           TO_NUMBER(SUBSTR(ra.bill_month, 4, 2)) AS month,\n");
      sql.append("           COALESCE(SUM(CASE WHEN ra.debt_mark NOT IN ('Q', 'Y') THEN ra.bill_item_amt ELSE 0 END), 0) AS nonBadDebt\n");
      sql.append("    FROM RPT_ACCOUNT ra\n");
      sql.append("    WHERE TO_NUMBER(SUBSTR(ra.bill_month, 1, 3)) = TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), 0), 'YYYY')) - 1911\n");
      sql.append("          AND TO_NUMBER(SUBSTR(ra.bill_month, 4, 2)) BETWEEN 1 AND 12\n");
      sql.append("    GROUP BY ACC_ITEM, TO_NUMBER(SUBSTR(ra.bill_month, 4, 2))\n");
      sql.append(")\n");
      sql.append("SELECT \n");
      sql.append("    CASE WHEN ratd.is_unmatched = 'Y' THEN 'OTHER' ELSE ratd.ACC_ITEM_1 END AS accItem,\n");
      sql.append("    ratd.ACC_ITEM_2 AS ovdItem,\n");
      sql.append("    MAX(ratd.BILL_ACC_NAME) AS accName,\n");
      for (int month = 1; month <= 12; month++) {
        sql.append("    COALESCE(MAX(CASE WHEN month = ").append(month).append(" THEN nonBadDebt ELSE 0 END), 0) AS nonBadDebt_").append(month).append(",\n");
      }
      for (int month = 1; month <= 11; month++) {
        sql.append("    COALESCE(MAX(CASE WHEN month = ").append(month).append(" THEN nonBadDebt ELSE 0 END), 0) +\n");
      }
      sql.append("    COALESCE(MAX(CASE WHEN month = 12 THEN nonBadDebt ELSE 0 END), 0) AS totalNonBadDebt,\n");
      sql.setLength(sql.length() - 2); // 移除最後一個逗號
      sql.append("\n");
      sql.append("FROM \n");
      sql.append("    combined_items ratd \n");
      sql.append("LEFT JOIN monthly cm ON ratd.ACC_ITEM_1 = cm.ACC_ITEM OR ratd.ACC_ITEM_2 = cm.ACC_ITEM \n");
      sql.append("GROUP BY \n");
      sql.append("    CASE WHEN ratd.is_unmatched = 'Y' THEN 'OTHER' ELSE ratd.ACC_ITEM_1 END,\n");
      sql.append("    ratd.ACC_ITEM_2\n");
      sql.append("ORDER BY \n");
      sql.append("    CASE WHEN accItem = 'OTHER' THEN 1 ELSE 0 END, \n");
      sql.append("    accItem");

      String finalSql = sql.toString();
      return finalSql;
    }

    public String selectBP222OTSummary(@Param("currentDate") String currentDate, @Param("rptType") String rptType) {
      StringBuilder sql = new StringBuilder();

      sql.append("SELECT \n");
      if ("2".equals(rptType)) {
        sql.append("    BILL_OFF_BELONG AS billOffBelong, \n");
      }
      sql.append("    BU_GROUP_MARK AS buGroupMark, \n");
      sql.append("    ACC_ITEM AS accItem, \n");
      sql.append("    BILL_MONTH AS billMonth, \n");
      sql.append("    SUM(BILL_ITEM_AMT) AS sumBillItemAmt \n");
      sql.append("FROM \n");
      sql.append("    RPT_ACCOUNT ra \n");
      sql.append("GROUP BY \n");
      if ("2".equals(rptType)) {
        sql.append("    BILL_OFF_BELONG, BU_GROUP_MARK, ACC_ITEM, BILL_MONTH \n");
      } else {
        sql.append("    BU_GROUP_MARK, ACC_ITEM, BILL_MONTH \n");
      }
      
      sql.append("ORDER BY \n");
      if ("2".equals(rptType)) {
        sql.append("    BILL_OFF_BELONG, BU_GROUP_MARK, ACC_ITEM, BILL_MONTH \n");
      } else {
        sql.append("    BU_GROUP_MARK, ACC_ITEM, BILL_MONTH \n");
      }
      
      return sql.toString();
    }

    public String selectBP22TOTSummary(@Param("currentDate") String currentDate) {
      StringBuilder sql = new StringBuilder();

      sql.append("WITH overdue_months AS ( \n");
      sql.append("    SELECT TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), -4), 'YYYYMM') AS BILL_MONTH, 3 AS OVERDUE_MONTHS FROM dual UNION ALL \n");
      sql.append("    SELECT TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), -7), 'YYYYMM') AS BILL_MONTH, 6 AS OVERDUE_MONTHS FROM dual UNION ALL \n");
      sql.append("    SELECT TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), -10), 'YYYYMM') AS BILL_MONTH, 9 AS OVERDUE_MONTHS FROM dual UNION ALL \n");
      sql.append("    SELECT TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), -13), 'YYYYMM') AS BILL_MONTH, 12 AS OVERDUE_MONTHS FROM dual UNION ALL \n");
      sql.append("    SELECT TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), -16), 'YYYYMM') AS BILL_MONTH, 15 AS OVERDUE_MONTHS FROM dual UNION ALL \n");
      sql.append("    SELECT TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), -19), 'YYYYMM') AS BILL_MONTH, 18 AS OVERDUE_MONTHS FROM dual UNION ALL \n");
      sql.append("    SELECT TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), -25), 'YYYYMM') AS BILL_MONTH, 24 AS OVERDUE_MONTHS FROM dual \n");
      sql.append("), \n");
      sql.append("valid_acc_items AS ( \n");
      sql.append("    SELECT '1144-11N' AS ACC_ITEM FROM dual UNION ALL SELECT '1144-11S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1812-11N' FROM dual UNION ALL SELECT '1812-11S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1144-21N' FROM dual UNION ALL SELECT '1144-21S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1812-21N' FROM dual UNION ALL SELECT '1812-21S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1144-31N' FROM dual UNION ALL SELECT '1144-31S' FROM dual UNION ALL SELECT '1144-31I' FROM dual UNION ALL \n");
      sql.append("    SELECT '1812-31N' FROM dual UNION ALL SELECT '1812-31S' FROM dual UNION ALL SELECT '1812-31I' FROM dual UNION ALL \n");
      sql.append("    SELECT '1144-42N' FROM dual UNION ALL SELECT '1144-42S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1812-42N' FROM dual UNION ALL SELECT '1812-42S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1144-45N' FROM dual UNION ALL SELECT '1144-45S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1812-45N' FROM dual UNION ALL SELECT '1812-45S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1144-46N' FROM dual UNION ALL SELECT '1144-46S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1812-46N' FROM dual UNION ALL SELECT '1812-46S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1144-61N' FROM dual UNION ALL SELECT '1144-61S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1812-61N' FROM dual UNION ALL SELECT '1812-61S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1144-62N' FROM dual UNION ALL SELECT '1144-62S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1812-62N' FROM dual UNION ALL SELECT '1812-62S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1144-63N' FROM dual UNION ALL SELECT '1144-63S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1812-63N' FROM dual UNION ALL SELECT '1812-63S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1144-16N' FROM dual UNION ALL SELECT '1144-16S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1812-16N' FROM dual UNION ALL SELECT '1812-16S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1144-71N' FROM dual UNION ALL SELECT '1144-71S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1812-71N' FROM dual UNION ALL SELECT '1812-71S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1144-81N' FROM dual UNION ALL SELECT '1144-81S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1812-81N' FROM dual UNION ALL SELECT '1812-81S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1144-82N' FROM dual UNION ALL SELECT '1144-82S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1812-82N' FROM dual UNION ALL SELECT '1812-82S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1144-83N' FROM dual UNION ALL SELECT '1144-83S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1812-83N' FROM dual UNION ALL SELECT '1812-83S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1144-90N' FROM dual UNION ALL SELECT '1144-90S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1812-90N' FROM dual UNION ALL SELECT '1812-90S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1178-69N' FROM dual UNION ALL SELECT '1178-69S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1178-73N' FROM dual UNION ALL SELECT '1178-73S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1812-69N' FROM dual UNION ALL SELECT '1812-69S' FROM dual UNION ALL \n");
      sql.append("    SELECT '1812-73N' FROM dual UNION ALL SELECT '1812-73S' FROM dual \n");
      sql.append("), \n");
      sql.append("expanded_rpt_account AS ( \n");
      sql.append("    SELECT \n");
      sql.append("        r.*, \n");
      sql.append("        CASE \n");
      sql.append("            WHEN r.ACC_ITEM IN (SELECT ACC_ITEM FROM valid_acc_items) THEN r.ACC_ITEM \n");
      sql.append("            ELSE 'OTHER' \n");
      sql.append("        END AS accItemGroup \n");
      sql.append("    FROM rpt_account r \n");
      sql.append(") \n");
      sql.append("SELECT \n");
      sql.append("    r.BILL_MONTH AS billMonth, \n");
      sql.append("    r.BILL_OFF_BELONG AS billOffBelong, \n");
      sql.append("    SUBSTR(r.accItemGroup, 1, INSTR(r.accItemGroup, '-') - 1) AS accItem1, \n");
      sql.append("    SUBSTR(r.accItemGroup, INSTR(r.accItemGroup, '-') + 1) AS accItem2, \n");
      sql.append("    SUM(r.BILL_ITEM_AMT) AS sumBillItemAmt \n");
      sql.append("FROM \n");
      sql.append("    expanded_rpt_account r \n");
      sql.append("JOIN \n");
      sql.append("    overdue_months o ON TO_CHAR(TO_NUMBER(SUBSTR(o.BILL_MONTH, 1, 4)) - 1911) || SUBSTR(o.BILL_MONTH, 5, 2) = r.BILL_MONTH \n");
      sql.append("WHERE \n");
      sql.append("    r.ACC_ITEM NOT LIKE '1813%' \n");
      sql.append("    AND r.ACC_ITEM NOT LIKE '5609%' \n");
      sql.append("GROUP BY \n");
      sql.append("    r.BILL_OFF_BELONG, \n");
      sql.append("    r.accItemGroup, \n");
      sql.append("    r.BILL_MONTH \n");
      sql.append("ORDER BY \n");
      sql.append("    r.BILL_OFF_BELONG ASC, \n");
      sql.append("    r.BILL_MONTH ASC, \n");
      sql.append("    r.accItemGroup ASC \n");
      
      return sql.toString();
    }

    public String selectBPGNERPSummary(@Param("type3") String type3) {
      StringBuilder sql = new StringBuilder();
      sql.append("SELECT \n");
      sql.append("    BILL_OFF AS billOff, \n");
      sql.append("    BILL_TEL AS billTel, \n");
      sql.append("    BILL_MONTH AS billMonth, \n");
      sql.append("    BILL_IDNO AS billIdno, \n");
      sql.append("    BILL_AMT AS billAmt \n");
      sql.append("FROM \n");
      sql.append("    RPT_BILL_MAIN rbm \n");
      sql.append("WHERE \n");
      sql.append("    BILL_TEL LIKE #{type3} || '%' \n");
      return sql.toString();
    }

    public String selectBPOWE2WSummary(@Param("currentDate") String currentDate,
    @Param("keepCnt") String keepCnt, 
    @Param("inputOPID") String inputOPID, 
    @Param("inputSTATUS") String inputSTATUS) {
      StringBuilder sql = new StringBuilder();
      
      sql.append("WITH RankedData AS ( \n");
      sql.append("    SELECT \n");
      sql.append("        BILL_TEL, \n");
      sql.append("        SUM(BILL_AMT) AS TOTAL_BILL_AMT, \n");
      sql.append("        ROW_NUMBER() OVER (ORDER BY SUM(BILL_AMT) DESC) AS RANKING \n");
      sql.append("    FROM RPT_BILL_MAIN \n");
      sql.append("    WHERE 0=0 \n");
      if (inputOPID != null && !inputOPID.isEmpty()) {
          sql.append("    AND BILL_OPID = #{inputOPID} \n");
      }
      if (inputSTATUS != null && !inputSTATUS.isEmpty()) {
          sql.append("    AND TEL_STATUS = #{inputSTATUS} \n");
      }
      sql.append("    AND TO_NUMBER(SUBSTR(BILL_MONTH, 1, 5)) <> TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), 0), 'YYYYMM')) - 191100 \n");
      sql.append("    GROUP BY BILL_TEL \n");
      sql.append(") \n");
      sql.append("SELECT \n");
      sql.append("    RD.RANKING AS ranking, \n");
      sql.append("    MAX(RBM.BILL_OFF_BELONG) AS billOffBelong, \n");
      sql.append("    MAX(RBM.BILL_OFF) AS billOff, \n");
      sql.append("    MAX(RD.BILL_TEL) AS billTel, \n");
      sql.append("    MAX(RBM.BILL_IDNO) AS billIdno, \n");
      sql.append("    MAX(RBM.DR_DATE) AS drDate, \n");
      sql.append("    MAX(RD.TOTAL_BILL_AMT) AS totalBillAmt \n");
      sql.append("FROM RankedData RD \n");
      sql.append("LEFT JOIN RPT_BILL_MAIN RBM \n");
      sql.append("    ON RD.BILL_TEL = RBM.BILL_TEL \n");
      if (keepCnt != null) {
          sql.append("WHERE RD.RANKING <= #{keepCnt} \n");
      }
      sql.append("GROUP BY RD.RANKING \n");
      sql.append("ORDER BY RD.RANKING \n");

      return sql.toString();
    }

    public String selectBPOWESummary(@Param("currentDate") String currentDate,
    @Param("keepCnt") String keepCnt, 
    @Param("inputOPID") String inputOPID, 
    @Param("inputSTATUS") String inputSTATUS) {
      StringBuilder sql = new StringBuilder();
      
      sql.append("WITH RankedData AS ( \n");
      sql.append("    SELECT \n");
      sql.append("        BILL_TEL, \n");
      sql.append("        SUM(BILL_AMT) AS TOTAL_BILL_AMT, \n");
      sql.append("        ROW_NUMBER() OVER (ORDER BY SUM(BILL_AMT) DESC) AS RANKING \n");
      sql.append("    FROM RPT_BILL_MAIN \n");
      sql.append("    WHERE 0=0 \n");
      if (inputOPID != null && !inputOPID.isEmpty()) {
          sql.append("    AND BILL_OPID = #{inputOPID} \n");
      }
      if (inputSTATUS != null && !inputSTATUS.isEmpty()) {
          sql.append("    AND TEL_STATUS = #{inputSTATUS} \n");
      }
      sql.append("    AND TO_NUMBER(SUBSTR(BILL_MONTH, 1, 5)) <> TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), 0), 'YYYYMM')) - 191100 \n");
      sql.append("    GROUP BY BILL_TEL \n");
      sql.append(") \n");
      sql.append("SELECT \n");
      sql.append("    RD.RANKING AS ranking, \n");
      sql.append("    RBM.BILL_OFF_BELONG AS billOffBelong, \n");
      sql.append("    RBM.BILL_OFF AS billOff, \n");
      sql.append("    RD.BILL_TEL AS billTel, \n");
      sql.append("    RBM.BILL_IDNO AS billIdno, \n");
      sql.append("    RBM.BILL_MONTH AS billMonth, \n");
      sql.append("    RBM.BILL_ID AS billId, \n");
      sql.append("    RBM.DR_DATE AS drDate, \n");
      sql.append("    RBM.BILL_AMT AS totalBillAmt \n");
      sql.append("FROM RPT_BILL_MAIN RBM \n");
      sql.append("LEFT JOIN RankedData RD \n");
      sql.append("    ON RD.BILL_TEL = RBM.BILL_TEL \n");
      if (keepCnt != null) {
          sql.append("WHERE RD.RANKING <= #{keepCnt} \n");
      }
      sql.append("ORDER BY RD.RANKING \n");

      return sql.toString();
    }
    
    public String selectBPZ10Summary(@Param("currentDate") String currentDate,
    @Param("itemType") String itemType) {
      StringBuilder sql = new StringBuilder();
      
      sql.append("SELECT \n");
      sql.append("    rit.BILL_ITEM_NAME AS billItemName, \n");
      sql.append("    rbm.BILL_OFF_BELONG, \n");
      sql.append("    rbd.BILL_OFF AS billOff, \n");
      sql.append("    rbd.BILL_TEL AS billTel, \n");
      sql.append("    COALESCE(brd.SUB_OFF, rbm.BILL_OFF) AS billSubOff, \n");
      sql.append("    COALESCE(brd.SUB_TEL, rbm.BILL_TEL) AS billSubTel, \n");
      sql.append("    rbd.BILL_MONTH AS billMonth, \n");
      sql.append("    rbm.BILL_AMT AS billAmt, \n");
      sql.append("    ritd.BILL_ITEM_CODE AS billItemCode, \n");
      sql.append("    rbd.BILL_ITEM_AMT AS billItemAmt \n");
      sql.append("FROM \n");
      sql.append("    (SELECT * FROM RPT_ITEM_TYPE WHERE BILL_ITEM_TYPE = #{itemType}) rit \n");
      sql.append("JOIN \n");
      sql.append("    RPT_ITEM_TYPE_DETL ritd \n");
      sql.append("    ON rit.BILL_ITEM_TYPE = ritd.BILL_ITEM_TYPE \n");
      sql.append("JOIN \n");
      sql.append("    RPT_BILL_DETL rbd \n");
      sql.append("    ON ritd.BILL_ITEM_CODE = rbd.BILL_ITEM_CODE \n");
      sql.append("    AND TO_NUMBER(SUBSTR(rbd.BILL_MONTH, 1, 5)) >= \n");
      sql.append("        TO_NUMBER(TO_CHAR(ADD_MONTHS(TO_DATE(#{currentDate}, 'YYYYMM'), -4), 'YYYYMM')) - 191100 \n");
      sql.append("LEFT JOIN \n");
      sql.append("    BILL_RELS_DETL brd \n");
      sql.append("    ON rbd.BILL_ITEM_CODE = brd.SUB_ITEM_CODE \n");
      sql.append("    AND rbd.BILL_MONTH = brd.BILL_MONTH \n");
      sql.append("    AND rbd.BILL_ID = brd.BILL_ID \n");
      sql.append("    AND rbd.BILL_OFF = brd.BILL_OFF \n");
      sql.append("    AND rbd.BILL_TEL = brd.BILL_TEL \n");
      sql.append("JOIN \n");
      sql.append("    RPT_BILL_MAIN rbm \n");
      sql.append("    ON rbd.BILL_MONTH = rbm.BILL_MONTH \n");
      sql.append("    AND rbd.BILL_ID = rbm.BILL_ID \n");
      sql.append("    AND rbd.BILL_OFF = rbm.BILL_OFF \n");
      sql.append("    AND rbd.BILL_TEL = rbm.BILL_TEL \n");
      sql.append("ORDER BY \n");
      sql.append("    rbd.BILL_OFF \n");

      return sql.toString();
    }

    public String selectBPGUSUBSummary(@Param("billIdnoList") String billIdnoList,
    @Param("itemType") String itemType) {
      StringBuilder sql = new StringBuilder();
      
      sql.append("WITH BILL_IDNO_LIST AS ( \n");
      sql.append("    SELECT TRIM(REGEXP_SUBSTR( \n");
      sql.append("        #{billIdnoList}, '[^,]+', 1, LEVEL)) AS BILL_IDNO \n");
      sql.append("    FROM DUAL \n");
      sql.append("    CONNECT BY REGEXP_SUBSTR( \n");
      sql.append("        #{billIdnoList}, '[^,]+', 1, LEVEL) IS NOT NULL \n");
      sql.append("), \n");
      sql.append("FILTERED_BILL_DETL AS ( \n");
      sql.append("    SELECT \n");
      sql.append("        rbd.BILL_MONTH, \n");
      sql.append("        rbd.BILL_TEL, \n");
      sql.append("        rbd.BILL_ITEM_CODE, \n");
      sql.append("        rbd.BILL_ITEM_AMT \n");
      sql.append("    FROM \n");
      sql.append("        RPT_BILL_DETL rbd \n");
      sql.append("    JOIN \n");
      sql.append("        (SELECT * FROM RPT_ITEM_TYPE_DETL WHERE BILL_ITEM_TYPE = #{itemType}) ritd \n");
      sql.append("    ON rbd.BILL_ITEM_CODE = ritd.BILL_ITEM_CODE \n");
      sql.append("), \n");
      sql.append("AGGREGATED_BILL_DETL AS ( \n");
      sql.append("    SELECT \n");
      sql.append("        fbd.BILL_MONTH, \n");
      sql.append("        fbd.BILL_TEL, \n");
      sql.append("        SUM(fbd.BILL_ITEM_AMT) AS TOTAL_AMT \n");
      sql.append("    FROM \n");
      sql.append("        FILTERED_BILL_DETL fbd \n");
      sql.append("    GROUP BY \n");
      sql.append("        fbd.BILL_MONTH, fbd.BILL_TEL \n");
      sql.append(") \n");
      sql.append("SELECT \n");
      sql.append("    bil.BILL_IDNO AS billIdno, \n");
      sql.append("    rbm.BILL_MONTH AS billMonth, \n");
      sql.append("    rbm.BILL_ID AS billId, \n");
      sql.append("    rbm.BILL_CYCLE AS billCycle, \n");
      sql.append("    rbm.BILL_OFF AS billOff, \n");
      sql.append("    rbm.BILL_TEL AS billTel, \n");
      sql.append("    NVL(abd.TOTAL_AMT, 0) AS totalAmt, \n");
      sql.append("    CASE \n");
      sql.append("        WHEN NVL(abd.TOTAL_AMT, 0) = 0 THEN 'N' \n");
      sql.append("        ELSE 'Y' \n");
      sql.append("    END AS dueId \n");
      sql.append("FROM \n");
      sql.append("    BILL_IDNO_LIST bil \n");
      sql.append("LEFT JOIN \n");
      sql.append("    RPT_BILL_MAIN rbm \n");
      sql.append("    ON bil.BILL_IDNO = TRIM(rbm.BILL_IDNO) \n");
      sql.append("LEFT JOIN \n");
      sql.append("    AGGREGATED_BILL_DETL abd \n");
      sql.append("    ON rbm.BILL_MONTH = abd.BILL_MONTH \n");
      sql.append("    AND rbm.BILL_TEL = abd.BILL_TEL \n");

      return sql.toString();
    }

    public String selectBPGNIDSummary(@Param("currentDate") String currentDate,
    @Param("billIdno") String billIdno) {
      StringBuilder sql = new StringBuilder();
      
      sql.append("SELECT \n");
      sql.append("    BILL_IDNO AS billIdno, \n");
      sql.append("    SUM(BILL_AMT) AS sumBillAmt \n");
      sql.append("FROM RPT_BILL_MAIN rbm \n");
      sql.append("WHERE 0=0 \n");
      sql.append("  AND TRIM(BILL_IDNO) = #{billIdno} \n");
      sql.append("  AND TO_NUMBER(SUBSTR(TO_CHAR(PAYLIMIT), 1, 6)) < TO_NUMBER(#{currentDate}) \n");
      sql.append("  AND TO_NUMBER(SUBSTR(TO_CHAR(TRIM(SUS_DATE1)), 1, 6)) < TO_NUMBER(#{currentDate}) \n");
      sql.append("  AND (TRIM(REMARK) = '0' OR TRIM(REMARK) IS NULL) \n");
      sql.append("  AND (TRIM(TEL_STATUS) IS NOT NULL) \n");
      sql.append("  AND (FALSE_MARK NOT IN ('N', 'J') OR TRIM(FALSE_MARK) IS NULL) \n");
      sql.append("  AND (SPLIT_MARK <> 'Y' OR TRIM(SPLIT_MARK) IS NULL) \n");
      sql.append("  AND (DISB_MARK NOT IN ('F', 'J') OR TRIM(DISB_MARK) IS NULL) \n");
      sql.append("  AND (LOSE_LAWSUIT_MARK <> 'P' OR TRIM(LOSE_LAWSUIT_MARK) IS NULL) \n");
      sql.append("GROUP BY BILL_IDNO \n");
      sql.append("FETCH FIRST 1 ROW ONLY"); // 限制返回一筆資料

      return sql.toString();
    }

    public String selectEMPData(@Param("empId") String empId) {
      StringBuilder sql = new StringBuilder();
      
      sql.append("SELECT \n");
      sql.append("    CNAME AS cname, \n");
      sql.append("    OUCODE1 || OUCODE2 || OUCODE3 || OUCODE4 || OUCODE5 AS oucode \n");
      sql.append("FROM COMM_EMPDATA ce \n");
      sql.append("WHERE EMPID = #{empId} \n");
      sql.append("FETCH FIRST 1 ROW ONLY"); // 限制返回一筆資料

      return sql.toString();
    }
    
  }

  
  
}