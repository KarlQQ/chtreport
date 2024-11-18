package ccbs.dao.core.mapper;

import static ccbs.dao.core.sql.RptListDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import ccbs.dao.core.entity.RptList;
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
public interface RptListMapper extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<RptList>, CommonUpdateMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(rptListId, rptProgName, progCodeType, opType, systemId, rptCode, funCode, rptName, rptFileType, fileNameRegular, rptFilePath, rptStartYm, rptEndYm, createDate, updateDate, createEmpid, rptCodeStatus);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="RptListResult", value = {
        @Result(column="RPT_LIST_ID", property="rptListId", jdbcType=JdbcType.NUMERIC, id=true),
        @Result(column="RPT_PROG_NAME", property="rptProgName", jdbcType=JdbcType.NVARCHAR),
        @Result(column="PROG_CODE_TYPE", property="progCodeType", jdbcType=JdbcType.NVARCHAR),
        @Result(column="OP_TYPE", property="opType", jdbcType=JdbcType.CHAR),
        @Result(column="SYSTEM_ID", property="systemId", jdbcType=JdbcType.NVARCHAR),
        @Result(column="RPT_CODE", property="rptCode", jdbcType=JdbcType.NVARCHAR),
        @Result(column="FUN_CODE", property="funCode", jdbcType=JdbcType.NVARCHAR),
        @Result(column="RPT_NAME", property="rptName", jdbcType=JdbcType.NVARCHAR),
        @Result(column="RPT_FILE_TYPE", property="rptFileType", jdbcType=JdbcType.NVARCHAR),
        @Result(column="FILE_NAME_REGULAR", property="fileNameRegular", jdbcType=JdbcType.NVARCHAR),
        @Result(column="RPT_FILE_PATH", property="rptFilePath", jdbcType=JdbcType.NVARCHAR),
        @Result(column="RPT_START_YM", property="rptStartYm", jdbcType=JdbcType.CHAR),
        @Result(column="RPT_END_YM", property="rptEndYm", jdbcType=JdbcType.CHAR),
        @Result(column="CREATE_DATE", property="createDate", jdbcType=JdbcType.NVARCHAR),
        @Result(column="UPDATE_DATE", property="updateDate", jdbcType=JdbcType.NVARCHAR),
        @Result(column="CREATE_EMPID", property="createEmpid", jdbcType=JdbcType.NVARCHAR),
        @Result(column="RPT_CODE_STATUS", property="rptCodeStatus", jdbcType=JdbcType.CHAR)
    })
    List<RptList> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("RptListResult")
    Optional<RptList> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, rptList, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, rptList, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(BigDecimal rptListId_) {
        return delete(c -> 
            c.where(rptListId, isEqualTo(rptListId_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(RptList row) {
        return MyBatis3Utils.insert(this::insert, row, rptList, c ->
            c.map(rptListId).toProperty("rptListId")
            .map(rptProgName).toProperty("rptProgName")
            .map(progCodeType).toProperty("progCodeType")
            .map(opType).toProperty("opType")
            .map(systemId).toProperty("systemId")
            .map(rptCode).toProperty("rptCode")
            .map(funCode).toProperty("funCode")
            .map(rptName).toProperty("rptName")
            .map(rptFileType).toProperty("rptFileType")
            .map(fileNameRegular).toProperty("fileNameRegular")
            .map(rptFilePath).toProperty("rptFilePath")
            .map(rptStartYm).toProperty("rptStartYm")
            .map(rptEndYm).toProperty("rptEndYm")
            .map(createDate).toProperty("createDate")
            .map(updateDate).toProperty("updateDate")
            .map(createEmpid).toProperty("createEmpid")
            .map(rptCodeStatus).toProperty("rptCodeStatus")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<RptList> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, rptList, c ->
            c.map(rptListId).toProperty("rptListId")
            .map(rptProgName).toProperty("rptProgName")
            .map(progCodeType).toProperty("progCodeType")
            .map(opType).toProperty("opType")
            .map(systemId).toProperty("systemId")
            .map(rptCode).toProperty("rptCode")
            .map(funCode).toProperty("funCode")
            .map(rptName).toProperty("rptName")
            .map(rptFileType).toProperty("rptFileType")
            .map(fileNameRegular).toProperty("fileNameRegular")
            .map(rptFilePath).toProperty("rptFilePath")
            .map(rptStartYm).toProperty("rptStartYm")
            .map(rptEndYm).toProperty("rptEndYm")
            .map(createDate).toProperty("createDate")
            .map(updateDate).toProperty("updateDate")
            .map(createEmpid).toProperty("createEmpid")
            .map(rptCodeStatus).toProperty("rptCodeStatus")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(RptList row) {
        return MyBatis3Utils.insert(this::insert, row, rptList, c ->
            c.map(rptListId).toPropertyWhenPresent("rptListId", row::getRptListId)
            .map(rptProgName).toPropertyWhenPresent("rptProgName", row::getRptProgName)
            .map(progCodeType).toPropertyWhenPresent("progCodeType", row::getProgCodeType)
            .map(opType).toPropertyWhenPresent("opType", row::getOpType)
            .map(systemId).toPropertyWhenPresent("systemId", row::getSystemId)
            .map(rptCode).toPropertyWhenPresent("rptCode", row::getRptCode)
            .map(funCode).toPropertyWhenPresent("funCode", row::getFunCode)
            .map(rptName).toPropertyWhenPresent("rptName", row::getRptName)
            .map(rptFileType).toPropertyWhenPresent("rptFileType", row::getRptFileType)
            .map(fileNameRegular).toPropertyWhenPresent("fileNameRegular", row::getFileNameRegular)
            .map(rptFilePath).toPropertyWhenPresent("rptFilePath", row::getRptFilePath)
            .map(rptStartYm).toPropertyWhenPresent("rptStartYm", row::getRptStartYm)
            .map(rptEndYm).toPropertyWhenPresent("rptEndYm", row::getRptEndYm)
            .map(createDate).toPropertyWhenPresent("createDate", row::getCreateDate)
            .map(updateDate).toPropertyWhenPresent("updateDate", row::getUpdateDate)
            .map(createEmpid).toPropertyWhenPresent("createEmpid", row::getCreateEmpid)
            .map(rptCodeStatus).toPropertyWhenPresent("rptCodeStatus", row::getRptCodeStatus)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<RptList> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, rptList, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<RptList> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, rptList, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<RptList> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, rptList, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<RptList> selectByPrimaryKey(BigDecimal rptListId_) {
        return selectOne(c ->
            c.where(rptListId, isEqualTo(rptListId_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, rptList, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(RptList row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(rptListId).equalTo(row::getRptListId)
                .set(rptProgName).equalTo(row::getRptProgName)
                .set(progCodeType).equalTo(row::getProgCodeType)
                .set(opType).equalTo(row::getOpType)
                .set(systemId).equalTo(row::getSystemId)
                .set(rptCode).equalTo(row::getRptCode)
                .set(funCode).equalTo(row::getFunCode)
                .set(rptName).equalTo(row::getRptName)
                .set(rptFileType).equalTo(row::getRptFileType)
                .set(fileNameRegular).equalTo(row::getFileNameRegular)
                .set(rptFilePath).equalTo(row::getRptFilePath)
                .set(rptStartYm).equalTo(row::getRptStartYm)
                .set(rptEndYm).equalTo(row::getRptEndYm)
                .set(createDate).equalTo(row::getCreateDate)
                .set(updateDate).equalTo(row::getUpdateDate)
                .set(createEmpid).equalTo(row::getCreateEmpid)
                .set(rptCodeStatus).equalTo(row::getRptCodeStatus);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(RptList row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(rptListId).equalToWhenPresent(row::getRptListId)
                .set(rptProgName).equalToWhenPresent(row::getRptProgName)
                .set(progCodeType).equalToWhenPresent(row::getProgCodeType)
                .set(opType).equalToWhenPresent(row::getOpType)
                .set(systemId).equalToWhenPresent(row::getSystemId)
                .set(rptCode).equalToWhenPresent(row::getRptCode)
                .set(funCode).equalToWhenPresent(row::getFunCode)
                .set(rptName).equalToWhenPresent(row::getRptName)
                .set(rptFileType).equalToWhenPresent(row::getRptFileType)
                .set(fileNameRegular).equalToWhenPresent(row::getFileNameRegular)
                .set(rptFilePath).equalToWhenPresent(row::getRptFilePath)
                .set(rptStartYm).equalToWhenPresent(row::getRptStartYm)
                .set(rptEndYm).equalToWhenPresent(row::getRptEndYm)
                .set(createDate).equalToWhenPresent(row::getCreateDate)
                .set(updateDate).equalToWhenPresent(row::getUpdateDate)
                .set(createEmpid).equalToWhenPresent(row::getCreateEmpid)
                .set(rptCodeStatus).equalToWhenPresent(row::getRptCodeStatus);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(RptList row) {
        return update(c ->
            c.set(rptProgName).equalTo(row::getRptProgName)
            .set(progCodeType).equalTo(row::getProgCodeType)
            .set(opType).equalTo(row::getOpType)
            .set(systemId).equalTo(row::getSystemId)
            .set(rptCode).equalTo(row::getRptCode)
            .set(funCode).equalTo(row::getFunCode)
            .set(rptName).equalTo(row::getRptName)
            .set(rptFileType).equalTo(row::getRptFileType)
            .set(fileNameRegular).equalTo(row::getFileNameRegular)
            .set(rptFilePath).equalTo(row::getRptFilePath)
            .set(rptStartYm).equalTo(row::getRptStartYm)
            .set(rptEndYm).equalTo(row::getRptEndYm)
            .set(createDate).equalTo(row::getCreateDate)
            .set(updateDate).equalTo(row::getUpdateDate)
            .set(createEmpid).equalTo(row::getCreateEmpid)
            .set(rptCodeStatus).equalTo(row::getRptCodeStatus)
            .where(rptListId, isEqualTo(row::getRptListId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(RptList row) {
        return update(c ->
            c.set(rptProgName).equalToWhenPresent(row::getRptProgName)
            .set(progCodeType).equalToWhenPresent(row::getProgCodeType)
            .set(opType).equalToWhenPresent(row::getOpType)
            .set(systemId).equalToWhenPresent(row::getSystemId)
            .set(rptCode).equalToWhenPresent(row::getRptCode)
            .set(funCode).equalToWhenPresent(row::getFunCode)
            .set(rptName).equalToWhenPresent(row::getRptName)
            .set(rptFileType).equalToWhenPresent(row::getRptFileType)
            .set(fileNameRegular).equalToWhenPresent(row::getFileNameRegular)
            .set(rptFilePath).equalToWhenPresent(row::getRptFilePath)
            .set(rptStartYm).equalToWhenPresent(row::getRptStartYm)
            .set(rptEndYm).equalToWhenPresent(row::getRptEndYm)
            .set(createDate).equalToWhenPresent(row::getCreateDate)
            .set(updateDate).equalToWhenPresent(row::getUpdateDate)
            .set(createEmpid).equalToWhenPresent(row::getCreateEmpid)
            .set(rptCodeStatus).equalToWhenPresent(row::getRptCodeStatus)
            .where(rptListId, isEqualTo(row::getRptListId))
        );
    }
}