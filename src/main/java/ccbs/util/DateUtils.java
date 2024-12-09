package ccbs.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalUnit;

public class DateUtils {
  public static String convertToRocDate(String opcDate) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
      LocalDate date = LocalDate.parse(opcDate, formatter);
      int rocYear = date.getYear() - 1911;
      String rocMonth = String.format("%02d", date.getMonthValue());
      String rocDay = String.format("%02d", date.getDayOfMonth());
      return rocYear + rocMonth + rocDay;
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("opcDate 格式不正確，必須為有效的 YYYYMMDD 日期");
    }
  }

  public static String convertToRocYearMonth(String opcYYYMM) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
      YearMonth date = YearMonth.parse(opcYYYMM, formatter);
      int rocYear = date.getYear() - 1911;
      String rocMonth = String.format("%02d", date.getMonthValue());
      return rocYear + rocMonth;
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("opcYYYMM 格式不正確，必須為有效的 YYYYMM 日期");
    }
  }

  public static String toRocYearMonth(String opcYYYMM, long amountToAdd, TemporalUnit unit) {
    LocalDate localDate = LocalDate.parse(opcYYYMM + "01", DateTimeFormatter.ofPattern("yyyyMMdd"));
    if (unit != null) {
      localDate.plus(amountToAdd, unit);
    }
    return localDate.format(DateTimeFormatter.ofPattern("yyyMM").withChronology(
        java.time.chrono.MinguoChronology.INSTANCE));
  }

  public static String toRocYearMonth(String opcYYYMM) {
    return toRocYearMonth(opcYYYMM, 0, null);
  }

  public static String calculateQuarter(String opcYearMonth) {
    YearMonth yearMonth = YearMonth.parse(opcYearMonth, DateTimeFormatter.ofPattern("yyyyMM"));
    int month = yearMonth.getMonthValue();
    int quarter = (month - 1) / 3 + 1;
    return String.valueOf(quarter);
  }
}