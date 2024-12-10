package ccbs.dao.core.mapper;

import static ccbs.dao.core.sql.RptItemTypeDetlDynamicSqlSupport.*;
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

import ccbs.dao.core.entity.RptItemTypeDetl;

@Mapper
public interface RptItemTypeDetlMapper extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<RptItemTypeDetl>, CommonUpdateMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(
        billItemCode, billItemType
    );

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="RptItemTypeDetlResult", value = {
        @Result(column="BILL_ITEM_CODE", property="billItemCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="BILL_ITEM_TYPE", property="billItemType", jdbcType=JdbcType.VARCHAR)
    })
    List<RptItemTypeDetl> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("RptItemTypeDetlResult")
    Optional<RptItemTypeDetl> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, rptItemTypeDetl, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, rptItemTypeDetl, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(String billItemCode_) {
        return delete(c -> 
            c.where(billItemCode, isEqualTo(billItemCode_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(RptItemTypeDetl row) {
        return MyBatis3Utils.insert(this::insert, row, rptItemTypeDetl, c ->
            c.map(billItemCode).toProperty("billItemCode")
            .map(billItemType).toProperty("billItemType")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<RptItemTypeDetl> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, rptItemTypeDetl, c ->
            c.map(billItemCode).toProperty("billItemCode")
            .map(billItemType).toProperty("billItemType")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(RptItemTypeDetl row) {
        return MyBatis3Utils.insert(this::insert, row, rptItemTypeDetl, c ->
            c.map(billItemCode).toPropertyWhenPresent("billItemCode", row::getBillItemCode)
            .map(billItemType).toPropertyWhenPresent("billItemType", row::getBillItemType)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<RptItemTypeDetl> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, rptItemTypeDetl, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<RptItemTypeDetl> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, rptItemTypeDetl, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<RptItemTypeDetl> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, rptItemTypeDetl, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<RptItemTypeDetl> selectByPrimaryKey(String billItemCode_) {
        return selectOne(c ->
            c.where(billItemCode, isEqualTo(billItemCode_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, rptItemTypeDetl, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(RptItemTypeDetl row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(billItemCode).equalTo(row::getBillItemCode)
                .set(billItemType).equalTo(row::getBillItemType);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(RptItemTypeDetl row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(billItemCode).equalToWhenPresent(row::getBillItemCode)
                .set(billItemType).equalToWhenPresent(row::getBillItemType);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(RptItemTypeDetl row) {
        return update(c ->
            c.set(billItemType).equalTo(row::getBillItemType)
            .where(billItemCode, isEqualTo(row::getBillItemCode))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(RptItemTypeDetl row) {
        return update(c ->
            c.set(billItemType).equalToWhenPresent(row::getBillItemType)
            .where(billItemCode, isEqualTo(row::getBillItemCode))
        );
    }
}