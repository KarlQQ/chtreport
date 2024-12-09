package ccbs.service.intf;

import ccbs.model.batch.BatchArrearsInputStr;
import ccbs.model.batch.BatchSimpleRptInStr;
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
  
}
