package ccbs.service.intf;

import ccbs.conf.aop.RptLogAspect.Result;

public interface Bp01f0013Service {
  public Result process(String jobId, String opcDate, String opcYearMonth, String isRerun);
}
