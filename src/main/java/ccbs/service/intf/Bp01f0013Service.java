package ccbs.service.intf;

import ccbs.service.intf.Bp01Service.Result;

public interface Bp01f0013Service {
  public Result process(String jobId, String opcDate, String opcYearMonth, String isRerun);
}
