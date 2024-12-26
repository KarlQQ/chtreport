package ccbs.service.intf;

import ccbs.conf.aop.RptLogAspect.Result;
import ccbs.model.batch.BatchArrearsInputStr;
import ccbs.model.batch.BatchSimpleRptInStr;
import ccbs.model.batch.BatchSimpleRptInStrWithItemType;
import ccbs.model.batch.BatchSimpleRptInStrWithOpid;
import ccbs.model.batch.BatchSimpleRptInStrWithType;
import ccbs.model.batch.SingleArrearsInputStr;
import ccbs.model.batch.SingleArrearsOutputStr;
import ccbs.model.batch.dData;

public interface ArrearsService {
  public SingleArrearsOutputStr singleArrearsQuery(SingleArrearsInputStr input);

  public void batchArrearsQuery(BatchArrearsInputStr input) throws Exception;

  public Result batchBP221D6Rpt(BatchSimpleRptInStr input) throws Exception;

  public Result batchBP2240D1Rpt(BatchSimpleRptInStr input) throws Exception;

  public Result batchBP2230D4D5Rpt(BatchSimpleRptInStr input) throws Exception;

  public dData batchBP2230D4Rpt(BatchSimpleRptInStr input) throws Exception;

  public dData batchBP2230D5Rpt(BatchSimpleRptInStr input) throws Exception;

  public Result batchBP2230D6Rpt(BatchSimpleRptInStr input) throws Exception;

  public Result batchBP222OTRpt(BatchSimpleRptInStr input) throws Exception;

  public Result batchBP222OT2Rpt(BatchSimpleRptInStr input) throws Exception;

  public Result batchBP22TOTRpt(BatchSimpleRptInStr input) throws Exception;

  public Result batchBPGNERPRpt(BatchSimpleRptInStrWithType input) throws Exception;

  public Result batchBPOWE2WRpt(BatchSimpleRptInStrWithOpid input) throws Exception;

  public Result batchBPOWERpt(BatchSimpleRptInStrWithOpid input) throws Exception;

  public Result batchBPZ10Rpt(BatchSimpleRptInStrWithItemType input) throws Exception;
  
  public Result batchBPGNIDDARpt(String inputFileName, String opcYYYMM, String outputDate, String opcDate, String isRerun, String jobId) throws Exception;

  public Result batchBPGNIDDCRpt(String inputFileName, String opcYYYMM, String outputDate, String opcDate, String isRerun, String jobId) throws Exception;

  public Result batchBPGNIDD2ARpt(String inputFileName, String opcYYYMM, String outputDate, String opcDate, String isRerun, String jobId) throws Exception;

  public Result batchBPGNIDD2CRpt(String inputFileName, String opcYYYMM, String outputDate, String opcDate, String isRerun, String jobId) throws Exception;

  public Result batchBPGNIDMARpt(String inputFileName, String opcYYYMM, String outputDate, String opcDate, String isRerun, String jobId) throws Exception;

  public Result batchBPGNIDMCRpt(String inputFileName, String opcYYYMM, String outputDate, String opcDate, String isRerun, String jobId) throws Exception;

  public Result batchBPGNIDM2ARpt(String inputFileName, String opcYYYMM, String outputDate, String opcDate, String isRerun, String jobId) throws Exception;

  public Result batchBPGNIDM2CRpt(String inputFileName, String opcYYYMM, String outputDate, String opcDate, String isRerun, String jobId) throws Exception;
}
