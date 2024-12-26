package ccbs.service.intf;

import ccbs.model.online.Rqbp019Input;
import ccbs.conf.aop.RptLogAspect.Result;

public interface Rqbp019Service {
  public static final String RPT_CODE = "BPGSM010";

  public Result process(Rqbp019Input input) throws Exception;
}
