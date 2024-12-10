package ccbs.service.intf;

import ccbs.data.entity.QueryAllNameAddrOfBillHistoryInput;
import ccbs.data.entity.QueryAllNameAddrOfBillHistoryOutput;

public interface WebServiceClient {
    QueryAllNameAddrOfBillHistoryOutput queryAllNameAddrOfBillHistory(QueryAllNameAddrOfBillHistoryInput input);
}