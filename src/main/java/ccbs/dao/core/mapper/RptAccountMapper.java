package ccbs.dao.core.mapper;

import static ccbs.dao.core.sql.RptAccountDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

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

import ccbs.dao.core.entity.RptAccount;

@Mapper
public interface RptAccountMapper extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<RptAccount>, CommonUpdateMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(
        billOffBelong, billOff, billMonth, debtMark, taxPrintId, rptCustType, 
        buGroupMark, accItem, rcvItem, billItemCode, billItemAmt, billItemTaxAmt, 
        billItemSaleAmt, billItemZeroTaxAmt, billItemNoTaxAmt, billItemFreeTaxAmt
    );

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="RptAccountResult", value = {
        @Result(column="BILL_OFF_BELONG", property="billOffBelong", jdbcType=JdbcType.VARCHAR),
        @Result(column="BILL_OFF", property="billOff", jdbcType=JdbcType.VARCHAR),
        @Result(column="BILL_MONTH", property="billMonth", jdbcType=JdbcType.VARCHAR),
        @Result(column="DEBT_MARK", property="debtMark", jdbcType=JdbcType.VARCHAR),
        @Result(column="TAX_PRINT_ID", property="taxPrintId", jdbcType=JdbcType.VARCHAR),
        @Result(column="RPT_CUST_TYPE", property="rptCustType", jdbcType=JdbcType.VARCHAR),
        @Result(column="BU_GROUP_MARK", property="buGroupMark", jdbcType=JdbcType.VARCHAR),
        @Result(column="ACC_ITEM", property="accItem", jdbcType=JdbcType.VARCHAR),
        @Result(column="RCV_ITEM", property="rcvItem", jdbcType=JdbcType.VARCHAR),
        @Result(column="BILL_ITEM_CODE", property="billItemCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="BILL_ITEM_AMT", property="billItemAmt", jdbcType=JdbcType.BIGINT),
        @Result(column="BILL_ITEM_TAX_AMT", property="billItemTaxAmt", jdbcType=JdbcType.BIGINT),
        @Result(column="BILL_ITEM_SALE_AMT", property="billItemSaleAmt", jdbcType=JdbcType.BIGINT),
        @Result(column="BILL_ITEM_ZERO_TAX_AMT", property="billItemZeroTaxAmt", jdbcType=JdbcType.BIGINT),
        @Result(column="BILL_ITEM_NO_TAX_AMT", property="billItemNoTaxAmt", jdbcType=JdbcType.BIGINT),
        @Result(column="BILL_ITEM_FREE_TAX_AMT", property="billItemFreeTaxAmt", jdbcType=JdbcType.BIGINT)
    })
    List<RptAccount> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("RptAccountResult")
    Optional<RptAccount> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, rptAccount, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, rptAccount, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(String billOffBelong_) {
        return delete(c -> 
            c.where(billOffBelong, isEqualTo(billOffBelong_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(RptAccount row) {
        return MyBatis3Utils.insert(this::insert, row, rptAccount, c ->
            c.map(billOffBelong).toProperty("billOffBelong")
            .map(billOff).toProperty("billOff")
            .map(billMonth).toProperty("billMonth")
            .map(debtMark).toProperty("debtMark")
            .map(taxPrintId).toProperty("taxPrintId")
            .map(rptCustType).toProperty("rptCustType")
            .map(buGroupMark).toProperty("buGroupMark")
            .map(accItem).toProperty("accItem")
            .map(rcvItem).toProperty("rcvItem")
            .map(billItemCode).toProperty("billItemCode")
            .map(billItemAmt).toProperty("billItemAmt")
            .map(billItemTaxAmt).toProperty("billItemTaxAmt")
            .map(billItemSaleAmt).toProperty("billItemSaleAmt")
            .map(billItemZeroTaxAmt).toProperty("billItemZeroTaxAmt")
            .map(billItemNoTaxAmt).toProperty("billItemNoTaxAmt")
            .map(billItemFreeTaxAmt).toProperty("billItemFreeTaxAmt")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<RptAccount> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, rptAccount, c ->
            c.map(billOffBelong).toProperty("billOffBelong")
            .map(billOff).toProperty("billOff")
            .map(billMonth).toProperty("billMonth")
            .map(debtMark).toProperty("debtMark")
            .map(taxPrintId).toProperty("taxPrintId")
            .map(rptCustType).toProperty("rptCustType")
            .map(buGroupMark).toProperty("buGroupMark")
            .map(accItem).toProperty("accItem")
            .map(rcvItem).toProperty("rcvItem")
            .map(billItemCode).toProperty("billItemCode")
            .map(billItemAmt).toProperty("billItemAmt")
            .map(billItemTaxAmt).toProperty("billItemTaxAmt")
            .map(billItemSaleAmt).toProperty("billItemSaleAmt")
            .map(billItemZeroTaxAmt).toProperty("billItemZeroTaxAmt")
            .map(billItemNoTaxAmt).toProperty("billItemNoTaxAmt")
            .map(billItemFreeTaxAmt).toProperty("billItemFreeTaxAmt")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(RptAccount row) {
        return MyBatis3Utils.insert(this::insert, row, rptAccount, c ->
            c.map(billOffBelong).toPropertyWhenPresent("billOffBelong", row::getBillOffBelong)
            .map(billOff).toPropertyWhenPresent("billOff", row::getBillOff)
            .map(billMonth).toPropertyWhenPresent("billMonth", row::getBillMonth)
            .map(debtMark).toPropertyWhenPresent("debtMark", row::getDebtMark)
            .map(taxPrintId).toPropertyWhenPresent("taxPrintId", row::getTaxPrintId)
            .map(rptCustType).toPropertyWhenPresent("rptCustType", row::getRptCustType)
            .map(buGroupMark).toPropertyWhenPresent("buGroupMark", row::getBuGroupMark)
            .map(accItem).toPropertyWhenPresent("accItem", row::getAccItem)
            .map(rcvItem).toPropertyWhenPresent("rcvItem", row::getRcvItem)
            .map(billItemCode).toPropertyWhenPresent("billItemCode", row::getBillItemCode)
            .map(billItemAmt).toPropertyWhenPresent("billItemAmt", row::getBillItemAmt)
            .map(billItemTaxAmt).toPropertyWhenPresent("billItemTaxAmt", row::getBillItemTaxAmt)
            .map(billItemSaleAmt).toPropertyWhenPresent("billItemSaleAmt", row::getBillItemSaleAmt)
            .map(billItemZeroTaxAmt).toPropertyWhenPresent("billItemZeroTaxAmt", row::getBillItemZeroTaxAmt)
            .map(billItemNoTaxAmt).toPropertyWhenPresent("billItemNoTaxAmt", row::getBillItemNoTaxAmt)
            .map(billItemFreeTaxAmt).toPropertyWhenPresent("billItemFreeTaxAmt", row::getBillItemFreeTaxAmt)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<RptAccount> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, rptAccount, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<RptAccount> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, rptAccount, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<RptAccount> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, rptAccount, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<RptAccount> selectByPrimaryKey(String billOffBelong_) {
        return selectOne(c ->
            c.where(billOffBelong, isEqualTo(billOffBelong_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, rptAccount, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(RptAccount row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(billOffBelong).equalTo(row::getBillOffBelong)
                .set(billOff).equalTo(row::getBillOff)
                .set(billMonth).equalTo(row::getBillMonth)
                .set(debtMark).equalTo(row::getDebtMark)
                .set(taxPrintId).equalTo(row::getTaxPrintId)
                .set(rptCustType).equalTo(row::getRptCustType)
                .set(buGroupMark).equalTo(row::getBuGroupMark)
                .set(accItem).equalTo(row::getAccItem)
                .set(rcvItem).equalTo(row::getRcvItem)
                .set(billItemCode).equalTo(row::getBillItemCode)
                .set(billItemAmt).equalTo(row::getBillItemAmt)
                .set(billItemTaxAmt).equalTo(row::getBillItemTaxAmt)
                .set(billItemSaleAmt).equalTo(row::getBillItemSaleAmt)
                .set(billItemZeroTaxAmt).equalTo(row::getBillItemZeroTaxAmt)
                .set(billItemNoTaxAmt).equalTo(row::getBillItemNoTaxAmt)
                .set(billItemFreeTaxAmt).equalTo(row::getBillItemFreeTaxAmt);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(RptAccount row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(billOffBelong).equalToWhenPresent(row::getBillOffBelong)
                .set(billOff).equalToWhenPresent(row::getBillOff)
                .set(billMonth).equalToWhenPresent(row::getBillMonth)
                .set(debtMark).equalToWhenPresent(row::getDebtMark)
                .set(taxPrintId).equalToWhenPresent(row::getTaxPrintId)
                .set(rptCustType).equalToWhenPresent(row::getRptCustType)
                .set(buGroupMark).equalToWhenPresent(row::getBuGroupMark)
                .set(accItem).equalToWhenPresent(row::getAccItem)
                .set(rcvItem).equalToWhenPresent(row::getRcvItem)
                .set(billItemCode).equalToWhenPresent(row::getBillItemCode)
                .set(billItemAmt).equalToWhenPresent(row::getBillItemAmt)
                .set(billItemTaxAmt).equalToWhenPresent(row::getBillItemTaxAmt)
                .set(billItemSaleAmt).equalToWhenPresent(row::getBillItemSaleAmt)
                .set(billItemZeroTaxAmt).equalToWhenPresent(row::getBillItemZeroTaxAmt)
                .set(billItemNoTaxAmt).equalToWhenPresent(row::getBillItemNoTaxAmt)
                .set(billItemFreeTaxAmt).equalToWhenPresent(row::getBillItemFreeTaxAmt);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(RptAccount row) {
        return update(c ->
            c.set(billOff).equalTo(row::getBillOff)
            .set(billMonth).equalTo(row::getBillMonth)
            .set(debtMark).equalTo(row::getDebtMark)
            .set(taxPrintId).equalTo(row::getTaxPrintId)
            .set(rptCustType).equalTo(row::getRptCustType)
            .set(buGroupMark).equalTo(row::getBuGroupMark)
            .set(accItem).equalTo(row::getAccItem)
            .set(rcvItem).equalTo(row::getRcvItem)
            .set(billItemCode).equalTo(row::getBillItemCode)
            .set(billItemAmt).equalTo(row::getBillItemAmt)
            .set(billItemTaxAmt).equalTo(row::getBillItemTaxAmt)
            .set(billItemSaleAmt).equalTo(row::getBillItemSaleAmt)
            .set(billItemZeroTaxAmt).equalTo(row::getBillItemZeroTaxAmt)
            .set(billItemNoTaxAmt).equalTo(row::getBillItemNoTaxAmt)
            .set(billItemFreeTaxAmt).equalTo(row::getBillItemFreeTaxAmt)
            .where(billOffBelong, isEqualTo(row::getBillOffBelong))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(RptAccount row) {
        return update(c ->
            c.set(billOff).equalToWhenPresent(row::getBillOff)
            .set(billMonth).equalToWhenPresent(row::getBillMonth)
            .set(debtMark).equalToWhenPresent(row::getDebtMark)
            .set(taxPrintId).equalToWhenPresent(row::getTaxPrintId)
            .set(rptCustType).equalToWhenPresent(row::getRptCustType)
            .set(buGroupMark).equalToWhenPresent(row::getBuGroupMark)
            .set(accItem).equalToWhenPresent(row::getAccItem)
            .set(rcvItem).equalToWhenPresent(row::getRcvItem)
            .set(billItemCode).equalToWhenPresent(row::getBillItemCode)
            .set(billItemAmt).equalToWhenPresent(row::getBillItemAmt)
            .set(billItemTaxAmt).equalToWhenPresent(row::getBillItemTaxAmt)
            .set(billItemSaleAmt).equalToWhenPresent(row::getBillItemSaleAmt)
            .set(billItemZeroTaxAmt).equalToWhenPresent(row::getBillItemZeroTaxAmt)
            .set(billItemNoTaxAmt).equalToWhenPresent(row::getBillItemNoTaxAmt)
            .set(billItemFreeTaxAmt).equalToWhenPresent(row::getBillItemFreeTaxAmt)
            .where(billOffBelong, isEqualTo(row::getBillOffBelong))
        );
    }
}