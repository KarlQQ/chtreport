package ccbs.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Data;

@Data
public class TxtWriter<T> {
  private Class<T> clazz;
  private List<TxtWriterConfig> txConfigs;
  private Map<String, Method> getterCache;
  private Integer totalLength;

  public TxtWriter(Class<T> clazz, List<TxtWriterConfig> txConfigs) {
    this.clazz = clazz;
    this.txConfigs = txConfigs;

    Map<String, Method> getterCache = new ConcurrentHashMap<>();
    AtomicInteger totalLength = new AtomicInteger(0);
    txConfigs.forEach(txConfig -> {
      String getterName = "get" + txConfig.getField().substring(0, 1).toUpperCase()
          + txConfig.getField().substring(1);
      Arrays.stream(clazz.getDeclaredMethods())
          .filter(m -> m.getName().equals(getterName))
          .findFirst()
          .ifPresent(getter -> getterCache.put(txConfig.getField(), getter));

      int endPosition = txConfig.getPosition() + txConfig.getLength() - 1;
      totalLength.updateAndGet(old -> endPosition > old ? endPosition : old);
    });
    this.getterCache = getterCache;
    this.totalLength = totalLength.get();
  }

  public List<String> readPojoToTxt(List<T> inputs) throws Exception {
    List<String> records = new ArrayList<>();
    for (T input : inputs) {
      StringBuilder record = new StringBuilder(totalLength);
      for (TxtWriterConfig txConfig : txConfigs) {
        Method getter = getterCache.get(txConfig.getField());
        if (getter != null) {
          String value = getter.invoke(input).toString();
          value =
              String.format(txConfig.isPadLeft() ? "%" : "%-" + txConfig.getLength() + "s", value);
          record.replace(
              txConfig.getPosition(), txConfig.getPosition() + txConfig.getLength(), value);
        }
      }
      records.add(record.toString());
    }

    return records;
  }

  @Data
  public static class TxtWriterConfig {
    private String field;
    private Integer length = 0;
    private Integer position = 0;
    private boolean padLeft = false;
  }
}
