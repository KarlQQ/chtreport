package ccbs.util;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StatisticUtils {
  public static String calculateQuarter(String opcYearMonth) {
    YearMonth yearMonth = YearMonth.parse(opcYearMonth, DateTimeFormatter.ofPattern("yyyyMM"));
    int month = yearMonth.getMonthValue();
    int quarter = (month - 1) / 3 + 1;
    return String.valueOf(quarter);
  }

  public static List<String> performStatistics(Collection<List<BigDecimal>> records) {
    return performStatistics(records, BigDecimal::toPlainString);
  }

  public static List<String> performStatistics(
      Collection<List<BigDecimal>> records, Function<? super BigDecimal, ? extends String> mapper) {
    int maxSize = records.stream().mapToInt(List::size).max().orElse(0);

    return IntStream.range(0, maxSize)
        .mapToObj(i
            -> records.stream()
                   .filter(list -> list.size() > i)
                   .map(list -> list.get(i))
                   .reduce(BigDecimal.ZERO, BigDecimal::add))
        .map(mapper)
        .collect(Collectors.toList());
  }

  public static List<String> calculateStatistics(Collection<List<Double>> records) {
    return calculateStatistics(records, val -> String.format("%.0f", val));
  }

  public static List<String> calculateStatistics(
      Collection<List<Double>> records, Function<? super Double, ? extends String> mapper) {
    int maxSize = records.stream().mapToInt(List::size).max().orElse(0);
    return IntStream.range(0, maxSize)
        .mapToDouble(i
            -> records.stream()
                   .filter(list -> list.size() > i)
                   .mapToDouble(list -> list.get(i))
                   .sum())
        .boxed()
        .map(mapper)
        .collect(Collectors.toList());
  }
}
