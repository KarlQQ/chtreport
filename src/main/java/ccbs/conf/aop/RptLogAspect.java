package ccbs.conf.aop;

import ccbs.model.batch.RptLogAfterExecuteInputStr;
import ccbs.model.batch.RptLogBeforeExecuteInputStr;
import ccbs.model.batch.RptLogBeforeExecuteOutputStr;
import ccbs.util.comm03.Comm03Service;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.stream.IntStream;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RptLogAspect {
  @Autowired private Comm03Service comm03Service;

  private ThreadLocal<String> idHolder = new ThreadLocal<>();

  @Before("@annotation(rptLogExecution)")
  public void logBeforeExecution(JoinPoint joinPoint, RptLogExecution rptLogExecution) {
    RptLogBeforeExecuteOutputStr out = comm03Service.COMM03_0001(
        RptLogBeforeExecuteInputStr.builder().rptCode(rptLogExecution.rptCode()).build());
    idHolder.set(out.getRptLogsId());
  }

  @SuppressWarnings("unchecked")
  @AfterReturning(value = "@annotation(rptLogExecution)", returning = "response")
  public void logAfterExecution(
      JoinPoint joinPoint, RptLogExecution rptLogExecution, Object response) {
    RptLogAfterExecuteInputStr input;
    if (response instanceof WraperResponse) {
      WraperResponse waperResponse = (WraperResponse) response;
      input = waperResponse.getInput();
    } else {
      input = RptLogAfterExecuteInputStr.builder()
                  .rptCode(rptLogExecution.rptCode())
                  .rptLogsId(idHolder.get())
                  .createCount(1)
                  .errorount(0)
                  .dDataSet(Collections.EMPTY_LIST)
                  .build();
      if (!rptLogExecution.paramMapping().isBlank()) {
        Object[] args = joinPoint.getArgs();
        String[] mapping = rptLogExecution.paramMapping().split(",");
        IntStream.range(0, mapping.length).forEach(index -> {
          String name = mapping[index];
          if (!name.isBlank() && index < args.length) {
            try {
              Field field = RptLogAfterExecuteInputStr.class.getDeclaredField(name);
              field.setAccessible(true);
              field.set(input, args[index]);
            } catch (NoSuchFieldException | IllegalAccessException e) {
            }
          }
        });
      }
    }

    input.setRptLogsId(idHolder.get());
    comm03Service.COMM03_0002(input);
    idHolder.remove();
  }

  public static interface WraperResponse { public RptLogAfterExecuteInputStr getInput(); }

  // @After("@annotation(rptLogExecution)")
  // public void logAfterExecution(JoinPoint joinPoint, RptLogExecution rptLogExecution) {
  //   @SuppressWarnings("unchecked")
  //   RptLogAfterExecuteInputStr input = RptLogAfterExecuteInputStr.builder()
  //                                          .rptCode(rptLogExecution.rptCode())
  //                                          .rptLogsId(idHolder.get())
  //                                          .createCount(0)
  //                                          .errorount(0)
  //                                          .dDataSet(Collections.EMPTY_LIST)
  //                                          .build();
  //   if (!rptLogExecution.paramMapping().isBlank()) {
  //     Object[] args = joinPoint.getArgs();
  //     String[] mapping = rptLogExecution.paramMapping().split(",");
  //     IntStream.range(0, mapping.length).forEach(index -> {
  //       String name = mapping[index];
  //       if (!name.isBlank() && index < args.length) {
  //         try {
  //           Field field = RptLogAfterExecuteInputStr.class.getDeclaredField(name);
  //           field.setAccessible(true);
  //           field.set(input, args[index]);
  //         } catch (NoSuchFieldException | IllegalAccessException e) {
  //         }
  //       }
  //     });
  //   }

  //   comm03Service.COMM03_0002(input);
  //   idHolder.remove();
  // }
}
