package ccbs.conf.aop;

import ccbs.util.StringUtils;
import java.util.Arrays;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class RptLogExternalAspect {
  public static final String KEY_UUID = "uuid";
  public static final String KEY_JOB_ID = "jobId";
  public static final String KEY_OUTER_URI = "outerUri";

  @Around("@annotation(RptLogExternal)")
  public Object logExternalApiCall(ProceedingJoinPoint joinPoint, RptLogExternal rptLogExternal)
      throws Throwable {
    long startTime = System.currentTimeMillis();
    MDC.put(KEY_UUID, UUID.randomUUID().toString());
    MDC.put(KEY_JOB_ID, rptLogExternal.rptCode());
    MDC.put(KEY_OUTER_URI, rptLogExternal.uri());
    log.info("外部 API 調用開始");
    log.info("外部 API 請求: {}",
        StringUtils.filterJson(joinPoint.getArgs(), Arrays.asList(rptLogExternal.excludes())));

    try {
      Object result = joinPoint.proceed();
      long duration = System.currentTimeMillis() - startTime;
      log.info("外部 API 調用完成, 耗時: {}ms, Result: {}", duration, result);
      return result;
    } catch (Throwable ex) {
      log.error("外部 API 調用失敗", ex);
      throw ex;
    }
  }
}