package ccbs.dao.core.mapper;

import static ccbs.dao.core.sql.RptBillMainDynamicSqlSupport.*;

import ccbs.dao.core.entity.RptBillMain;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import jakarta.annotation.Generated;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonInsertMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

@Mapper
public interface RptBillMainMapper extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<RptBillMain>, CommonUpdateMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(billIdMark, offAdmin, billOffBelong, billOff, billTel, billMonth, billId, billCycle, billIdno, paylimit, delayDateCnt, comboAccNo, bankNo, paytype, billAmt, taxAmt, saleAmt, zeroTaxAmt, noTaxAmt, freeTaxAmt, prepayAmt, taxPrintId, greed, credit, privateMak, buGroup, rptCustType, remark, remarkTerm, debtMark, debtDate, telStatus, drDate, susCnt, susDate1, susDate2, delayLmtPgm, falseMark, bankMark, receiptNo, invCarrierNo, rateBeginDate, rateEndDate, chgDate, splitMark);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="RptBillMainResult", value = {
        @Result(column="BILL_ID_MARK", property="billIdMark", jdbcType=JdbcType.CHAR),
        @Result(column="OFF_ADMIN", property="offAdmin", jdbcType=JdbcType.CHAR),
        @Result(column="BILL_OFF_BELONG", property="billOffBelong", jdbcType=JdbcType.CHAR),
        @Result(column="BILL_OFF", property="billOff", jdbcType=JdbcType.CHAR),
        @Result(column="BILL_TEL", property="billTel", jdbcType=JdbcType.NVARCHAR),
        @Result(column="BILL_MONTH", property="billMonth", jdbcType=JdbcType.CHAR),
        @Result(column="BILL_ID", property="billId", jdbcType=JdbcType.CHAR),
        @Result(column="BILL_CYCLE", property="billCycle", jdbcType=JdbcType.CHAR),
        @Result(column="BILL_IDNO", property="billIdno", jdbcType=JdbcType.NVARCHAR),
        @Result(column="PAYLIMIT", property="paylimit", jdbcType=JdbcType.NVARCHAR),
        @Result(column="DELAY_DATE_CNT", property="delayDateCnt", jdbcType=JdbcType.NUMERIC),
        @Result(column="COMBO_ACC_NO", property="comboAccNo", jdbcType=JdbcType.NVARCHAR),
        @Result(column="BANK_NO", property="bankNo", jdbcType=JdbcType.NVARCHAR),
        @Result(column="PAYTYPE", property="paytype", jdbcType=JdbcType.CHAR),
        @Result(column="BILL_AMT", property="billAmt", jdbcType=JdbcType.NUMERIC),
        @Result(column="TAX_AMT", property="taxAmt", jdbcType=JdbcType.NUMERIC),
        @Result(column="SALE_AMT", property="saleAmt", jdbcType=JdbcType.NUMERIC),
        @Result(column="ZERO_TAX_AMT", property="zeroTaxAmt", jdbcType=JdbcType.NUMERIC),
        @Result(column="NO_TAX_AMT", property="noTaxAmt", jdbcType=JdbcType.NUMERIC),
        @Result(column="FREE_TAX_AMT", property="freeTaxAmt", jdbcType=JdbcType.NUMERIC),
        @Result(column="PREPAY_AMT", property="prepayAmt", jdbcType=JdbcType.NUMERIC),
        @Result(column="TAX_PRINT_ID", property="taxPrintId", jdbcType=JdbcType.CHAR),
        @Result(column="GREED", property="greed", jdbcType=JdbcType.CHAR),
        @Result(column="CREDIT", property="credit", jdbcType=JdbcType.CHAR),
        @Result(column="PRIVATE_MAK", property="privateMak", jdbcType=JdbcType.CHAR),
        @Result(column="BU_GROUP", property="buGroup", jdbcType=JdbcType.CHAR),
        @Result(column="RPT_CUST_TYPE", property="rptCustType", jdbcType=JdbcType.CHAR),
        @Result(column="REMARK", property="remark", jdbcType=JdbcType.CHAR),
        @Result(column="REMARK_TERM", property="remarkTerm", jdbcType=JdbcType.CHAR),
        @Result(column="DEBT_MARK", property="debtMark", jdbcType=JdbcType.CHAR),
        @Result(column="DEBT_DATE", property="debtDate", jdbcType=JdbcType.NVARCHAR),
        @Result(column="TEL_STATUS", property="telStatus", jdbcType=JdbcType.CHAR),
        @Result(column="DR_DATE", property="drDate", jdbcType=JdbcType.NVARCHAR),
        @Result(column="SUS_CNT", property="susCnt", jdbcType=JdbcType.CHAR),
        @Result(column="SUS_DATE1", property="susDate1", jdbcType=JdbcType.NVARCHAR),
        @Result(column="SUS_DATE2", property="susDate2", jdbcType=JdbcType.NVARCHAR),
        @Result(column="DELAY_LMT_PGM", property="delayLmtPgm", jdbcType=JdbcType.CHAR),
        @Result(column="FALSE_MARK", property="falseMark", jdbcType=JdbcType.CHAR),
        @Result(column="BANK_MARK", property="bankMark", jdbcType=JdbcType.CHAR),
        @Result(column="RECEIPT_NO", property="receiptNo", jdbcType=JdbcType.NVARCHAR),
        @Result(column="INV_CARRIER_NO", property="invCarrierNo", jdbcType=JdbcType.NVARCHAR),
        @Result(column="RATE_BEGIN_DATE", property="rateBeginDate", jdbcType=JdbcType.NVARCHAR),
        @Result(column="RATE_END_DATE", property="rateEndDate", jdbcType=JdbcType.NVARCHAR),
        @Result(column="CHG_DATE", property="chgDate", jdbcType=JdbcType.NVARCHAR),
        @Result(column="SPLIT_MARK", property="splitMark", jdbcType=JdbcType.CHAR)
    })
    List<RptBillMain> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("RptBillMainResult")
    Optional<RptBillMain> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, rptBillMain, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, rptBillMain, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(RptBillMain row) {
        return MyBatis3Utils.insert(this::insert, row, rptBillMain, c ->
            c.map(billIdMark).toProperty("billIdMark")
            .map(offAdmin).toProperty("offAdmin")
            .map(billOffBelong).toProperty("billOffBelong")
            .map(billOff).toProperty("billOff")
            .map(billTel).toProperty("billTel")
            .map(billMonth).toProperty("billMonth")
            .map(billId).toProperty("billId")
            .map(billCycle).toProperty("billCycle")
            .map(billIdno).toProperty("billIdno")
            .map(paylimit).toProperty("paylimit")
            .map(delayDateCnt).toProperty("delayDateCnt")
            .map(comboAccNo).toProperty("comboAccNo")
            .map(bankNo).toProperty("bankNo")
            .map(paytype).toProperty("paytype")
            .map(billAmt).toProperty("billAmt")
            .map(taxAmt).toProperty("taxAmt")
            .map(saleAmt).toProperty("saleAmt")
            .map(zeroTaxAmt).toProperty("zeroTaxAmt")
            .map(noTaxAmt).toProperty("noTaxAmt")
            .map(freeTaxAmt).toProperty("freeTaxAmt")
            .map(prepayAmt).toProperty("prepayAmt")
            .map(taxPrintId).toProperty("taxPrintId")
            .map(greed).toProperty("greed")
            .map(credit).toProperty("credit")
            .map(privateMak).toProperty("privateMak")
            .map(buGroup).toProperty("buGroup")
            .map(rptCustType).toProperty("rptCustType")
            .map(remark).toProperty("remark")
            .map(remarkTerm).toProperty("remarkTerm")
            .map(debtMark).toProperty("debtMark")
            .map(debtDate).toProperty("debtDate")
            .map(telStatus).toProperty("telStatus")
            .map(drDate).toProperty("drDate")
            .map(susCnt).toProperty("susCnt")
            .map(susDate1).toProperty("susDate1")
            .map(susDate2).toProperty("susDate2")
            .map(delayLmtPgm).toProperty("delayLmtPgm")
            .map(falseMark).toProperty("falseMark")
            .map(bankMark).toProperty("bankMark")
            .map(receiptNo).toProperty("receiptNo")
            .map(invCarrierNo).toProperty("invCarrierNo")
            .map(rateBeginDate).toProperty("rateBeginDate")
            .map(rateEndDate).toProperty("rateEndDate")
            .map(chgDate).toProperty("chgDate")
            .map(splitMark).toProperty("splitMark")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<RptBillMain> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, rptBillMain, c ->
            c.map(billIdMark).toProperty("billIdMark")
            .map(offAdmin).toProperty("offAdmin")
            .map(billOffBelong).toProperty("billOffBelong")
            .map(billOff).toProperty("billOff")
            .map(billTel).toProperty("billTel")
            .map(billMonth).toProperty("billMonth")
            .map(billId).toProperty("billId")
            .map(billCycle).toProperty("billCycle")
            .map(billIdno).toProperty("billIdno")
            .map(paylimit).toProperty("paylimit")
            .map(delayDateCnt).toProperty("delayDateCnt")
            .map(comboAccNo).toProperty("comboAccNo")
            .map(bankNo).toProperty("bankNo")
            .map(paytype).toProperty("paytype")
            .map(billAmt).toProperty("billAmt")
            .map(taxAmt).toProperty("taxAmt")
            .map(saleAmt).toProperty("saleAmt")
            .map(zeroTaxAmt).toProperty("zeroTaxAmt")
            .map(noTaxAmt).toProperty("noTaxAmt")
            .map(freeTaxAmt).toProperty("freeTaxAmt")
            .map(prepayAmt).toProperty("prepayAmt")
            .map(taxPrintId).toProperty("taxPrintId")
            .map(greed).toProperty("greed")
            .map(credit).toProperty("credit")
            .map(privateMak).toProperty("privateMak")
            .map(buGroup).toProperty("buGroup")
            .map(rptCustType).toProperty("rptCustType")
            .map(remark).toProperty("remark")
            .map(remarkTerm).toProperty("remarkTerm")
            .map(debtMark).toProperty("debtMark")
            .map(debtDate).toProperty("debtDate")
            .map(telStatus).toProperty("telStatus")
            .map(drDate).toProperty("drDate")
            .map(susCnt).toProperty("susCnt")
            .map(susDate1).toProperty("susDate1")
            .map(susDate2).toProperty("susDate2")
            .map(delayLmtPgm).toProperty("delayLmtPgm")
            .map(falseMark).toProperty("falseMark")
            .map(bankMark).toProperty("bankMark")
            .map(receiptNo).toProperty("receiptNo")
            .map(invCarrierNo).toProperty("invCarrierNo")
            .map(rateBeginDate).toProperty("rateBeginDate")
            .map(rateEndDate).toProperty("rateEndDate")
            .map(chgDate).toProperty("chgDate")
            .map(splitMark).toProperty("splitMark")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(RptBillMain row) {
        return MyBatis3Utils.insert(this::insert, row, rptBillMain, c ->
            c.map(billIdMark).toPropertyWhenPresent("billIdMark", row::getBillIdMark)
            .map(offAdmin).toPropertyWhenPresent("offAdmin", row::getOffAdmin)
            .map(billOffBelong).toPropertyWhenPresent("billOffBelong", row::getBillOffBelong)
            .map(billOff).toPropertyWhenPresent("billOff", row::getBillOff)
            .map(billTel).toPropertyWhenPresent("billTel", row::getBillTel)
            .map(billMonth).toPropertyWhenPresent("billMonth", row::getBillMonth)
            .map(billId).toPropertyWhenPresent("billId", row::getBillId)
            .map(billCycle).toPropertyWhenPresent("billCycle", row::getBillCycle)
            .map(billIdno).toPropertyWhenPresent("billIdno", row::getBillIdno)
            .map(paylimit).toPropertyWhenPresent("paylimit", row::getPaylimit)
            .map(delayDateCnt).toPropertyWhenPresent("delayDateCnt", row::getDelayDateCnt)
            .map(comboAccNo).toPropertyWhenPresent("comboAccNo", row::getComboAccNo)
            .map(bankNo).toPropertyWhenPresent("bankNo", row::getBankNo)
            .map(paytype).toPropertyWhenPresent("paytype", row::getPaytype)
            .map(billAmt).toPropertyWhenPresent("billAmt", row::getBillAmt)
            .map(taxAmt).toPropertyWhenPresent("taxAmt", row::getTaxAmt)
            .map(saleAmt).toPropertyWhenPresent("saleAmt", row::getSaleAmt)
            .map(zeroTaxAmt).toPropertyWhenPresent("zeroTaxAmt", row::getZeroTaxAmt)
            .map(noTaxAmt).toPropertyWhenPresent("noTaxAmt", row::getNoTaxAmt)
            .map(freeTaxAmt).toPropertyWhenPresent("freeTaxAmt", row::getFreeTaxAmt)
            .map(prepayAmt).toPropertyWhenPresent("prepayAmt", row::getPrepayAmt)
            .map(taxPrintId).toPropertyWhenPresent("taxPrintId", row::getTaxPrintId)
            .map(greed).toPropertyWhenPresent("greed", row::getGreed)
            .map(credit).toPropertyWhenPresent("credit", row::getCredit)
            .map(privateMak).toPropertyWhenPresent("privateMak", row::getPrivateMak)
            .map(buGroup).toPropertyWhenPresent("buGroup", row::getBuGroup)
            .map(rptCustType).toPropertyWhenPresent("rptCustType", row::getRptCustType)
            .map(remark).toPropertyWhenPresent("remark", row::getRemark)
            .map(remarkTerm).toPropertyWhenPresent("remarkTerm", row::getRemarkTerm)
            .map(debtMark).toPropertyWhenPresent("debtMark", row::getDebtMark)
            .map(debtDate).toPropertyWhenPresent("debtDate", row::getDebtDate)
            .map(telStatus).toPropertyWhenPresent("telStatus", row::getTelStatus)
            .map(drDate).toPropertyWhenPresent("drDate", row::getDrDate)
            .map(susCnt).toPropertyWhenPresent("susCnt", row::getSusCnt)
            .map(susDate1).toPropertyWhenPresent("susDate1", row::getSusDate1)
            .map(susDate2).toPropertyWhenPresent("susDate2", row::getSusDate2)
            .map(delayLmtPgm).toPropertyWhenPresent("delayLmtPgm", row::getDelayLmtPgm)
            .map(falseMark).toPropertyWhenPresent("falseMark", row::getFalseMark)
            .map(bankMark).toPropertyWhenPresent("bankMark", row::getBankMark)
            .map(receiptNo).toPropertyWhenPresent("receiptNo", row::getReceiptNo)
            .map(invCarrierNo).toPropertyWhenPresent("invCarrierNo", row::getInvCarrierNo)
            .map(rateBeginDate).toPropertyWhenPresent("rateBeginDate", row::getRateBeginDate)
            .map(rateEndDate).toPropertyWhenPresent("rateEndDate", row::getRateEndDate)
            .map(chgDate).toPropertyWhenPresent("chgDate", row::getChgDate)
            .map(splitMark).toPropertyWhenPresent("splitMark", row::getSplitMark)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<RptBillMain> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, rptBillMain, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<RptBillMain> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, rptBillMain, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<RptBillMain> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, rptBillMain, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, rptBillMain, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(RptBillMain row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(billIdMark).equalTo(row::getBillIdMark)
                .set(offAdmin).equalTo(row::getOffAdmin)
                .set(billOffBelong).equalTo(row::getBillOffBelong)
                .set(billOff).equalTo(row::getBillOff)
                .set(billTel).equalTo(row::getBillTel)
                .set(billMonth).equalTo(row::getBillMonth)
                .set(billId).equalTo(row::getBillId)
                .set(billCycle).equalTo(row::getBillCycle)
                .set(billIdno).equalTo(row::getBillIdno)
                .set(paylimit).equalTo(row::getPaylimit)
                .set(delayDateCnt).equalTo(row::getDelayDateCnt)
                .set(comboAccNo).equalTo(row::getComboAccNo)
                .set(bankNo).equalTo(row::getBankNo)
                .set(paytype).equalTo(row::getPaytype)
                .set(billAmt).equalTo(row::getBillAmt)
                .set(taxAmt).equalTo(row::getTaxAmt)
                .set(saleAmt).equalTo(row::getSaleAmt)
                .set(zeroTaxAmt).equalTo(row::getZeroTaxAmt)
                .set(noTaxAmt).equalTo(row::getNoTaxAmt)
                .set(freeTaxAmt).equalTo(row::getFreeTaxAmt)
                .set(prepayAmt).equalTo(row::getPrepayAmt)
                .set(taxPrintId).equalTo(row::getTaxPrintId)
                .set(greed).equalTo(row::getGreed)
                .set(credit).equalTo(row::getCredit)
                .set(privateMak).equalTo(row::getPrivateMak)
                .set(buGroup).equalTo(row::getBuGroup)
                .set(rptCustType).equalTo(row::getRptCustType)
                .set(remark).equalTo(row::getRemark)
                .set(remarkTerm).equalTo(row::getRemarkTerm)
                .set(debtMark).equalTo(row::getDebtMark)
                .set(debtDate).equalTo(row::getDebtDate)
                .set(telStatus).equalTo(row::getTelStatus)
                .set(drDate).equalTo(row::getDrDate)
                .set(susCnt).equalTo(row::getSusCnt)
                .set(susDate1).equalTo(row::getSusDate1)
                .set(susDate2).equalTo(row::getSusDate2)
                .set(delayLmtPgm).equalTo(row::getDelayLmtPgm)
                .set(falseMark).equalTo(row::getFalseMark)
                .set(bankMark).equalTo(row::getBankMark)
                .set(receiptNo).equalTo(row::getReceiptNo)
                .set(invCarrierNo).equalTo(row::getInvCarrierNo)
                .set(rateBeginDate).equalTo(row::getRateBeginDate)
                .set(rateEndDate).equalTo(row::getRateEndDate)
                .set(chgDate).equalTo(row::getChgDate)
                .set(splitMark).equalTo(row::getSplitMark);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(RptBillMain row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(billIdMark).equalToWhenPresent(row::getBillIdMark)
                .set(offAdmin).equalToWhenPresent(row::getOffAdmin)
                .set(billOffBelong).equalToWhenPresent(row::getBillOffBelong)
                .set(billOff).equalToWhenPresent(row::getBillOff)
                .set(billTel).equalToWhenPresent(row::getBillTel)
                .set(billMonth).equalToWhenPresent(row::getBillMonth)
                .set(billId).equalToWhenPresent(row::getBillId)
                .set(billCycle).equalToWhenPresent(row::getBillCycle)
                .set(billIdno).equalToWhenPresent(row::getBillIdno)
                .set(paylimit).equalToWhenPresent(row::getPaylimit)
                .set(delayDateCnt).equalToWhenPresent(row::getDelayDateCnt)
                .set(comboAccNo).equalToWhenPresent(row::getComboAccNo)
                .set(bankNo).equalToWhenPresent(row::getBankNo)
                .set(paytype).equalToWhenPresent(row::getPaytype)
                .set(billAmt).equalToWhenPresent(row::getBillAmt)
                .set(taxAmt).equalToWhenPresent(row::getTaxAmt)
                .set(saleAmt).equalToWhenPresent(row::getSaleAmt)
                .set(zeroTaxAmt).equalToWhenPresent(row::getZeroTaxAmt)
                .set(noTaxAmt).equalToWhenPresent(row::getNoTaxAmt)
                .set(freeTaxAmt).equalToWhenPresent(row::getFreeTaxAmt)
                .set(prepayAmt).equalToWhenPresent(row::getPrepayAmt)
                .set(taxPrintId).equalToWhenPresent(row::getTaxPrintId)
                .set(greed).equalToWhenPresent(row::getGreed)
                .set(credit).equalToWhenPresent(row::getCredit)
                .set(privateMak).equalToWhenPresent(row::getPrivateMak)
                .set(buGroup).equalToWhenPresent(row::getBuGroup)
                .set(rptCustType).equalToWhenPresent(row::getRptCustType)
                .set(remark).equalToWhenPresent(row::getRemark)
                .set(remarkTerm).equalToWhenPresent(row::getRemarkTerm)
                .set(debtMark).equalToWhenPresent(row::getDebtMark)
                .set(debtDate).equalToWhenPresent(row::getDebtDate)
                .set(telStatus).equalToWhenPresent(row::getTelStatus)
                .set(drDate).equalToWhenPresent(row::getDrDate)
                .set(susCnt).equalToWhenPresent(row::getSusCnt)
                .set(susDate1).equalToWhenPresent(row::getSusDate1)
                .set(susDate2).equalToWhenPresent(row::getSusDate2)
                .set(delayLmtPgm).equalToWhenPresent(row::getDelayLmtPgm)
                .set(falseMark).equalToWhenPresent(row::getFalseMark)
                .set(bankMark).equalToWhenPresent(row::getBankMark)
                .set(receiptNo).equalToWhenPresent(row::getReceiptNo)
                .set(invCarrierNo).equalToWhenPresent(row::getInvCarrierNo)
                .set(rateBeginDate).equalToWhenPresent(row::getRateBeginDate)
                .set(rateEndDate).equalToWhenPresent(row::getRateEndDate)
                .set(chgDate).equalToWhenPresent(row::getChgDate)
                .set(splitMark).equalToWhenPresent(row::getSplitMark);
    }
}