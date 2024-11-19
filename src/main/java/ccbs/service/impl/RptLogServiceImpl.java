package ccbs.service.impl;

import ccbs.dao.core.entity.RptList;
import ccbs.dao.core.entity.RptLogs;
import ccbs.dao.core.entity.RptLogsDetls;
import ccbs.dao.core.mapper.RptListMapper;
import ccbs.dao.core.mapper.RptLogsDetlsMapper;
import ccbs.dao.core.mapper.RptLogsMapper;
import ccbs.dao.core.sql.RptListDynamicSqlSupport;
import ccbs.dao.core.sql.RptLogsDetlsDynamicSqlSupport;
import ccbs.dao.core.sql.RptLogsDynamicSqlSupport;
import ccbs.model.batch.RptLogAfterExecuteInputStr;
import ccbs.model.online.*;
import ccbs.service.intf.RptLogService;
import ccbs.util.NameMapping;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mybatis.dynamic.sql.SqlBuilder.*;

@Service
@Slf4j
public class RptLogServiceImpl implements RptLogService {
    @Autowired
    private RptLogsMapper rptLogsMapper;

    @Autowired
    private RptListMapper rptListMapper;

    @Autowired
    private RptLogsDetlsMapper rptLogsDetlsMapper;

    RptLogsDynamicSqlSupport.RptLogs rptLogs = RptLogsDynamicSqlSupport.rptLogs.withAlias("rptLogs");
    RptListDynamicSqlSupport.RptList rptList = RptListDynamicSqlSupport.rptList.withAlias("rptist");
    RptLogsDetlsDynamicSqlSupport.RptLogsDetls rptLogsDetls = RptLogsDetlsDynamicSqlSupport.rptLogsDetls.withAlias("rptLogsDetls");

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean insertRptLogRecordBeforeExecute(RptLogs input) {
        try {
            int count = rptLogsMapper.insertSelective(input);
            if (count > 0) {
                return true;
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return false;
    }

    @Override
    public RptLogs getRptLogRecord(RptLogs rptLogsIn) {
        RptLogs result = new RptLogs();
        try {
            //quert rpt_logs
            SelectStatementProvider rptLogsProvider =
                    select(rptLogs.allColumns())
                            .from(rptLogs)
                            .where(rptLogs.startDate, isEqualTo(rptLogsIn.getStartDate()))
                            .and(rptLogs.startTime, isEqualTo(rptLogsIn.getStartTime()))
                            .and(rptLogs.rptCode, isEqualTo(rptLogsIn.getRptCode()))
                            .and(rptLogs.createStatus, isEqualTo(rptLogsIn.getCreateStatus())).
                            build().render(RenderingStrategies.MYBATIS3);
            result = rptLogsMapper.selectOne(rptLogsProvider).get();

        } catch (Exception e) {
            log.error("", e);
        }

        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateRptLogRecordAndRptLogDetailsAfterExecute(RptLogAfterExecuteInputStr input) {

        try {

            //update RPT_LOGS
            RptLogs rptLogsIn = new RptLogs();

            rptLogsIn.setRptLogsId(new BigDecimal(input.getRptLogsId()));
            rptLogsIn.setRptCode(input.getRptCode());

            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String formattedCurrentDate = currentDate.format(dateFormatter);
            rptLogsIn.setEndDate(formattedCurrentDate);

            LocalTime currentTime = LocalTime.now();
            DateTimeFormatter tiemFormatter = DateTimeFormatter.ofPattern("HHmmss");
            String formattedCurrentTime = currentTime.format(tiemFormatter);
            rptLogsIn.setEndTime(formattedCurrentTime);

            rptLogsIn.setCreateEmpid(input.getCreateEmpid());
            rptLogsIn.setCreateStatus("CP");

            rptLogsIn.setCreateCount(new BigDecimal(input.getCreateCount()));
            rptLogsIn.setErrorCount(new BigDecimal(input.getErrorount()));

            RptList rptListOut = getRptList(input);
            rptLogsIn.setOpType(rptListOut.getOpType());

            if (!input.isOpBatchnoNull()) {
                rptLogsIn.setOpBatchno(input.getOpBatchno());
            }

            if (!input.isParamIntValuseNull()) {
                rptLogsIn.setParamIntValues(input.getParamIntValuse());
            }

            if (!input.isParamExtValuseNull()) {
                rptLogsIn.setParamExtValues(input.getParamExtValuse());
            }

            int count = rptLogsMapper.updateByPrimaryKeySelective(rptLogsIn);

            boolean updateFlg = false;
            if (count > 0) {
                updateFlg = true;
            }

            //update RPT_LOGS_DETLS.RPT_LOGS_STATUS to "E" if IsRerun
            if (input.getIsRerun().equals("Y")) {
                for (int i = 0; i < input.getDDataSet().size(); i++) {
                    RptLogsDetlsDynamicSqlSupport.RptLogsDetls rptLogsDetls = RptLogsDetlsDynamicSqlSupport.rptLogsDetls;
                    UpdateStatementProvider provider = update(rptLogsDetls)
                            .set(rptLogsDetls.rptLogsStatus).equalTo("E")
                            .where(rptLogsDetls.rptFileName, isEqualTo(input.getDDataSet().get(i).getRptFileName()))
                            .and(rptLogsDetls.rptLogsStatus, isEqualTo("U"))
                            .build()
                            .render(RenderingStrategies.MYBATIS3);
                    rptLogsDetlsMapper.update(provider);
                }
            }

            if (updateFlg) {
                for (int i = 0; i < input.getDDataSet().size(); i++) {
                    RptLogsDetls rptLogsDetls = new RptLogsDetls();
                    rptLogsDetls.setRptFileName(input.getDDataSet().get(i).getRptFileName());
                    rptLogsDetls.setBillOff(input.getDDataSet().get(i).getBillOff());
                    rptLogsDetls.setRptTimes(input.getDDataSet().get(i).getRptTimes());
                    rptLogsDetls.setBillMonth(input.getDDataSet().get(i).getBillMonth());
                    rptLogsDetls.setBillCycle(input.getDDataSet().get(i).getBillCycle());
                    rptLogsDetls.setRptDate(input.getDDataSet().get(i).getRptDate());
                    rptLogsDetls.setRptQuarter(input.getDDataSet().get(i).getRptQuarter());
                    rptLogsDetls.setOffAdmin(input.getDDataSet().get(i).getOffAdmin());
                    rptLogsDetls.setRptFilePath(rptListOut.getRptFilePath());

                    rptLogsDetls.setRptFileCount(new BigDecimal(input.getDDataSet().get(i).getRptFileCount()));
                    rptLogsDetls.setRptFileAmt(input.getDDataSet().get(i).getRptFileAmt());
                    rptLogsDetls.setRptSecretMark(input.getDDataSet().get(i).getRptSecretMark());
                    rptLogsDetls.setRptLogsId(new BigDecimal(input.getRptLogsId()));
                    rptLogsDetls.setRptLogsStatus("U");
                    rptLogsDetlsMapper.insert(rptLogsDetls);
                }
            }

            return true;

        } catch (Exception e) {
            log.error("", e);
        }

        return false;
    }
    @Override
    public RptList getRptList(RptLogAfterExecuteInputStr input) {
        //query RPT_LIST
        SelectStatementProvider rptListProvider =
                select(rptList.allColumns())
                        .from(rptList)
                        .where(rptList.rptCode, isEqualTo(input.getRptCode()))
                        .build().render(RenderingStrategies.MYBATIS3);
        RptList rptListOut = rptListMapper.selectOne(rptListProvider).get();
        return rptListOut;
    }

    @Override
    public List<BatchRptQueryOut> batchRptQuery(BatchRptQueryIn input) {
//        序號 	資料表 	欄位
//        1	【RPT_LOGS_DETLS】
//              RPT_DATE
//        2		BILL_OFF
//        3		OFF_ADMIN
//        4		RPT_TIMES

//        5	【RPT_LOGS】
//              PT_LOGS_ID

//        6	【RPT_LIST】
//              RPT_CODE
//        7		FUN_CODE

        //1. find rpt_logs_id from rpt_logs with rptcode
        List<RptLogs> rptLogsList = new ArrayList<>();
        List<RptList> rptCodeListByFunCode = new ArrayList<>();
        List<String> rptCodeListByFunCodeStr = new ArrayList<>();

        if (input.getRptCategory() != null) {
//            String rptCode = converRptCategorToRptCode(input.getRptCategory());

            //get rpt code list from fun code
            rptCodeListByFunCode = getRptCodeByFunCode(input.getRptCategory());
            for(int i = 0; i <rptCodeListByFunCode.size(); i++){
                String rptCode = rptCodeListByFunCode.get(i).getRptCode();
                rptCodeListByFunCodeStr.add(rptCode);
            }
            rptLogsList = null;

        }else{
            rptCodeListByFunCodeStr = null;
            rptLogsList = null;

        }

        //2. find rpt_logs_detls with all other condition include rpt_logs_id list from 1.
        List<RptLogsDetls> rptLogsDetlsResults = new ArrayList<>();
        //query wtih web input condition
        //query RPT_LOGS_DETLS
        if (!input.isRrptSecretMarkNull() && input.getRptSecretMark().equals("N")) {
            SelectStatementProvider rptLogsDetlsSecretMarkProvider = select(rptLogsDetls.withAlias("detls").allColumns()).from(rptLogsDetls.withAlias("detls"))
                    .where(rptLogsDetls.withAlias("detls").rptDate, isGreaterThanOrEqualToWhenPresent(input.getRptDateStart()))
                    .and(rptLogsDetls.withAlias("detls").rptDate, isLessThanOrEqualToWhenPresent(input.getRptDateEnd()))
                    .and(rptLogsDetls.withAlias("detls").billOff, isEqualToWhenPresent(input.getBillOff()))
                    .and(rptLogsDetls.withAlias("detls").rptTimes, isEqualToWhenPresent(input.getRptTimes()))
                    .and(rptLogsDetls.withAlias("detls").rptSecretMark, isEqualTo(input.getRptSecretMark()))
                    .and(rptLogsDetls.withAlias("detls").billCycle, isEqualToWhenPresent(input.getRptPeriod()))
                    .and(rptLogsDetls.withAlias("detls").billMonth, isEqualToWhenPresent(input.getRptYearMonth()))
                    .and(exists(select(rptLogs.withAlias("logs").allColumns()).from(rptLogs.withAlias("logs"))
                            .where(rptLogs.withAlias("logs").rptLogsId, isEqualTo(rptLogsDetls.withAlias("detls").rptLogsId))
                            .and(rptLogs.withAlias("logs").rptCode, isInWhenPresent(rptCodeListByFunCodeStr))))
                    .and(rptLogsDetls.withAlias("detls").rptLogsStatus, isEqualTo("U"))
                    .build().render(RenderingStrategies.MYBATIS3);
            rptLogsDetlsResults = rptLogsDetlsMapper.selectMany(rptLogsDetlsSecretMarkProvider);
        } else {
            SelectStatementProvider rptLogsDetlsProvider = select(rptLogsDetls.withAlias("detls").allColumns()).from(rptLogsDetls.withAlias("detls"))
                    .where(rptLogsDetls.withAlias("detls").rptDate, isGreaterThanOrEqualToWhenPresent(input.getRptDateStart()))
                    .and(rptLogsDetls.withAlias("detls").rptDate, isLessThanOrEqualToWhenPresent(input.getRptDateEnd()))
                    .and(rptLogsDetls.withAlias("detls").billOff, isEqualToWhenPresent(input.getBillOff()))
                    .and(rptLogsDetls.withAlias("detls").rptTimes, isEqualToWhenPresent(input.getRptTimes()))
                    .and(rptLogsDetls.withAlias("detls").billCycle, isEqualToWhenPresent(input.getRptPeriod()))
                    .and(rptLogsDetls.withAlias("detls").billMonth, isEqualToWhenPresent(input.getRptYearMonth()))
                    .and(exists(select(rptLogs.withAlias("logs").allColumns()).from(rptLogs.withAlias("logs"))
                            .where(rptLogs.withAlias("logs").rptLogsId, isEqualTo(rptLogsDetls.withAlias("detls").rptLogsId))
                            .and(rptLogs.withAlias("logs").rptCode, isInWhenPresent(rptCodeListByFunCodeStr))))
                    .and(rptLogsDetls.withAlias("detls").rptLogsStatus, isEqualTo("U"))
                    .build().render(RenderingStrategies.MYBATIS3);
            rptLogsDetlsResults = rptLogsDetlsMapper.selectMany(rptLogsDetlsProvider);
        }


        //query RPT_LOGS
        List<RptLogs> rptLogsFinalList = new ArrayList<>();
        if(rptLogsList==null){
            for (int i = 0; i < rptLogsDetlsResults.size(); i++) {
                SelectStatementProvider rptLogsProvider = select(rptLogs.allColumns()).from(rptLogs)
                        .where(rptLogs.rptLogsId, isEqualTo(rptLogsDetlsResults.get(i).getRptLogsId()))
                        .build().render(RenderingStrategies.MYBATIS3);

                RptLogs rptLogsResult = rptLogsMapper.selectOne(rptLogsProvider).get();
                rptLogsFinalList.add(rptLogsResult);
            }
        }else{
            rptLogsFinalList = rptLogsList;
        }


//        List<RptLogs> uniquteRptLogList = new ArrayList<>();
//        uniquteRptLogList = rptLogsList.stream().distinct().collect(Collectors.toList());

//        序號 	欄位名稱	長度控制	說明
//        1	{檔名}	30	【RPT_LOGS_DETLS】.「RPT_FILE_NAME」
//        2	{報表名稱}	30	【RPT_LIST】.「RPT_NAME」
//        3	{日期}	8	【RPT_LOGS_DETLS】.「RPT_DATE」
//        4	{筆數}	9	【RPT_LOGS_DETLS】.「RPT_FILE_COUNT」
//        5	{金額}	9	【RPT_LOGS_DETLS】.「RPT_FILE_AMT」
//        6	{報表狀態}	6	【RPT_LOGS】.「CREATE_STATUS」
//        1.	當本欄位值為CP時可開放點擊後，其餘情況請反白。
//        2.	點擊後方可觸發第三部分功能


        //query RPT_LIST
        List<RptList> rptListResults = new ArrayList<>();
        for (int i = 0; i < rptLogsFinalList.size(); i++) {
            SelectStatementProvider rptListProvider = select(rptList.allColumns()).from(rptList)
                    .where(rptList.rptCode, isEqualTo(rptLogsFinalList.get(i).getRptCode()))
                    .build().render(RenderingStrategies.MYBATIS3);

            RptList rptListResult = rptListMapper.selectOne(rptListProvider).get();
            rptListResults.add(rptListResult);
        }
//        List<RptList> uniquteRptListRows = new ArrayList<>();
//        uniquteRptListRows = rptListResults.stream().distinct().collect(Collectors.toList());

        List<BatchRptQueryOut> batchRptQueryOuts = new ArrayList<>();

        for (int i = 0; i < rptLogsDetlsResults.size(); i++) {
            BatchRptQueryOut batchRptQueryOut = new BatchRptQueryOut();
            batchRptQueryOut.setRptFileName(rptLogsDetlsResults.get(i).getRptFileName());
            batchRptQueryOut.setRptDate(rptLogsDetlsResults.get(i).getRptDate());
            batchRptQueryOut.setRptFileCount(rptLogsDetlsResults.get(i).getRptFileCount().toString());

            if (!rptLogsDetlsResults.get(i).isRptFileAmtNull()) {
                batchRptQueryOut.setRptAmt(rptLogsDetlsResults.get(i).getRptFileAmt().toString());
            }

            RptLogs rptLogsOut = getRptLogsByRptLogsDetls(rptLogsDetlsResults.get(i), rptLogsFinalList);
            System.out.println("getCreateStatus is : "+rptLogsOut.getCreateStatus());
            batchRptQueryOut.setRptStatus(rptLogsOut.getCreateStatus().equals("CP")?"完成":"執行中");

            RptList rptListOut = getRptListByRptLogs(rptLogsOut, rptListResults);
            batchRptQueryOut.setRptName(rptListOut.getRptName());
            batchRptQueryOut.setRptLogsId(rptLogsDetlsResults.get(i).getRptLogsId().toString());
            batchRptQueryOut.setRptSecretMark(rptLogsDetlsResults.get(i).getRptSecretMark());

            batchRptQueryOuts.add(batchRptQueryOut);
        }

        return batchRptQueryOuts;
    }

//    private String converRptCategorToRptCode(String rptCategory) {
//        String rptCode = null;
//        if(rptCategory.equals("1")){
//            rptCode = "BPGUSUB";
//        }
//        return rptCode;
//    }

    private RptLogs getRptLogsByRptLogsDetls(RptLogsDetls rptLogsDetlsIn, List<RptLogs> rptLogsRows) {

        BigDecimal rptLogsId = rptLogsDetlsIn.getRptLogsId();

        for (int i = 0; i < rptLogsRows.size(); i++) {
            if (rptLogsRows.get(i).getRptLogsId().compareTo(rptLogsId) == 0) {
                return rptLogsRows.get(i);
            }
        }

        return null;
    }

    private RptList getRptListByRptLogs(RptLogs rptLogsIn, List<RptList> rptListRows) {

        String rptCode = rptLogsIn.getRptCode();

        for (int i = 0; i < rptListRows.size(); i++) {
            if (rptListRows.get(i).getRptCode().equals(rptCode)) {
                return rptListRows.get(i);
            }
        }

        return null;
    }

    @Override
    public  List<RptCategoryOut> getRptCodeOptions() {
        //query RPT_LIST
        SelectStatementProvider rptListProvider =
                select(rptList.allColumns())
                        .from(rptList)
                        .build().render(RenderingStrategies.MYBATIS3);
        List<RptList> rptListOut = rptListMapper.selectMany(rptListProvider);

        List<RptCategoryOut> rptCategoryOuts = new ArrayList<>();

        RptCategoryOut rptCategoryOutForAll = new RptCategoryOut();
        rptCategoryOutForAll.setName("---全部---");
        rptCategoryOutForAll.setId("0");
        rptCategoryOutForAll.setCode("0");
        rptCategoryOuts.add(rptCategoryOutForAll);

        for(int i =0 ;i<rptListOut.size();i++){
            RptCategoryOut rptCategoryOut = new RptCategoryOut();
            rptCategoryOut.setId(rptListOut.get(i).getRptListId().toString());
            rptCategoryOut.setCode(rptListOut.get(i).getRptCode());
            rptCategoryOut.setName(rptListOut.get(i).getRptName());

            rptCategoryOuts.add(rptCategoryOut);
        }
        return rptCategoryOuts;
    }

    public List<RptList> getRptCodeByFunCode(String funCode) {
        //query rpt code
        SelectStatementProvider rptListProvider =
                select(rptList.rptCode)
                        .from(rptList)
                        .where(rptList.funCode, isEqualTo(funCode))
                        .build().render(RenderingStrategies.MYBATIS3);
        List<RptList> rptCode = rptListMapper.selectMany(rptListProvider);
        return rptCode;
    }

    @Override
    public  List<RptCategoryOut> getFunCodeOptions() {
        //query RPT_LIST
        SelectStatementProvider rptListProvider =
                selectDistinct(rptList.funCode)
                        .from(rptList)
                        .build().render(RenderingStrategies.MYBATIS3);
        List<RptList> rptListOut = rptListMapper.selectMany(rptListProvider);

        List<RptCategoryOut> rptCategoryOuts = new ArrayList<>();

        RptCategoryOut rptCategoryOutForAll = new RptCategoryOut();
        rptCategoryOutForAll.setName("---全部---");
        //rptCategoryOutForAll.setId("0");
        rptCategoryOutForAll.setCode("0");
        rptCategoryOuts.add(rptCategoryOutForAll);

        for(int i =0 ;i<rptListOut.size();i++){
            RptCategoryOut rptCategoryOut = new RptCategoryOut();
            //rptCategoryOut.setId(rptListOut.get(i).getRptListId().toString());
            rptCategoryOut.setCode(rptListOut.get(i).getFunCode());
            rptCategoryOut.setName(NameMapping.getRptFunctionName(rptListOut.get(i).getFunCode()));
            rptCategoryOuts.add(rptCategoryOut);
        }
        return rptCategoryOuts;
    }
}
