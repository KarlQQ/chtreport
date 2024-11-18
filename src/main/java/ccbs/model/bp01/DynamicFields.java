package ccbs.model.bp01;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DynamicFields {
  @Builder.Default private Map<String, String> fieldDefinition = new HashMap<>();

  public <T> Map<String, T> load(String aggregatedData, Function<String, T> fun) {
    Map<String, String> resultMap = new HashMap<>();
    for (String entry : aggregatedData.split(",")) {
      String[] keyValue = entry.split("=");
      if (keyValue.length == 2) {
        resultMap.put(keyValue[0], keyValue[1]);
      }
    }
    return load(resultMap, fun);
  }

  public <T> Map<String, T> load(Map<String, String> resultMap, Function<String, T> fun) {
    Map<String, T> dnamicFields = new HashMap<>();
    for (String key : fieldDefinition.keySet()) {
      dnamicFields.put(key, fun.apply(resultMap.get(key)));
    }
    return dnamicFields;
  }
}
