package ccbs.dao.core.mapper;

import static ccbs.dao.core.sql.BillRelsDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import ccbs.dao.core.entity.BillRels;
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
public interface BillRelsMapper extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<BillRels>, CommonUpdateMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(billOff, billTel, billMonth, billId, subOff, subTel, subMonth, subId, billOffBelong, billTela, billAmt, subTela, subAmt);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="BillRelsResult", value = {
        @Result(column="BILL_OFF", property="billOff", jdbcType=JdbcType.CHAR, id=true),
        @Result(column="BILL_TEL", property="billTel", jdbcType=JdbcType.NVARCHAR, id=true),
        @Result(column="BILL_MONTH", property="billMonth", jdbcType=JdbcType.CHAR, id=true),
        @Result(column="BILL_ID", property="billId", jdbcType=JdbcType.CHAR, id=true),
        @Result(column="SUB_OFF", property="subOff", jdbcType=JdbcType.CHAR, id=true),
        @Result(column="SUB_TEL", property="subTel", jdbcType=JdbcType.NVARCHAR, id=true),
        @Result(column="SUB_MONTH", property="subMonth", jdbcType=JdbcType.CHAR, id=true),
        @Result(column="SUB_ID", property="subId", jdbcType=JdbcType.CHAR, id=true),
        @Result(column="BILL_OFF_BELONG", property="billOffBelong", jdbcType=JdbcType.CHAR),
        @Result(column="BILL_TELA", property="billTela", jdbcType=JdbcType.NVARCHAR),
        @Result(column="BILL_AMT", property="billAmt", jdbcType=JdbcType.NUMERIC),
        @Result(column="SUB_TELA", property="subTela", jdbcType=JdbcType.NVARCHAR),
        @Result(column="SUB_AMT", property="subAmt", jdbcType=JdbcType.NUMERIC)
    })
    List<BillRels> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("BillRelsResult")
    Optional<BillRels> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, billRels, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, billRels, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(String billOff_, String billTel_, String billMonth_, String billId_, String subOff_, String subTel_, String subMonth_, String subId_) {
        return delete(c -> 
            c.where(billOff, isEqualTo(billOff_))
            .and(billTel, isEqualTo(billTel_))
            .and(billMonth, isEqualTo(billMonth_))
            .and(billId, isEqualTo(billId_))
            .and(subOff, isEqualTo(subOff_))
            .and(subTel, isEqualTo(subTel_))
            .and(subMonth, isEqualTo(subMonth_))
            .and(subId, isEqualTo(subId_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(BillRels row) {
        return MyBatis3Utils.insert(this::insert, row, billRels, c ->
            c.map(billOff).toProperty("billOff")
            .map(billTel).toProperty("billTel")
            .map(billMonth).toProperty("billMonth")
            .map(billId).toProperty("billId")
            .map(subOff).toProperty("subOff")
            .map(subTel).toProperty("subTel")
            .map(subMonth).toProperty("subMonth")
            .map(subId).toProperty("subId")
            .map(billOffBelong).toProperty("billOffBelong")
            .map(billTela).toProperty("billTela")
            .map(billAmt).toProperty("billAmt")
            .map(subTela).toProperty("subTela")
            .map(subAmt).toProperty("subAmt")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<BillRels> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, billRels, c ->
            c.map(billOff).toProperty("billOff")
            .map(billTel).toProperty("billTel")
            .map(billMonth).toProperty("billMonth")
            .map(billId).toProperty("billId")
            .map(subOff).toProperty("subOff")
            .map(subTel).toProperty("subTel")
            .map(subMonth).toProperty("subMonth")
            .map(subId).toProperty("subId")
            .map(billOffBelong).toProperty("billOffBelong")
            .map(billTela).toProperty("billTela")
            .map(billAmt).toProperty("billAmt")
            .map(subTela).toProperty("subTela")
            .map(subAmt).toProperty("subAmt")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(BillRels row) {
        return MyBatis3Utils.insert(this::insert, row, billRels, c ->
            c.map(billOff).toPropertyWhenPresent("billOff", row::getBillOff)
            .map(billTel).toPropertyWhenPresent("billTel", row::getBillTel)
            .map(billMonth).toPropertyWhenPresent("billMonth", row::getBillMonth)
            .map(billId).toPropertyWhenPresent("billId", row::getBillId)
            .map(subOff).toPropertyWhenPresent("subOff", row::getSubOff)
            .map(subTel).toPropertyWhenPresent("subTel", row::getSubTel)
            .map(subMonth).toPropertyWhenPresent("subMonth", row::getSubMonth)
            .map(subId).toPropertyWhenPresent("subId", row::getSubId)
            .map(billOffBelong).toPropertyWhenPresent("billOffBelong", row::getBillOffBelong)
            .map(billTela).toPropertyWhenPresent("billTela", row::getBillTela)
            .map(billAmt).toPropertyWhenPresent("billAmt", row::getBillAmt)
            .map(subTela).toPropertyWhenPresent("subTela", row::getSubTela)
            .map(subAmt).toPropertyWhenPresent("subAmt", row::getSubAmt)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<BillRels> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, billRels, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<BillRels> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, billRels, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<BillRels> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, billRels, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<BillRels> selectByPrimaryKey(String billOff_, String billTel_, String billMonth_, String billId_, String subOff_, String subTel_, String subMonth_, String subId_) {
        return selectOne(c ->
            c.where(billOff, isEqualTo(billOff_))
            .and(billTel, isEqualTo(billTel_))
            .and(billMonth, isEqualTo(billMonth_))
            .and(billId, isEqualTo(billId_))
            .and(subOff, isEqualTo(subOff_))
            .and(subTel, isEqualTo(subTel_))
            .and(subMonth, isEqualTo(subMonth_))
            .and(subId, isEqualTo(subId_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, billRels, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(BillRels row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(billOff).equalTo(row::getBillOff)
                .set(billTel).equalTo(row::getBillTel)
                .set(billMonth).equalTo(row::getBillMonth)
                .set(billId).equalTo(row::getBillId)
                .set(subOff).equalTo(row::getSubOff)
                .set(subTel).equalTo(row::getSubTel)
                .set(subMonth).equalTo(row::getSubMonth)
                .set(subId).equalTo(row::getSubId)
                .set(billOffBelong).equalTo(row::getBillOffBelong)
                .set(billTela).equalTo(row::getBillTela)
                .set(billAmt).equalTo(row::getBillAmt)
                .set(subTela).equalTo(row::getSubTela)
                .set(subAmt).equalTo(row::getSubAmt);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(BillRels row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(billOff).equalToWhenPresent(row::getBillOff)
                .set(billTel).equalToWhenPresent(row::getBillTel)
                .set(billMonth).equalToWhenPresent(row::getBillMonth)
                .set(billId).equalToWhenPresent(row::getBillId)
                .set(subOff).equalToWhenPresent(row::getSubOff)
                .set(subTel).equalToWhenPresent(row::getSubTel)
                .set(subMonth).equalToWhenPresent(row::getSubMonth)
                .set(subId).equalToWhenPresent(row::getSubId)
                .set(billOffBelong).equalToWhenPresent(row::getBillOffBelong)
                .set(billTela).equalToWhenPresent(row::getBillTela)
                .set(billAmt).equalToWhenPresent(row::getBillAmt)
                .set(subTela).equalToWhenPresent(row::getSubTela)
                .set(subAmt).equalToWhenPresent(row::getSubAmt);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(BillRels row) {
        return update(c ->
            c.set(billOffBelong).equalTo(row::getBillOffBelong)
            .set(billTela).equalTo(row::getBillTela)
            .set(billAmt).equalTo(row::getBillAmt)
            .set(subTela).equalTo(row::getSubTela)
            .set(subAmt).equalTo(row::getSubAmt)
            .where(billOff, isEqualTo(row::getBillOff))
            .and(billTel, isEqualTo(row::getBillTel))
            .and(billMonth, isEqualTo(row::getBillMonth))
            .and(billId, isEqualTo(row::getBillId))
            .and(subOff, isEqualTo(row::getSubOff))
            .and(subTel, isEqualTo(row::getSubTel))
            .and(subMonth, isEqualTo(row::getSubMonth))
            .and(subId, isEqualTo(row::getSubId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(BillRels row) {
        return update(c ->
            c.set(billOffBelong).equalToWhenPresent(row::getBillOffBelong)
            .set(billTela).equalToWhenPresent(row::getBillTela)
            .set(billAmt).equalToWhenPresent(row::getBillAmt)
            .set(subTela).equalToWhenPresent(row::getSubTela)
            .set(subAmt).equalToWhenPresent(row::getSubAmt)
            .where(billOff, isEqualTo(row::getBillOff))
            .and(billTel, isEqualTo(row::getBillTel))
            .and(billMonth, isEqualTo(row::getBillMonth))
            .and(billId, isEqualTo(row::getBillId))
            .and(subOff, isEqualTo(row::getSubOff))
            .and(subTel, isEqualTo(row::getSubTel))
            .and(subMonth, isEqualTo(row::getSubMonth))
            .and(subId, isEqualTo(row::getSubId))
        );
    }
}