package ccbs.util.comm03;

import ccbs.model.batch.RptLogAfterExecuteInputStr;
import ccbs.model.batch.RptLogAfterExecuteOutputStr;
import ccbs.model.batch.RptLogBeforeExecuteInputStr;
import ccbs.model.batch.RptLogBeforeExecuteOutputStr;

public interface Comm03Service {

    public RptLogBeforeExecuteOutputStr COMM03_0001 (RptLogBeforeExecuteInputStr inputStr);
    public RptLogAfterExecuteOutputStr COMM03_0002 (RptLogAfterExecuteInputStr inputStr);

}
