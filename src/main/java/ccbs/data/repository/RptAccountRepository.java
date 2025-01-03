package ccbs.data.repository;

import ccbs.data.entity.RptAccount;
import ccbs.data.entity.Rqbp017Dto;
import ccbs.model.bp01.Bp01f0003DTO;
import ccbs.model.bp01.Bp01f0013DTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RptAccountRepository extends JpaRepository<RptAccount, Long> {
  public Page<RptAccount> findByBillMonth(String billMonth, Pageable pageable);

  @EntityGraph(value = "RptAccount.withItemType", type = EntityGraph.EntityGraphType.LOAD)
  public Page<RptAccount> findWithItemTypeByBillMonth(String billMonth, Pageable pageable);

  /*
    SELECT
      DETL.BILL_ITEM_TYPE,
      RAC.BILL_OFF_BELONG,
      RAC.RPT_CUST_TYPE,
      SUM(RAC.BILL_ITEM_AMT),
      COUNT(*)
    FROM
      RPT_ACCOUNT RAC
    JOIN RPT_ITEM_TYPE_DETL DETL ON
      RAC.BILL_ITEM_CODE = DETL.BILL_ITEM_CODE
    WHERE
      RAC.BILL_MONTH = :billMonth
    GROUP BY
      RAC.BILL_OFF_BELONG,
      DETL.BILL_ITEM_TYPE,
      RAC.RPT_CUST_TYPE
    ORDER BY
      TO_NUMBER(RAC.BILL_OFF_BELONG),
      DETL.BILL_ITEM_TYPE,
      RAC.RPT_CUST_TYPE;
   */
  @Query(
      "SELECT new ccbs.model.bp01.Bp01f0003DTO(ac.billOffBelong, ac.rptCustType, ac.rptItemTypeDetl.billItemType, SUM(ac.billItemAmt), COUNT(*)) "
      + "FROM RptAccount ac "
      + "WHERE ac.billMonth = :billMonth "
      + "GROUP BY ac.billOffBelong, ac.rptCustType, ac.rptItemTypeDetl.billItemType "
      + "ORDER BY TO_NUMBER(ac.billOffBelong), ac.rptCustType, ac.rptItemTypeDetl.billItemType ")
  public List<Bp01f0003DTO>
  summaryBusinessODArrears(@Param("billMonth") String billMonth);

  /*
    SELECT
      RAC.ACC_ITEM,
      SUBSTR(RAC.BILL_MONTH,0,3) BILL_MONTH,
      AC.ACC_NAME,
      SUM(RAC.BILL_ITEM_AMT),
      COUNT(*)
    FROM
      RPT_ACCOUNT RAC
    LEFT JOIN
    (
      SELECT
        CONCAT(CONCAT(ACC_TYPE, '-'), ACC_CODE) ACC_ITEM,
        ACC_NAME
      FROM
        ACCOUNTING) AC
    ON
      RAC.ACC_ITEM = AC.ACC_ITEM
    WHERE
      SUBSTR(RAC.BILL_MONTH, 0, 3) BETWEEN :startBillYear AND :endBillYear
    GROUP BY
      RAC.ACC_ITEM,
      AC.ACC_NAME,
      SUBSTR(RAC.BILL_MONTH,0,3)
    ORDER BY
      RAC.ACC_ITEM,
      AC.ACC_NAME,
      SUBSTR(RAC.BILL_MONTH,0,3) DESC;
  */
  @Query(
      "SELECT new ccbs.model.bp01.Bp01f0013DTO(ac.accounting.accItem, ac.accounting.accName, SUBSTR(ac.billMonth,0,3), SUM(ac.billItemAmt), COUNT(*)) "
      + "FROM RptAccount ac "
      + "WHERE SUBSTR(ac.billMonth,0,3) BETWEEN :startBillYear AND :endBillYear "
      + "GROUP BY ac.accounting.accItem, ac.accounting.accName, SUBSTR(ac.billMonth,0,3) "
      + "ORDER BY ac.accounting.accItem, ac.accounting.accName, SUBSTR(ac.billMonth,0,3) DESC ")
  List<Bp01f0013DTO>
  analysisOutstandingArrears(
      @Param("startBillYear") String startBillYear, @Param("endBillYear") String endBillYear);

  @Query(name = "AnalysisReceivableArrears", nativeQuery = true)
  public List<Rqbp017Dto> analysisReceivableArrears(
      @Param("startBillYear") String startBillYear, @Param("endBillYear") String endBillYear);
}