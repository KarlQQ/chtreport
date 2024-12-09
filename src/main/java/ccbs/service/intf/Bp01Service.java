package ccbs.service.intf;

import ccbs.conf.aop.RptLogAspect;
import ccbs.model.batch.RptLogAfterExecuteInputStr;
import ccbs.model.batch.dData;
import java.io.File;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

public interface Bp01Service {
  public Result process0003(String jobId, String opcDate, String opcYearMonth, String isRerun);

  public Result process0007(
      String jobId, String opcDate, String opcYearMonth, String isRerun, String inputType);

  public Result process0013(String jobId, String opcDate, String opcYearMonth, String isRerun);

  public Result process0015(String jobId, String opcDate, String opcYearMonth, String isRerun);

  @Builder
  @Getter
  public static class Result implements RptLogAspect.WraperResponse {
    private String rptCode;
    @Builder.Default private Integer errorount = 0;
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
          .errorount(errorount)
          .opBatchno(opBatchno)
          .isRerun(isRerun)
          .dDataSet(dDataList)
          .build();
    }
  }
}
