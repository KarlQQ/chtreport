package ccbs.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
}