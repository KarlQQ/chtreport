package ccbs.util.comm03;

import ccbs.dao.core.entity.RptLogs;
import ccbs.service.intf.RptLogService;
import ccbs.model.batch.RptLogAfterExecuteInputStr;
import ccbs.model.batch.RptLogAfterExecuteOutputStr;
import ccbs.model.batch.RptLogBeforeExecuteInputStr;
import ccbs.model.batch.RptLogBeforeExecuteOutputStr;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class Comm03ServiceImpl implements Comm03Service {

    @Autowired
    private RptLogService rptLogService;

    //        RQBP004_(共用)報表產製日誌(執行前)
    public RptLogBeforeExecuteOutputStr COMM03_0001 (RptLogBeforeExecuteInputStr inputStr){

        String rptCode = inputStr.getRptCode();

//        2.	並處理該記錄以下欄位
//        欄位 	值
//        RPT_CODE	傳入值{RPT_CODE }
//        START_DATE	系統日
//        START_TIME	系統時
//        CREATE_STATUS	“ IP”

        RptLogs rptLogsIn = new RptLogs();
        RptLogs rptLogsOut = null;

        rptLogsIn.setRptCode(rptCode);

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedCurrentDate = currentDate.format(dateFormatter);

        rptLogsIn.setStartDate(formattedCurrentDate);


        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter tiemFormatter = DateTimeFormatter.ofPattern("HHmmss");
        String formattedCurrentTime = currentTime.format(tiemFormatter);

        rptLogsIn.setStartTime(formattedCurrentTime);

        rptLogsIn.setCreateStatus("IP");

        boolean insertFlg =  rptLogService.insertRptLogRecordBeforeExecute(rptLogsIn);

        if(insertFlg){
            //get RPT_LOGS_ID from RPT_LOGS
            rptLogsOut = rptLogService.getRptLogRecord(rptLogsIn);
        }


        RptLogBeforeExecuteOutputStr rptLogBeforeExecuteOutputStr = new RptLogBeforeExecuteOutputStr();
        if(rptLogsOut != null){
            rptLogBeforeExecuteOutputStr.setProcResult("00");
            rptLogBeforeExecuteOutputStr.setProcDescription("成功");
            rptLogBeforeExecuteOutputStr.setRptLogsId(rptLogsOut.getRptLogsId().toString());
        }else {
            rptLogBeforeExecuteOutputStr.setProcResult("QQ");
            rptLogBeforeExecuteOutputStr.setProcDescription("失敗");
        }

        return rptLogBeforeExecuteOutputStr;
    }

    //        RQBP011_(共用)報表產製日誌(執行後)
    public RptLogAfterExecuteOutputStr COMM03_0002 (RptLogAfterExecuteInputStr inputStr){

        boolean updateFlg = rptLogService.updateRptLogRecordAndRptLogDetailsAfterExecute(inputStr);

        RptLogAfterExecuteOutputStr rptLogAfterExecuteOutputStr = new RptLogAfterExecuteOutputStr();

        if(updateFlg){
            rptLogAfterExecuteOutputStr.setProcResult("00");
            rptLogAfterExecuteOutputStr.setProcDescription("成功");
        }else {
            rptLogAfterExecuteOutputStr.setProcResult("QQ");
            rptLogAfterExecuteOutputStr.setProcDescription("失敗");
        }

        return rptLogAfterExecuteOutputStr;
    }
}
