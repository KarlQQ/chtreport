package ccbs.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class StringUtils {
  public static String formatNumberWithCommas(BigDecimal number) {
    return "\"" + (number != null ? String.format("%,d", number.longValue()) : "0") + "\"";
  }

  public static String formatNumberWithCommasWithoutQuotes(BigDecimal number) {
    return number != null ? String.format("%,d", number.longValue()) : "0";
  }

  public static String formatStringForCSV(String string) {
    return "\"" + (string != null ? string : "") + "\"";
  }

  public static List<String> filterJson(Object[] objs, List<String> excludedPaths) {
    JsonFactory jsonFactory = new JsonFactory();
    return List.of(objs)
        .parallelStream()
        .map(obj -> {
          if (Objects.nonNull(excludedPaths) && excludedPaths.size() > 0) {
            try (StringWriter writer = new StringWriter();
                 JsonParser parser = jsonFactory.createParser(obj.toString());
                 JsonGenerator generator = jsonFactory.createGenerator(writer)) {
              filterFields(parser, generator, excludedPaths, "");
              generator.flush();
              return writer.toString();
            } catch (IOException e) {
            }
          }
          return obj.toString();
        })
        .toList();
  }

  private static void filterFields(JsonParser parser, JsonGenerator generator,
      List<String> excludedPaths, String currentPath) throws IOException {
    while (parser.nextToken() != null) {
      switch (parser.getCurrentToken()) {
        case START_OBJECT:
          generator.writeStartObject();
          String parentPath = currentPath.isEmpty() ? "" : currentPath + ".";
          while (parser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = parser.getCurrentName();
            parser.nextToken();
            String newPath = parentPath + fieldName;

            if (excludedPaths.stream().noneMatch(newPath::equals)) {
              generator.writeFieldName(fieldName);
              filterFields(parser, generator, excludedPaths, newPath);
            } else {
              parser.skipChildren();
            }
          }
          generator.writeEndObject();
          return;

        case START_ARRAY:
          generator.writeStartArray();
          while (parser.nextToken() != JsonToken.END_ARRAY) {
            filterFields(parser, generator, excludedPaths, currentPath);
          }
          generator.writeEndArray();
          return;

        default:
          generator.copyCurrentEvent(parser);
      }
    }
  }
}