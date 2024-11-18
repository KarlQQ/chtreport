package ccbs.dao.core.mapper;

import static ccbs.dao.core.sql.RptLogsDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import ccbs.dao.core.entity.RptLogs;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;
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
public interface RptLogsMapper extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<RptLogs>, CommonUpdateMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(rptLogsId, rptCode, startDate, startTime, endDate, endTime, createEmpid, createStatus, createCount, errorCount, opType, opBatchno, paramIntValues, paramExtValues);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="RptLogsResult", value = {
        @Result(column="RPT_LOGS_ID", property="rptLogsId", jdbcType=JdbcType.NUMERIC, id=true),
        @Result(column="RPT_CODE", property="rptCode", jdbcType=JdbcType.NVARCHAR),
        @Result(column="START_DATE", property="startDate", jdbcType=JdbcType.NVARCHAR),
        @Result(column="START_TIME", property="startTime", jdbcType=JdbcType.NVARCHAR),
        @Result(column="END_DATE", property="endDate", jdbcType=JdbcType.NVARCHAR),
        @Result(column="END_TIME", property="endTime", jdbcType=JdbcType.NVARCHAR),
        @Result(column="CREATE_EMPID", property="createEmpid", jdbcType=JdbcType.NVARCHAR),
        @Result(column="CREATE_STATUS", property="createStatus", jdbcType=JdbcType.NVARCHAR),
        @Result(column="CREATE_COUNT", property="createCount", jdbcType=JdbcType.NUMERIC),
        @Result(column="ERROR_COUNT", property="errorCount", jdbcType=JdbcType.NUMERIC),
        @Result(column="OP_TYPE", property="opType", jdbcType=JdbcType.CHAR),
        @Result(column="OP_BATCHNO", property="opBatchno", jdbcType=JdbcType.NUMERIC),
        @Result(column="PARAM_INT_VALUES", property="paramIntValues", jdbcType=JdbcType.NVARCHAR),
        @Result(column="PARAM_EXT_VALUES", property="paramExtValues", jdbcType=JdbcType.NVARCHAR)
    })
    List<RptLogs> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("RptLogsResult")
    Optional<RptLogs> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, rptLogs, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, rptLogs, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(BigDecimal rptLogsId_) {
        return delete(c -> 
            c.where(rptLogsId, isEqualTo(rptLogsId_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(RptLogs row) {
        return MyBatis3Utils.insert(this::insert, row, rptLogs, c ->
            c.map(rptLogsId).toProperty("rptLogsId")
            .map(rptCode).toProperty("rptCode")
            .map(startDate).toProperty("startDate")
            .map(startTime).toProperty("startTime")
            .map(endDate).toProperty("endDate")
            .map(endTime).toProperty("endTime")
            .map(createEmpid).toProperty("createEmpid")
            .map(createStatus).toProperty("createStatus")
            .map(createCount).toProperty("createCount")
            .map(errorCount).toProperty("errorCount")
            .map(opType).toProperty("opType")
            .map(opBatchno).toProperty("opBatchno")
            .map(paramIntValues).toProperty("paramIntValues")
            .map(paramExtValues).toProperty("paramExtValues")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<RptLogs> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, rptLogs, c ->
            c.map(rptLogsId).toProperty("rptLogsId")
            .map(rptCode).toProperty("rptCode")
            .map(startDate).toProperty("startDate")
            .map(startTime).toProperty("startTime")
            .map(endDate).toProperty("endDate")
            .map(endTime).toProperty("endTime")
            .map(createEmpid).toProperty("createEmpid")
            .map(createStatus).toProperty("createStatus")
            .map(createCount).toProperty("createCount")
            .map(errorCount).toProperty("errorCount")
            .map(opType).toProperty("opType")
            .map(opBatchno).toProperty("opBatchno")
            .map(paramIntValues).toProperty("paramIntValues")
            .map(paramExtValues).toProperty("paramExtValues")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(RptLogs row) {
        return MyBatis3Utils.insert(this::insert, row, rptLogs, c ->
            c.map(rptLogsId).toPropertyWhenPresent("rptLogsId", row::getRptLogsId)
            .map(rptCode).toPropertyWhenPresent("rptCode", row::getRptCode)
            .map(startDate).toPropertyWhenPresent("startDate", row::getStartDate)
            .map(startTime).toPropertyWhenPresent("startTime", row::getStartTime)
            .map(endDate).toPropertyWhenPresent("endDate", row::getEndDate)
            .map(endTime).toPropertyWhenPresent("endTime", row::getEndTime)
            .map(createEmpid).toPropertyWhenPresent("createEmpid", row::getCreateEmpid)
            .map(createStatus).toPropertyWhenPresent("createStatus", row::getCreateStatus)
            .map(createCount).toPropertyWhenPresent("createCount", row::getCreateCount)
            .map(errorCount).toPropertyWhenPresent("errorCount", row::getErrorCount)
            .map(opType).toPropertyWhenPresent("opType", row::getOpType)
            .map(opBatchno).toPropertyWhenPresent("opBatchno", row::getOpBatchno)
            .map(paramIntValues).toPropertyWhenPresent("paramIntValues", row::getParamIntValues)
            .map(paramExtValues).toPropertyWhenPresent("paramExtValues", row::getParamExtValues)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<RptLogs> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, rptLogs, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<RptLogs> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, rptLogs, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<RptLogs> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, rptLogs, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<RptLogs> selectByPrimaryKey(BigDecimal rptLogsId_) {
        return selectOne(c ->
            c.where(rptLogsId, isEqualTo(rptLogsId_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, rptLogs, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(RptLogs row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(rptLogsId).equalTo(row::getRptLogsId)
                .set(rptCode).equalTo(row::getRptCode)
                .set(startDate).equalTo(row::getStartDate)
                .set(startTime).equalTo(row::getStartTime)
                .set(endDate).equalTo(row::getEndDate)
                .set(endTime).equalTo(row::getEndTime)
                .set(createEmpid).equalTo(row::getCreateEmpid)
                .set(createStatus).equalTo(row::getCreateStatus)
                .set(createCount).equalTo(row::getCreateCount)
                .set(errorCount).equalTo(row::getErrorCount)
                .set(opType).equalTo(row::getOpType)
                .set(opBatchno).equalTo(row::getOpBatchno)
                .set(paramIntValues).equalTo(row::getParamIntValues)
                .set(paramExtValues).equalTo(row::getParamExtValues);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(RptLogs row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(rptLogsId).equalToWhenPresent(row::getRptLogsId)
                .set(rptCode).equalToWhenPresent(row::getRptCode)
                .set(startDate).equalToWhenPresent(row::getStartDate)
                .set(startTime).equalToWhenPresent(row::getStartTime)
                .set(endDate).equalToWhenPresent(row::getEndDate)
                .set(endTime).equalToWhenPresent(row::getEndTime)
                .set(createEmpid).equalToWhenPresent(row::getCreateEmpid)
                .set(createStatus).equalToWhenPresent(row::getCreateStatus)
                .set(createCount).equalToWhenPresent(row::getCreateCount)
                .set(errorCount).equalToWhenPresent(row::getErrorCount)
                .set(opType).equalToWhenPresent(row::getOpType)
                .set(opBatchno).equalToWhenPresent(row::getOpBatchno)
                .set(paramIntValues).equalToWhenPresent(row::getParamIntValues)
                .set(paramExtValues).equalToWhenPresent(row::getParamExtValues);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(RptLogs row) {
        return update(c ->
            c.set(rptCode).equalTo(row::getRptCode)
            .set(startDate).equalTo(row::getStartDate)
            .set(startTime).equalTo(row::getStartTime)
            .set(endDate).equalTo(row::getEndDate)
            .set(endTime).equalTo(row::getEndTime)
            .set(createEmpid).equalTo(row::getCreateEmpid)
            .set(createStatus).equalTo(row::getCreateStatus)
            .set(createCount).equalTo(row::getCreateCount)
            .set(errorCount).equalTo(row::getErrorCount)
            .set(opType).equalTo(row::getOpType)
            .set(opBatchno).equalTo(row::getOpBatchno)
            .set(paramIntValues).equalTo(row::getParamIntValues)
            .set(paramExtValues).equalTo(row::getParamExtValues)
            .where(rptLogsId, isEqualTo(row::getRptLogsId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(RptLogs row) {
        return update(c ->
            c.set(rptCode).equalToWhenPresent(row::getRptCode)
            .set(startDate).equalToWhenPresent(row::getStartDate)
            .set(startTime).equalToWhenPresent(row::getStartTime)
            .set(endDate).equalToWhenPresent(row::getEndDate)
            .set(endTime).equalToWhenPresent(row::getEndTime)
            .set(createEmpid).equalToWhenPresent(row::getCreateEmpid)
            .set(createStatus).equalToWhenPresent(row::getCreateStatus)
            .set(createCount).equalToWhenPresent(row::getCreateCount)
            .set(errorCount).equalToWhenPresent(row::getErrorCount)
            .set(opType).equalToWhenPresent(row::getOpType)
            .set(opBatchno).equalToWhenPresent(row::getOpBatchno)
            .set(paramIntValues).equalToWhenPresent(row::getParamIntValues)
            .set(paramExtValues).equalToWhenPresent(row::getParamExtValues)
            .where(rptLogsId, isEqualTo(row::getRptLogsId))
        );
    }

//    @Insert("INSERT INTO RPT_LOGS(RPT_CODE,START_DATE,START_TIME,CREATE_STATUS) VALUES(#{rptCode}, #{startDate}, #{startTime}, #{createStatus})")@Options(useGeneratedKeys = true, keyProperty = "rptLogsId")void insertAndGetId(RptLogs entity);

//    @Insert("INSERT INTO RPT_LOGS(RPT_CODE,START_DATE,START_TIME,CREATE_STATUS) VALUES(#{rptCode}, #{startDate}, #{startTime}, #{createStatus})")@SelectKey(statement = "SELECT ISEQ$$_73119.NEXTVAL FROM dual", keyProperty = "rptLogsId", before = true, resultType = BigDecimal.class)void insertAndGetId(RptLogs entity);

}