package ccbs.service.impl;

import ccbs.conf.aop.RptLogExecution;
import ccbs.conf.base.Bp01Config.Bp01f0013Config;
import ccbs.data.repository.RptAccountRepository;
import ccbs.model.bp01.Bp01f0013DTO;
import ccbs.service.intf.Bp01Service.Result;
import ccbs.service.intf.Bp01f0013Service;
import ccbs.util.FileUtils;
import ccbs.util.StatisticUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Bp01f0013ServiceImpl implements Bp01f0013Service {
  @Value("${ccbs.csvFilePath}") private String csvFilePath;
  @Autowired private Bp01f0013Config config;
  @Autowired private RptAccountRepository rptAccountRepository;

  @Override
  @RptLogExecution(rptCode = "BP2230D2")
  public Result process(String jobId, String opcDate, String opcYearMonth, String isRerun) {
    String rocYearMonth = String.valueOf(Integer.valueOf(opcYearMonth) - 191100);
    Integer startROCYear =
        Integer.parseInt(rocYearMonth.substring(0, 3)) + config.getYears().getStart();
    Integer endROCYear = startROCYear - config.getYears().getTitles().size() + 1;

    List<String> report = new ArrayList<>();
    report.addAll(getHeader(opcDate, opcYearMonth, startROCYear, config.getYears().getTitles()));

    Map<String, Integer> fieldMapping =
        IntStream.range(0, config.getYears().getTitles().size())
            .boxed()
            .collect(Collectors.toMap(
                index -> String.valueOf(startROCYear - index), Function.identity()));
    List<Bp01f0013DTO> resultList = rptAccountRepository.analysisOutstandingArrears(
        String.valueOf(endROCYear), String.valueOf(startROCYear));
    Map<String, List<Double>> records = aggregateData(fieldMapping, resultList);
    report.addAll(records.entrySet()
                      .stream()
                      .map(entry -> {
                        String key = entry.getKey();
                        return resultList.stream()
                            .filter(result -> key.equals(result.getAccItem()))
                            .findAny()
                            .map(result
                                -> new StringBuffer(key)
                                       .append(",")
                                       .append(result.getAccName())
                                       .append(",")
                                       .append(entry.getValue()
                                                   .stream()
                                                   .map(val -> String.format("%.0f", val))
                                                   .collect(Collectors.joining(", ")))
                                       .append(System.lineSeparator())
                                       .toString())
                            .orElse(null);
                      })
                      .collect(Collectors.toList()));
    report.addAll(getFooter(records));

    try {
      String filePath =
          Paths.get(csvFilePath, String.format(config.getFilename(), opcDate)).toString();
      File reportFile = FileUtils.generateFile(filePath, report);
      return Result.builder()
          .rptCode(config.getRptCode())
          .isRerun(isRerun)
          .opBatchno(jobId)
          .rptFileName(reportFile.getName())
          .billMonth(rocYearMonth)
          .rptDate(opcDate)
          .rptFileCount(report.size())
          .reportFile(reportFile)
          .build();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Map<String, List<Double>> aggregateData(
      Map<String, Integer> fieldMapping, List<Bp01f0013DTO> resultList) {
    Map<String, List<Double>> aggregatedData = new HashMap<>();
    resultList.forEach(result -> {
      String code = result.getAccItem();
      aggregatedData.computeIfAbsent(
          code, k -> new ArrayList<>(Collections.nCopies(fieldMapping.entrySet().size(), 0.0)));
      if (fieldMapping.containsKey(result.getBillYear())) {
        aggregatedData.get(code).set(
            fieldMapping.get(result.getBillYear()), result.getBillItemAmtSum().doubleValue());
      }
    });
    return aggregatedData;

    // return resultList.stream().collect(Collectors.toMap(result
    //     -> result.getAccItem(),
    //     result -> List.of(result.getBillItemAmtSum().doubleValue()), (oldVal, newVal) -> {
    //       oldVal.addAll(newVal);
    //       return oldVal;
    //     }));
  }

  private List<String> getHeader(
      String opcDate, String rocYearMonth, Integer startROCYear, List<String> titles) {
    String h1 = String.format("%s 年 %s 月  檔上欠費分析表     製表日期 %s",
                    rocYearMonth.substring(0, 3), rocYearMonth.substring(3), opcDate)
        + System.lineSeparator();
    StringBuffer h2 = new StringBuffer("BP22,年度");
    StringBuffer h3 = new StringBuffer("科目代號,會計科目");
    IntStream.range(0, titles.size()).forEach(index -> {
      h2.append(",").append(startROCYear - index).append(" 年　度");
      h3.append(",").append(titles.get(index));
    });
    return List.of(h1, h2.append(System.lineSeparator()).toString(),
        h3.append(System.lineSeparator()).toString());
  }

  private List<String> getFooter(Map<String, List<Double>> records) {
    StringBuffer f1 = new StringBuffer(",總計,").append(
        String.join(",", StatisticUtils.calculateStatistics(records.values())));
    return List.of(f1.toString());
  }
}