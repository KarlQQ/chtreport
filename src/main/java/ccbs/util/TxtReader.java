package ccbs.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Data;

@Data
public class TxtReader<T> {
  private Class<T> clazz;
  private List<TxtReaderConfig> txConfigs;
  private Map<String, Method> setterCache;

  public TxtReader(Class<T> clazz, List<TxtReaderConfig> txConfigs) {
    this.clazz = clazz;
    this.txConfigs = txConfigs;

    Map<String, Method> setterCache = new ConcurrentHashMap<>();
    txConfigs.forEach(txConfig -> {
      String setterName = "set" + txConfig.getField().substring(0, 1).toUpperCase()
          + txConfig.getField().substring(1);
      Arrays.stream(clazz.getDeclaredMethods())
          .filter(m -> m.getName().equals(setterName))
          .findFirst()
          .ifPresent(setter -> setterCache.put(txConfig.getField(), setter));
    });

    this.setterCache = setterCache;
  }

  public List<T> readTxtToPojo(List<String> lines) throws Exception {
    List<T> records = new ArrayList<>();
    for (String line : lines) {
      T record = clazz.getDeclaredConstructor().newInstance();
      for (TxtReaderConfig txConfig : txConfigs) {
        Method setter = setterCache.get(txConfig.getField());
        if (setter != null) {
          String value = line.substring(txConfig.getPosition(),
              Math.min(txConfig.getPosition() + txConfig.getLength(), line.length()));
          if (txConfig.isTrim()) {
            value = value.trim();
          }

          setter.invoke(record, convertValue(value, txConfig.getClazz()));
        }
      }
      records.add(record);
    }

    return records;
  }

  private Object convertValue(String value, String className) throws ClassNotFoundException {
    Class<?> targetClass = Class.forName(className);
    if (targetClass == String.class) {
      return value;
    } else if (targetClass == Integer.class) {
      return Integer.parseInt(value);
    } else if (targetClass == Double.class) {
      return Double.parseDouble(value);
    } else if (targetClass == BigDecimal.class) {
      return new BigDecimal(value);
    }
    throw new IllegalArgumentException("Unsupported class type: " + className);
  }

  @Data
  public static class TxtReaderConfig {
    private String field;
    private Integer length = 0;
    private Integer position = 0;
    private String clazz = "java.lang.String";
    private boolean trim = true;
  }
}
