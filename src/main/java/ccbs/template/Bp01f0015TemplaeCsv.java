package ccbs.template;

import ccbs.conf.base.Bp01Config.Bp01f0015Config;
import ccbs.util.StatisticUtils;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Bp01f0015TemplaeCsv {
  @Autowired private Bp01f0015Config config;

  public List<String> getTitle(String opcDate, String rocYearMonth) {
    return List.of(new StringBuffer(
        String.format("%s 年 %s 月  北分欠費依會計科目【非呆帳】統計表     製表日期 %s",
            rocYearMonth.substring(0, 3), rocYearMonth.substring(3), opcDate))
                       .append(System.lineSeparator())
                       .append(System.lineSeparator())
                       .toString());
  }

  public List<String> getHeader(String rocYear) {
    String[] headers = config.getReceiveItems().getHeaders().split(",");
    StringBuffer h1 = new StringBuffer(String.format("%s 年度", rocYear));
    IntStream.range(0, headers.length - 1).forEach(index -> h1.append(","));
    StringBuffer h2 = new StringBuffer(String.join(",", headers));
    IntStream.range(1, 13).forEach(index -> {
      h1.append(",").append(String.format("%02d 月", index));
      h2.append(",合計欠費／非呆帳");
    });
    return List.of(h1.append(",總計").append(System.lineSeparator()).toString(),
        h2.append(",總計欠費／非呆帳").append(System.lineSeparator()).toString());
  }

  public List<String> getFooter(Collection<List<BigDecimal>> records) {
    Integer length = config.getReceiveItems().getHeaders().split(",").length;

    StringBuffer f1 = new StringBuffer(
        IntStream.range(0, length).mapToObj(index -> "").collect(Collectors.joining(",")))
                          .append(",總計,")
                          .append(String.join(",", StatisticUtils.performStatistics(records)));
    return List.of(f1.append(System.lineSeparator()).toString());
  }
}
