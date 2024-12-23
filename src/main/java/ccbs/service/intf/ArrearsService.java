package ccbs.service.intf;

import ccbs.model.batch.BatchArrearsInputStr;
import ccbs.model.batch.BatchSimpleRptInStr;
import ccbs.model.batch.BatchSimpleRptInStrWithItemType;
import ccbs.model.batch.BatchSimpleRptInStrWithOpid;
import ccbs.model.batch.BatchSimpleRptInStrWithType;
import ccbs.model.batch.SingleArrearsInputStr;
import ccbs.model.batch.SingleArrearsOutputStr;
import ccbs.model.batch.dData;
import ccbs.service.intf.Bp01Service.Result;

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
  
}
