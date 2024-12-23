package ccbs.util;

import java.math.BigDecimal;

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

}