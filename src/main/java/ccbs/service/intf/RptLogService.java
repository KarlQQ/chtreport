package ccbs.service.intf;

import ccbs.dao.core.entity.RptList;
import ccbs.dao.core.entity.RptLogs;
import ccbs.model.batch.RptLogAfterExecuteInputStr;
import ccbs.model.online.*;

import java.util.List;

public interface RptLogService {

    public boolean insertRptLogRecordBeforeExecute(RptLogs input);

    public RptLogs getRptLogRecord(RptLogs rptLogsIn);

    public boolean updateRptLogRecordAndRptLogDetailsAfterExecute(RptLogAfterExecuteInputStr input);

    public RptList getRptList(RptLogAfterExecuteInputStr input);

    public List<BatchRptQueryOut> batchRptQuery(BatchRptQueryIn input) ;

    public List<RptCategoryOut> getRptCodeOptions(String funCode);

    public List<RptCategoryOut> getFunCodeOptions();
}
