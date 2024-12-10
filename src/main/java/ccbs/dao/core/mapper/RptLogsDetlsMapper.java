package ccbs.dao.core.mapper;

import static ccbs.dao.core.sql.RptLogsDetlsDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import ccbs.dao.core.entity.RptLogsDetls;
import java.math.BigDecimal;
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
public interface RptLogsDetlsMapper extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<RptLogsDetls>, CommonUpdateMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(rptFileName, rptLogsId, billOff, rptTimes, billMonth, billCycle, rptDate, rptQuarter, offAdmin, rptFilePath, rptFileCount, rptFileAmt, rptSecretMark, rptLogsStatus);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="RptLogsDetlsResult", value = {
        @Result(column="RPT_FILE_NAME", property="rptFileName", jdbcType=JdbcType.NVARCHAR, id=true),
        @Result(column="RPT_LOGS_ID", property="rptLogsId", jdbcType=JdbcType.NUMERIC, id=true),
        @Result(column="BILL_OFF", property="billOff", jdbcType=JdbcType.CHAR),
        @Result(column="RPT_TIMES", property="rptTimes", jdbcType=JdbcType.CHAR),
        @Result(column="BILL_MONTH", property="billMonth", jdbcType=JdbcType.CHAR),
        @Result(column="BILL_CYCLE", property="billCycle", jdbcType=JdbcType.CHAR),
        @Result(column="RPT_DATE", property="rptDate", jdbcType=JdbcType.NVARCHAR),
        @Result(column="RPT_QUARTER", property="rptQuarter", jdbcType=JdbcType.CHAR),
        @Result(column="OFF_ADMIN", property="offAdmin", jdbcType=JdbcType.CHAR),
        @Result(column="RPT_FILE_PATH", property="rptFilePath", jdbcType=JdbcType.NVARCHAR),
        @Result(column="RPT_FILE_COUNT", property="rptFileCount", jdbcType=JdbcType.NUMERIC),
        @Result(column="RPT_FILE_AMT", property="rptFileAmt", jdbcType=JdbcType.NUMERIC),
        @Result(column="RPT_SECRET_MARK", property="rptSecretMark", jdbcType=JdbcType.CHAR),
        @Result(column="RPT_LOGS_STATUS", property="rptLogsStatus", jdbcType=JdbcType.CHAR)
    })
    List<RptLogsDetls> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("RptLogsDetlsResult")
    Optional<RptLogsDetls> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, rptLogsDetls, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, rptLogsDetls, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(String rptFileName_, BigDecimal rptLogsId_) {
        return delete(c -> 
            c.where(rptFileName, isEqualTo(rptFileName_))
            .and(rptLogsId, isEqualTo(rptLogsId_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(RptLogsDetls row) {
        return MyBatis3Utils.insert(this::insert, row, rptLogsDetls, c ->
            c.map(rptFileName).toProperty("rptFileName")
            .map(rptLogsId).toProperty("rptLogsId")
            .map(billOff).toProperty("billOff")
            .map(rptTimes).toProperty("rptTimes")
            .map(billMonth).toProperty("billMonth")
            .map(billCycle).toProperty("billCycle")
            .map(rptDate).toProperty("rptDate")
            .map(rptQuarter).toProperty("rptQuarter")
            .map(offAdmin).toProperty("offAdmin")
            .map(rptFilePath).toProperty("rptFilePath")
            .map(rptFileCount).toProperty("rptFileCount")
            .map(rptFileAmt).toProperty("rptFileAmt")
            .map(rptSecretMark).toProperty("rptSecretMark")
            .map(rptLogsStatus).toProperty("rptLogsStatus")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<RptLogsDetls> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, rptLogsDetls, c ->
            c.map(rptFileName).toProperty("rptFileName")
            .map(rptLogsId).toProperty("rptLogsId")
            .map(billOff).toProperty("billOff")
            .map(rptTimes).toProperty("rptTimes")
            .map(billMonth).toProperty("billMonth")
            .map(billCycle).toProperty("billCycle")
            .map(rptDate).toProperty("rptDate")
            .map(rptQuarter).toProperty("rptQuarter")
            .map(offAdmin).toProperty("offAdmin")
            .map(rptFilePath).toProperty("rptFilePath")
            .map(rptFileCount).toProperty("rptFileCount")
            .map(rptFileAmt).toProperty("rptFileAmt")
            .map(rptSecretMark).toProperty("rptSecretMark")
            .map(rptLogsStatus).toProperty("rptLogsStatus")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(RptLogsDetls row) {
        return MyBatis3Utils.insert(this::insert, row, rptLogsDetls, c ->
            c.map(rptFileName).toPropertyWhenPresent("rptFileName", row::getRptFileName)
            .map(rptLogsId).toPropertyWhenPresent("rptLogsId", row::getRptLogsId)
            .map(billOff).toPropertyWhenPresent("billOff", row::getBillOff)
            .map(rptTimes).toPropertyWhenPresent("rptTimes", row::getRptTimes)
            .map(billMonth).toPropertyWhenPresent("billMonth", row::getBillMonth)
            .map(billCycle).toPropertyWhenPresent("billCycle", row::getBillCycle)
            .map(rptDate).toPropertyWhenPresent("rptDate", row::getRptDate)
            .map(rptQuarter).toPropertyWhenPresent("rptQuarter", row::getRptQuarter)
            .map(offAdmin).toPropertyWhenPresent("offAdmin", row::getOffAdmin)
            .map(rptFilePath).toPropertyWhenPresent("rptFilePath", row::getRptFilePath)
            .map(rptFileCount).toPropertyWhenPresent("rptFileCount", row::getRptFileCount)
            .map(rptFileAmt).toPropertyWhenPresent("rptFileAmt", row::getRptFileAmt)
            .map(rptSecretMark).toPropertyWhenPresent("rptSecretMark", row::getRptSecretMark)
            .map(rptLogsStatus).toPropertyWhenPresent("rptLogsStatus", row::getRptLogsStatus)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<RptLogsDetls> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, rptLogsDetls, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<RptLogsDetls> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, rptLogsDetls, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<RptLogsDetls> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, rptLogsDetls, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<RptLogsDetls> selectByPrimaryKey(String rptFileName_, BigDecimal rptLogsId_) {
        return selectOne(c ->
            c.where(rptFileName, isEqualTo(rptFileName_))
            .and(rptLogsId, isEqualTo(rptLogsId_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, rptLogsDetls, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(RptLogsDetls row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(rptFileName).equalTo(row::getRptFileName)
                .set(rptLogsId).equalTo(row::getRptLogsId)
                .set(billOff).equalTo(row::getBillOff)
                .set(rptTimes).equalTo(row::getRptTimes)
                .set(billMonth).equalTo(row::getBillMonth)
                .set(billCycle).equalTo(row::getBillCycle)
                .set(rptDate).equalTo(row::getRptDate)
                .set(rptQuarter).equalTo(row::getRptQuarter)
                .set(offAdmin).equalTo(row::getOffAdmin)
                .set(rptFilePath).equalTo(row::getRptFilePath)
                .set(rptFileCount).equalTo(row::getRptFileCount)
                .set(rptFileAmt).equalTo(row::getRptFileAmt)
                .set(rptSecretMark).equalTo(row::getRptSecretMark)
                .set(rptLogsStatus).equalTo(row::getRptLogsStatus);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(RptLogsDetls row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(rptFileName).equalToWhenPresent(row::getRptFileName)
                .set(rptLogsId).equalToWhenPresent(row::getRptLogsId)
                .set(billOff).equalToWhenPresent(row::getBillOff)
                .set(rptTimes).equalToWhenPresent(row::getRptTimes)
                .set(billMonth).equalToWhenPresent(row::getBillMonth)
                .set(billCycle).equalToWhenPresent(row::getBillCycle)
                .set(rptDate).equalToWhenPresent(row::getRptDate)
                .set(rptQuarter).equalToWhenPresent(row::getRptQuarter)
                .set(offAdmin).equalToWhenPresent(row::getOffAdmin)
                .set(rptFilePath).equalToWhenPresent(row::getRptFilePath)
                .set(rptFileCount).equalToWhenPresent(row::getRptFileCount)
                .set(rptFileAmt).equalToWhenPresent(row::getRptFileAmt)
                .set(rptSecretMark).equalToWhenPresent(row::getRptSecretMark)
                .set(rptLogsStatus).equalToWhenPresent(row::getRptLogsStatus);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(RptLogsDetls row) {
        return update(c ->
            c.set(billOff).equalTo(row::getBillOff)
            .set(rptTimes).equalTo(row::getRptTimes)
            .set(billMonth).equalTo(row::getBillMonth)
            .set(billCycle).equalTo(row::getBillCycle)
            .set(rptDate).equalTo(row::getRptDate)
            .set(rptQuarter).equalTo(row::getRptQuarter)
            .set(offAdmin).equalTo(row::getOffAdmin)
            .set(rptFilePath).equalTo(row::getRptFilePath)
            .set(rptFileCount).equalTo(row::getRptFileCount)
            .set(rptFileAmt).equalTo(row::getRptFileAmt)
            .set(rptSecretMark).equalTo(row::getRptSecretMark)
            .set(rptLogsStatus).equalTo(row::getRptLogsStatus)
            .where(rptFileName, isEqualTo(row::getRptFileName))
            .and(rptLogsId, isEqualTo(row::getRptLogsId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(RptLogsDetls row) {
        return update(c ->
            c.set(billOff).equalToWhenPresent(row::getBillOff)
            .set(rptTimes).equalToWhenPresent(row::getRptTimes)
            .set(billMonth).equalToWhenPresent(row::getBillMonth)
            .set(billCycle).equalToWhenPresent(row::getBillCycle)
            .set(rptDate).equalToWhenPresent(row::getRptDate)
            .set(rptQuarter).equalToWhenPresent(row::getRptQuarter)
            .set(offAdmin).equalToWhenPresent(row::getOffAdmin)
            .set(rptFilePath).equalToWhenPresent(row::getRptFilePath)
            .set(rptFileCount).equalToWhenPresent(row::getRptFileCount)
            .set(rptFileAmt).equalToWhenPresent(row::getRptFileAmt)
            .set(rptSecretMark).equalToWhenPresent(row::getRptSecretMark)
            .set(rptLogsStatus).equalToWhenPresent(row::getRptLogsStatus)
            .where(rptFileName, isEqualTo(row::getRptFileName))
            .and(rptLogsId, isEqualTo(row::getRptLogsId))
        );
    }
}