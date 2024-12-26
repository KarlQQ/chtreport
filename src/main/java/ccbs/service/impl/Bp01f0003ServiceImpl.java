package ccbs.service.impl;

import ccbs.conf.aop.RptLog;
import ccbs.conf.aop.RptLogAspect.Result;
import ccbs.conf.base.Bp01Config.Bp01f0003Config;
import ccbs.conf.base.Bp01Config.TypeItem;
import ccbs.data.repository.RptAccountRepository;
import ccbs.model.batch.dData;
import ccbs.model.bp01.Bp01f0003DTO;
import ccbs.model.online.OfficeInfoQueryIn;
import ccbs.model.online.OfficeInfoQueryOut;
import ccbs.service.intf.Bp01f0003Service;
import ccbs.util.DateUtils;
import ccbs.util.FileUtils;
import ccbs.util.comm01.Comm01Service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
public class Bp01f0003ServiceImpl implements Bp01f0003Service {
  private static final String SEPERATE =
      "==========================================================================================";

  @Value("${ccbs.csvFilePath}") private String csvFilePath;
  @Autowired private Bp01f0003Config config;
  @Autowired private RptAccountRepository rptAccountRepository;
  @Autowired private Comm01Service comm01Service;

  @Override
  @RptLog(rptCode = "BP220RPT")
  public Result process(String jobId, String opcDate, String opcYearMonth, String isRerun) {
    String rocYearMonth =
        DateUtils.toRocYearMonth(opcYearMonth, config.getYears().getShift(), ChronoUnit.MONTHS);
    List<Bp01f0003DTO> reusltList = rptAccountRepository.summaryBusinessODArrears(rocYearMonth);
    Map<String, String> billOffBelongMapping = getBillOffBelongMapping(reusltList);

    List<TypeItem> rptCustTypes = config.getRptCustTypes();
    List<String> report =
        config.getGroupBillItems()
            .parallelStream()
            .flatMap(groupBillItem -> {
              // handle single groupBillItem which is setting in config file
              List<TypeItem> billItemTypes = groupBillItem.getBillItemTypes();
              // csv file records
              List<String> recordList = new ArrayList<>();
              recordList.add(getHeader(billItemTypes, rptCustTypes).toString());
              // to caculate total amount/per groupBillItem
              List<List<Double>> totalSum = new ArrayList<>();
              filterRecordsAndGroupAmountByBillOffBelong(reusltList, groupBillItem.getPattern())
                  .entrySet()
                  .forEach(entry -> {
                    List<Double> sum = new ArrayList<>();
                    StringBuffer record = new StringBuffer(groupBillItem.getName())
                                              .append(",")
                                              .append(billOffBelongMapping.get(entry.getKey()));
                    rptCustTypes.forEach(rptCustType -> {
                      billItemTypes.forEach(billItemType -> {
                        Double amount = sumByAmountKey(
                            entry.getValue(), rptCustType.getType(), billItemType.getType());
                        record.append(",").append(String.format("%.0f", amount));
                        sum.add(amount);
                      });
                    });
                    totalSum.add(sum);
                    recordList.add(record.append(System.lineSeparator()).toString());
                  });

              recordList.add(
                  new StringBuffer(groupBillItem.getName())
                      .append(",合計,")
                      .append(totalSum.size() > 0
                              ? IntStream.range(0, totalSum.get(0).size())
                                    .mapToDouble(i
                                        -> totalSum.stream().mapToDouble(list -> list.get(i)).sum())
                                    .boxed()
                                    .map(amount -> String.format("%.0f", amount))
                                    .collect(Collectors.joining(","))
                              : "")
                      .append(System.lineSeparator())
                      .toString());
              recordList.add(new StringBuffer(SEPERATE).append(System.lineSeparator()).toString());
              return recordList.stream();
            })
            .collect(Collectors.toList());

    try {
      String filePath =
          Paths.get(csvFilePath, String.format(config.getFilename(), opcDate)).toString();
      File reportFile = FileUtils.generateFile(filePath, report);
      return Result.builder()
          .rptCode(config.getRptCode())
          .isRerun(isRerun)
          .opBatchno(jobId)
          .dDataList(List.of(dData.builder()
                                 .rptFileName(reportFile.getName())
                                 .billMonth(DateUtils.toRocYearMonth(opcYearMonth))
                                 .rptDate(opcDate)
                                 .rptFileCount(report.size())
                                 .build()))
          .reportFile(reportFile)
          .build();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private StringBuffer getHeader(List<TypeItem> billItemTypes, List<TypeItem> rptCustTypes) {
    StringBuffer h1 = new StringBuffer(",客戶別");
    StringBuffer h2 = new StringBuffer("統計項目,統計類別");
    StringBuffer h3 = new StringBuffer(SEPERATE);
    rptCustTypes.forEach(rptCustType -> {
      for (int i = 0; i < billItemTypes.size(); i++) {
        h1.append(",").append(i == 0 ? rptCustType.getName() : "");
        h2.append(",").append(billItemTypes.get(i).getName());
        h3.append(",");
      }
    });

    return h1.append(System.lineSeparator())
        .append(h2)
        .append(System.lineSeparator())
        .append(h3)
        .append(System.lineSeparator());
  }

  private Map<String, String> getBillOffBelongMapping(List<Bp01f0003DTO> reusltList) {
    return reusltList.stream()
        .map(Bp01f0003DTO::getBillOffBelong)
        .collect(Collectors.toSet())
        .stream()
        .collect(Collectors.toMap(Function.identity(), value -> {
          OfficeInfoQueryOut out = comm01Service.COMM01_0003(
              OfficeInfoQueryIn.builder().transType("A").officeCode(value).build());
          return out.getResultOfficeCN() != null ? out.getResultOfficeCN() : (String) value;
        }));
  }

  private Map<String, Map<String, Double>> filterRecordsAndGroupAmountByBillOffBelong(
      List<Bp01f0003DTO> resultList, String regx) {
    return resultList.stream()
        .filter(result -> result.getBillItemType().matches(regx))
        .collect(Collectors.groupingBy(Bp01f0003DTO::getBillOffBelong,
            Collectors.toMap(result
                -> getAmountKey(result.getRptCustType(), result.getBillItemType()),
                result
                -> result.getBillItemAmtSum().doubleValue(),
                (oldValue, newValue) -> oldValue)));
  }

  private Double sumByAmountKey(Map<String, Double> sums, String rptCustType, String billItemType) {
    String key = getAmountKey(rptCustType, billItemType);
    if (sums.containsKey(key)) {
      return sums.get(key).doubleValue();
    } else {
      return sums.entrySet()
          .stream()
          .filter(entry -> entry.getKey().matches(key))
          .mapToDouble(entry -> entry.getValue())
          .sum();
    }
  }

  private String getAmountKey(String rptCustType, String billItemType) {
    return new StringBuilder("amount_")
        .append(rptCustType)
        .append("_")
        .append(billItemType)
        .toString();
  }
}
