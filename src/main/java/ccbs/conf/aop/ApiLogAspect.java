package ccbs.conf.aop;

import ccbs.util.StringUtils;
import java.util.Arrays;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ApiLogAspect {
  public static final String KEY_UUID = "uuid";
  public static final String KEY_URI = "uri";

  @Before("@annotation(ApiLog)")
  public void logBeforeExecution(JoinPoint joinPoint, ApiLog apiLog) {
    MDC.put(KEY_UUID, UUID.randomUUID().toString());
    MDC.put(KEY_URI, apiLog.uri());
    log.info("API 調用開始");
    log.info("API 請求: {}",
        StringUtils.filterJson(joinPoint.getArgs(), Arrays.asList(apiLog.excludes())));
  }

  @AfterReturning(value = "@annotation(ApiLog)", returning = "response")
  public void logAfterExecution(JoinPoint joinPoint, RptLog rptLog, Object response) {
    log.info("API 調用完成");
  }

  @AfterThrowing(value = "@annotation(ApiLog)", throwing = "ex")
  public void logException(JoinPoint joinPoint, RptLog rptLog, Throwable ex) {
    log.error("API 調用失敗", ex);
  }
}
