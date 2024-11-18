package ccbs.dao.core.mapper;

import static ccbs.dao.core.sql.CommOfficeDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;


import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

import ccbs.dao.core.entity.CommOffice;

@Mapper
public interface CommOfficeMapper extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<CommOffice>, CommonUpdateMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(office, lifeStart, lifeEnd, partId, offName, businessNo, offZip, offAddress, offTelno, offFax, offEmail, billTelno, boss, manager, offCity, offSw, offBill, offProc, offBelong, offAdmin, admin, admProc, areano, servTelno, servFax, mailtelno, mailfax, wordha, wordhc, wordhe, wordhf, wordhi, errFax);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="CommOfficeResult", value = {
        @Result(column="OFFICE", property="office", jdbcType=JdbcType.CHAR, id=true),
        @Result(column="LIFE_START", property="lifeStart", jdbcType=JdbcType.CHAR),
        @Result(column="LIFE_END", property="lifeEnd", jdbcType=JdbcType.CHAR),
        @Result(column="PART_ID", property="partId", jdbcType=JdbcType.CHAR),
        @Result(column="OFF_NAME", property="offName", jdbcType=JdbcType.CHAR),
        @Result(column="BUSINESS_NO", property="businessNo", jdbcType=JdbcType.CHAR),
        @Result(column="OFF_ZIP", property="offZip", jdbcType=JdbcType.CHAR),
        @Result(column="OFF_ADDRESS", property="offAddress", jdbcType=JdbcType.CHAR),
        @Result(column="OFF_TELNO", property="offTelno", jdbcType=JdbcType.CHAR),
        @Result(column="OFF_FAX", property="offFax", jdbcType=JdbcType.CHAR),
        @Result(column="OFF_EMAIL", property="offEmail", jdbcType=JdbcType.CHAR),
        @Result(column="BILL_TELNO", property="billTelno", jdbcType=JdbcType.CHAR),
        @Result(column="BOSS", property="boss", jdbcType=JdbcType.CHAR),
        @Result(column="MANAGER", property="manager", jdbcType=JdbcType.CHAR),
        @Result(column="OFF_CITY", property="offCity", jdbcType=JdbcType.CHAR),
        @Result(column="OFF_SW", property="offSw", jdbcType=JdbcType.CHAR),
        @Result(column="OFF_BILL", property="offBill", jdbcType=JdbcType.CHAR),
        @Result(column="OFF_PROC", property="offProc", jdbcType=JdbcType.CHAR),
        @Result(column="OFF_BELONG", property="offBelong", jdbcType=JdbcType.CHAR),
        @Result(column="OFF_ADMIN", property="offAdmin", jdbcType=JdbcType.CHAR),
        @Result(column="ADMIN", property="admin", jdbcType=JdbcType.CHAR),
        @Result(column="ADM_PROC", property="admProc", jdbcType=JdbcType.CHAR),
        @Result(column="AREANO", property="areano", jdbcType=JdbcType.CHAR),
        @Result(column="SERV_TELNO", property="servTelno", jdbcType=JdbcType.CHAR),
        @Result(column="SERV_FAX", property="servFax", jdbcType=JdbcType.CHAR),
        @Result(column="MAILTELNO", property="mailtelno", jdbcType=JdbcType.CHAR),
        @Result(column="MAILFAX", property="mailfax", jdbcType=JdbcType.CHAR),
        @Result(column="WORDHA", property="wordha", jdbcType=JdbcType.CHAR),
        @Result(column="WORDHC", property="wordhc", jdbcType=JdbcType.CHAR),
        @Result(column="WORDHE", property="wordhe", jdbcType=JdbcType.CHAR),
        @Result(column="WORDHF", property="wordhf", jdbcType=JdbcType.CHAR),
        @Result(column="WORDHI", property="wordhi", jdbcType=JdbcType.CHAR),
        @Result(column="ERR_FAX", property="errFax", jdbcType=JdbcType.CHAR)
    })
    List<CommOffice> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("CommOfficeResult")
    Optional<CommOffice> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, commOffice, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, commOffice, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(String office_) {
        return delete(c -> 
            c.where(office, isEqualTo(office_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(CommOffice row) {
        return MyBatis3Utils.insert(this::insert, row, commOffice, c ->
            c.map(office).toProperty("office")
            .map(lifeStart).toProperty("lifeStart")
            .map(lifeEnd).toProperty("lifeEnd")
            .map(partId).toProperty("partId")
            .map(offName).toProperty("offName")
            .map(businessNo).toProperty("businessNo")
            .map(offZip).toProperty("offZip")
            .map(offAddress).toProperty("offAddress")
            .map(offTelno).toProperty("offTelno")
            .map(offFax).toProperty("offFax")
            .map(offEmail).toProperty("offEmail")
            .map(billTelno).toProperty("billTelno")
            .map(boss).toProperty("boss")
            .map(manager).toProperty("manager")
            .map(offCity).toProperty("offCity")
            .map(offSw).toProperty("offSw")
            .map(offBill).toProperty("offBill")
            .map(offProc).toProperty("offProc")
            .map(offBelong).toProperty("offBelong")
            .map(offAdmin).toProperty("offAdmin")
            .map(admin).toProperty("admin")
            .map(admProc).toProperty("admProc")
            .map(areano).toProperty("areano")
            .map(servTelno).toProperty("servTelno")
            .map(servFax).toProperty("servFax")
            .map(mailtelno).toProperty("mailtelno")
            .map(mailfax).toProperty("mailfax")
            .map(wordha).toProperty("wordha")
            .map(wordhc).toProperty("wordhc")
            .map(wordhe).toProperty("wordhe")
            .map(wordhf).toProperty("wordhf")
            .map(wordhi).toProperty("wordhi")
            .map(errFax).toProperty("errFax")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<CommOffice> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, commOffice, c ->
            c.map(office).toProperty("office")
            .map(lifeStart).toProperty("lifeStart")
            .map(lifeEnd).toProperty("lifeEnd")
            .map(partId).toProperty("partId")
            .map(offName).toProperty("offName")
            .map(businessNo).toProperty("businessNo")
            .map(offZip).toProperty("offZip")
            .map(offAddress).toProperty("offAddress")
            .map(offTelno).toProperty("offTelno")
            .map(offFax).toProperty("offFax")
            .map(offEmail).toProperty("offEmail")
            .map(billTelno).toProperty("billTelno")
            .map(boss).toProperty("boss")
            .map(manager).toProperty("manager")
            .map(offCity).toProperty("offCity")
            .map(offSw).toProperty("offSw")
            .map(offBill).toProperty("offBill")
            .map(offProc).toProperty("offProc")
            .map(offBelong).toProperty("offBelong")
            .map(offAdmin).toProperty("offAdmin")
            .map(admin).toProperty("admin")
            .map(admProc).toProperty("admProc")
            .map(areano).toProperty("areano")
            .map(servTelno).toProperty("servTelno")
            .map(servFax).toProperty("servFax")
            .map(mailtelno).toProperty("mailtelno")
            .map(mailfax).toProperty("mailfax")
            .map(wordha).toProperty("wordha")
            .map(wordhc).toProperty("wordhc")
            .map(wordhe).toProperty("wordhe")
            .map(wordhf).toProperty("wordhf")
            .map(wordhi).toProperty("wordhi")
            .map(errFax).toProperty("errFax")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(CommOffice row) {
        return MyBatis3Utils.insert(this::insert, row, commOffice, c ->
            c.map(office).toPropertyWhenPresent("office", row::getOffice)
            .map(lifeStart).toPropertyWhenPresent("lifeStart", row::getLifeStart)
            .map(lifeEnd).toPropertyWhenPresent("lifeEnd", row::getLifeEnd)
            .map(partId).toPropertyWhenPresent("partId", row::getPartId)
            .map(offName).toPropertyWhenPresent("offName", row::getOffName)
            .map(businessNo).toPropertyWhenPresent("businessNo", row::getBusinessNo)
            .map(offZip).toPropertyWhenPresent("offZip", row::getOffZip)
            .map(offAddress).toPropertyWhenPresent("offAddress", row::getOffAddress)
            .map(offTelno).toPropertyWhenPresent("offTelno", row::getOffTelno)
            .map(offFax).toPropertyWhenPresent("offFax", row::getOffFax)
            .map(offEmail).toPropertyWhenPresent("offEmail", row::getOffEmail)
            .map(billTelno).toPropertyWhenPresent("billTelno", row::getBillTelno)
            .map(boss).toPropertyWhenPresent("boss", row::getBoss)
            .map(manager).toPropertyWhenPresent("manager", row::getManager)
            .map(offCity).toPropertyWhenPresent("offCity", row::getOffCity)
            .map(offSw).toPropertyWhenPresent("offSw", row::getOffSw)
            .map(offBill).toPropertyWhenPresent("offBill", row::getOffBill)
            .map(offProc).toPropertyWhenPresent("offProc", row::getOffProc)
            .map(offBelong).toPropertyWhenPresent("offBelong", row::getOffBelong)
            .map(offAdmin).toPropertyWhenPresent("offAdmin", row::getOffAdmin)
            .map(admin).toPropertyWhenPresent("admin", row::getAdmin)
            .map(admProc).toPropertyWhenPresent("admProc", row::getAdmProc)
            .map(areano).toPropertyWhenPresent("areano", row::getAreano)
            .map(servTelno).toPropertyWhenPresent("servTelno", row::getServTelno)
            .map(servFax).toPropertyWhenPresent("servFax", row::getServFax)
            .map(mailtelno).toPropertyWhenPresent("mailtelno", row::getMailtelno)
            .map(mailfax).toPropertyWhenPresent("mailfax", row::getMailfax)
            .map(wordha).toPropertyWhenPresent("wordha", row::getWordha)
            .map(wordhc).toPropertyWhenPresent("wordhc", row::getWordhc)
            .map(wordhe).toPropertyWhenPresent("wordhe", row::getWordhe)
            .map(wordhf).toPropertyWhenPresent("wordhf", row::getWordhf)
            .map(wordhi).toPropertyWhenPresent("wordhi", row::getWordhi)
            .map(errFax).toPropertyWhenPresent("errFax", row::getErrFax)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<CommOffice> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, commOffice, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<CommOffice> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, commOffice, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<CommOffice> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, commOffice, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<CommOffice> selectByPrimaryKey(String office_) {
        return selectOne(c ->
            c.where(office, isEqualTo(office_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, commOffice, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(CommOffice row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(office).equalTo(row::getOffice)
                .set(lifeStart).equalTo(row::getLifeStart)
                .set(lifeEnd).equalTo(row::getLifeEnd)
                .set(partId).equalTo(row::getPartId)
                .set(offName).equalTo(row::getOffName)
                .set(businessNo).equalTo(row::getBusinessNo)
                .set(offZip).equalTo(row::getOffZip)
                .set(offAddress).equalTo(row::getOffAddress)
                .set(offTelno).equalTo(row::getOffTelno)
                .set(offFax).equalTo(row::getOffFax)
                .set(offEmail).equalTo(row::getOffEmail)
                .set(billTelno).equalTo(row::getBillTelno)
                .set(boss).equalTo(row::getBoss)
                .set(manager).equalTo(row::getManager)
                .set(offCity).equalTo(row::getOffCity)
                .set(offSw).equalTo(row::getOffSw)
                .set(offBill).equalTo(row::getOffBill)
                .set(offProc).equalTo(row::getOffProc)
                .set(offBelong).equalTo(row::getOffBelong)
                .set(offAdmin).equalTo(row::getOffAdmin)
                .set(admin).equalTo(row::getAdmin)
                .set(admProc).equalTo(row::getAdmProc)
                .set(areano).equalTo(row::getAreano)
                .set(servTelno).equalTo(row::getServTelno)
                .set(servFax).equalTo(row::getServFax)
                .set(mailtelno).equalTo(row::getMailtelno)
                .set(mailfax).equalTo(row::getMailfax)
                .set(wordha).equalTo(row::getWordha)
                .set(wordhc).equalTo(row::getWordhc)
                .set(wordhe).equalTo(row::getWordhe)
                .set(wordhf).equalTo(row::getWordhf)
                .set(wordhi).equalTo(row::getWordhi)
                .set(errFax).equalTo(row::getErrFax);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(CommOffice row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(office).equalToWhenPresent(row::getOffice)
                .set(lifeStart).equalToWhenPresent(row::getLifeStart)
                .set(lifeEnd).equalToWhenPresent(row::getLifeEnd)
                .set(partId).equalToWhenPresent(row::getPartId)
                .set(offName).equalToWhenPresent(row::getOffName)
                .set(businessNo).equalToWhenPresent(row::getBusinessNo)
                .set(offZip).equalToWhenPresent(row::getOffZip)
                .set(offAddress).equalToWhenPresent(row::getOffAddress)
                .set(offTelno).equalToWhenPresent(row::getOffTelno)
                .set(offFax).equalToWhenPresent(row::getOffFax)
                .set(offEmail).equalToWhenPresent(row::getOffEmail)
                .set(billTelno).equalToWhenPresent(row::getBillTelno)
                .set(boss).equalToWhenPresent(row::getBoss)
                .set(manager).equalToWhenPresent(row::getManager)
                .set(offCity).equalToWhenPresent(row::getOffCity)
                .set(offSw).equalToWhenPresent(row::getOffSw)
                .set(offBill).equalToWhenPresent(row::getOffBill)
                .set(offProc).equalToWhenPresent(row::getOffProc)
                .set(offBelong).equalToWhenPresent(row::getOffBelong)
                .set(offAdmin).equalToWhenPresent(row::getOffAdmin)
                .set(admin).equalToWhenPresent(row::getAdmin)
                .set(admProc).equalToWhenPresent(row::getAdmProc)
                .set(areano).equalToWhenPresent(row::getAreano)
                .set(servTelno).equalToWhenPresent(row::getServTelno)
                .set(servFax).equalToWhenPresent(row::getServFax)
                .set(mailtelno).equalToWhenPresent(row::getMailtelno)
                .set(mailfax).equalToWhenPresent(row::getMailfax)
                .set(wordha).equalToWhenPresent(row::getWordha)
                .set(wordhc).equalToWhenPresent(row::getWordhc)
                .set(wordhe).equalToWhenPresent(row::getWordhe)
                .set(wordhf).equalToWhenPresent(row::getWordhf)
                .set(wordhi).equalToWhenPresent(row::getWordhi)
                .set(errFax).equalToWhenPresent(row::getErrFax);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(CommOffice row) {
        return update(c ->
            c.set(lifeStart).equalTo(row::getLifeStart)
            .set(lifeEnd).equalTo(row::getLifeEnd)
            .set(partId).equalTo(row::getPartId)
            .set(offName).equalTo(row::getOffName)
            .set(businessNo).equalTo(row::getBusinessNo)
            .set(offZip).equalTo(row::getOffZip)
            .set(offAddress).equalTo(row::getOffAddress)
            .set(offTelno).equalTo(row::getOffTelno)
            .set(offFax).equalTo(row::getOffFax)
            .set(offEmail).equalTo(row::getOffEmail)
            .set(billTelno).equalTo(row::getBillTelno)
            .set(boss).equalTo(row::getBoss)
            .set(manager).equalTo(row::getManager)
            .set(offCity).equalTo(row::getOffCity)
            .set(offSw).equalTo(row::getOffSw)
            .set(offBill).equalTo(row::getOffBill)
            .set(offProc).equalTo(row::getOffProc)
            .set(offBelong).equalTo(row::getOffBelong)
            .set(offAdmin).equalTo(row::getOffAdmin)
            .set(admin).equalTo(row::getAdmin)
            .set(admProc).equalTo(row::getAdmProc)
            .set(areano).equalTo(row::getAreano)
            .set(servTelno).equalTo(row::getServTelno)
            .set(servFax).equalTo(row::getServFax)
            .set(mailtelno).equalTo(row::getMailtelno)
            .set(mailfax).equalTo(row::getMailfax)
            .set(wordha).equalTo(row::getWordha)
            .set(wordhc).equalTo(row::getWordhc)
            .set(wordhe).equalTo(row::getWordhe)
            .set(wordhf).equalTo(row::getWordhf)
            .set(wordhi).equalTo(row::getWordhi)
            .set(errFax).equalTo(row::getErrFax)
            .where(office, isEqualTo(row::getOffice))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(CommOffice row) {
        return update(c ->
            c.set(lifeStart).equalToWhenPresent(row::getLifeStart)
            .set(lifeEnd).equalToWhenPresent(row::getLifeEnd)
            .set(partId).equalToWhenPresent(row::getPartId)
            .set(offName).equalToWhenPresent(row::getOffName)
            .set(businessNo).equalToWhenPresent(row::getBusinessNo)
            .set(offZip).equalToWhenPresent(row::getOffZip)
            .set(offAddress).equalToWhenPresent(row::getOffAddress)
            .set(offTelno).equalToWhenPresent(row::getOffTelno)
            .set(offFax).equalToWhenPresent(row::getOffFax)
            .set(offEmail).equalToWhenPresent(row::getOffEmail)
            .set(billTelno).equalToWhenPresent(row::getBillTelno)
            .set(boss).equalToWhenPresent(row::getBoss)
            .set(manager).equalToWhenPresent(row::getManager)
            .set(offCity).equalToWhenPresent(row::getOffCity)
            .set(offSw).equalToWhenPresent(row::getOffSw)
            .set(offBill).equalToWhenPresent(row::getOffBill)
            .set(offProc).equalToWhenPresent(row::getOffProc)
            .set(offBelong).equalToWhenPresent(row::getOffBelong)
            .set(offAdmin).equalToWhenPresent(row::getOffAdmin)
            .set(admin).equalToWhenPresent(row::getAdmin)
            .set(admProc).equalToWhenPresent(row::getAdmProc)
            .set(areano).equalToWhenPresent(row::getAreano)
            .set(servTelno).equalToWhenPresent(row::getServTelno)
            .set(servFax).equalToWhenPresent(row::getServFax)
            .set(mailtelno).equalToWhenPresent(row::getMailtelno)
            .set(mailfax).equalToWhenPresent(row::getMailfax)
            .set(wordha).equalToWhenPresent(row::getWordha)
            .set(wordhc).equalToWhenPresent(row::getWordhc)
            .set(wordhe).equalToWhenPresent(row::getWordhe)
            .set(wordhf).equalToWhenPresent(row::getWordhf)
            .set(wordhi).equalToWhenPresent(row::getWordhi)
            .set(errFax).equalToWhenPresent(row::getErrFax)
            .where(office, isEqualTo(row::getOffice))
        );
    }

    @SelectProvider(type = SqlProvider.class, method = "selectOfficeNameByCodeAndType")
    @ResultMap("CommOfficeResult")
    Optional<CommOffice> selectOfficeNameByCodeAndType(@Param("officeCode") String officeCode, @Param("transType") String transType);

    class SqlProvider {
        public String selectOfficeNameByCodeAndType(@Param("officeCode") String officeCode, @Param("transType") String transType) {
            String sql = "SELECT OFF_NAME, OFFICE FROM COMM_OFFICE WHERE TRIM(OFFICE) = ";
            switch (transType.trim()) {
                case "A":
                    sql += "#{officeCode}";
                    break;
                case "B":
                    sql += "(SELECT OFF_BELONG FROM COMM_OFFICE WHERE TRIM(OFFICE) = #{officeCode})";
                    break;
                case "C":
                    sql += "(SELECT OFF_ADMIN FROM COMM_OFFICE WHERE TRIM(OFFICE) = #{officeCode})";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid transType: " + transType);
            }
            return sql;
        }
    }
}