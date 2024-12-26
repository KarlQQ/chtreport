package ccbs.conf.aop;

import ccbs.model.batch.RptLogAfterExecuteInputStr;
import ccbs.model.batch.RptLogBeforeExecuteInputStr;
import ccbs.model.batch.RptLogBeforeExecuteOutputStr;
import ccbs.model.batch.dData;
import ccbs.util.StringUtils;
import ccbs.util.comm03.Comm03Service;
import cht.bss.externalcheck.util.JsonLog;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class RptLogAspect {
  public static final String KEY_UUID = "uuid";
  public static final String KEY_JOB_ID = "jobId";
  @Value("${ccbs.jsonLogPath}") private String jsonLogPath;

  @Autowired private Comm03Service comm03Service;
  private ThreadLocal<String> idHolder = new ThreadLocal<>();
  private ThreadLocal<Date> startTimeHolder = new ThreadLocal<>();

  @Before("@annotation(rptLog)")
  public void logBeforeExecution(JoinPoint joinPoint, RptLog rptLog) {
    MDC.put(KEY_UUID, UUID.randomUUID().toString());
    MDC.put(KEY_JOB_ID, rptLog.rptCode());
    log.info("批次執行開始");
    log.info("批次執行參數: {}",
        StringUtils.filterJson(joinPoint.getArgs(), Arrays.asList(rptLog.excludes())));
    startTimeHolder.set(new Date());
    try {
      RptLogBeforeExecuteOutputStr out = comm03Service.COMM03_0001(
          RptLogBeforeExecuteInputStr.builder().rptCode(rptLog.rptCode()).build());
      idHolder.set(out.getRptLogsId());
    } catch (Exception e) {
      log.error("寫入 RPT_LOG 失敗", e);
      throw e;
    }
  }

  @AfterReturning(value = "@annotation(rptLog)", returning = "response")
  public void logAfterExecution(JoinPoint joinPoint, RptLog rptLog, Object response) {
    RptLogAfterExecuteInputStr input;
    if (response instanceof WraperResponse) {
      WraperResponse wraperResponse = (WraperResponse) response;
      input = wraperResponse.getInput();
      try {
        JsonLog jsonLog = JsonLog.create(rptLog.rptCode(), jsonLogPath);
        jsonLog.write("", rptLog.rptCode(), wraperResponse.getFilename(),
            JsonLog.TagType.OUT.name(), "REAL-CNT", input.getDDataSet().size());
        jsonLog.write("", rptLog.rptCode(), wraperResponse.getFilename(),
            JsonLog.TagType.OUT.name(), "ERR-CNT", input.getErrorCount());
        jsonLog.writeJsonLog(idHolder.get(), String.valueOf(Thread.currentThread().getId()),
            startTimeHolder.get(), "ccbsap", false, startTimeHolder.get(), new Date());
      } catch (Throwable e) {
        log.error("寫入 JSON_LOG 失敗", e);
      }
    } else {
      input = RptLogAfterExecuteInputStr.builder()
                  .createCount(1)
                  .errorCount(0)
                  .dDataSet(Collections.emptyList())
                  .build();
    }

    input.setRptLogsId(idHolder.get());
    input.setRptCode(rptLog.rptCode());
    try {
      comm03Service.COMM03_0002(input);
      log.info("批次執行完成");
    } catch (Exception e) {
      log.error("寫入 RPT_LOG 失敗", e);
      throw e;
    } finally {
      startTimeHolder.remove();
      idHolder.remove();
    }
  }

  @AfterThrowing(value = "@annotation(rptLog)", throwing = "ex")
  public void logException(JoinPoint joinPoint, RptLog rptLog, Throwable ex) {
    RptLogAfterExecuteInputStr input = RptLogAfterExecuteInputStr.builder()
                                           .rptCode(rptLog.rptCode())
                                           .rptLogsId(idHolder.get())
                                           .createCount(0)
                                           .errorCount(1)
                                           .dDataSet(Collections.emptyList())
                                           .build();
    try {
      comm03Service.COMM03_0002(input);
      log.error("批次執行失敗", ex);
    } catch (Exception e) {
      log.error("寫入 RPT_LOG 失敗", e);
      throw e;
    } finally {
      idHolder.remove();
    }
  }

  public static interface WraperResponse {
    public RptLogAfterExecuteInputStr getInput();
    public String getFilename();
  }

  @Builder
  @Getter
  public static class Result implements RptLogAspect.WraperResponse {
    private String rptCode;
    @Builder.Default private Integer errorCount = 0;
    private String isRerun;
    private String opBatchno;
    private File reportFile;
    private List<dData> dDataList;
    private String rptFileName;
    private String billMonth;
    private String rptDate;
    private Integer rptFileCount;

    @Override
    public RptLogAfterExecuteInputStr getInput() {
      if (dDataList == null) {
        dDataList = List.of(dData.builder()
                                .rptFileName(rptFileName)
                                .billMonth(billMonth)
                                .rptDate(rptDate)
                                .rptFileCount(rptFileCount)
                                .build());
      }
      return RptLogAfterExecuteInputStr.builder()
          .rptCode(rptCode)
          .createEmpid("SYSTEM")
          .createCount(dDataList.size())
          .errorCount(errorCount)
          .opBatchno(opBatchno)
          .isRerun(isRerun)
          .dDataSet(dDataList)
          .build();
    }

    @Override
    public String getFilename() {
      return rptFileName;
    }
  }
}
