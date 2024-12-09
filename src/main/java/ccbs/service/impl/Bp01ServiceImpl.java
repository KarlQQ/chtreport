package ccbs.service.impl;

import ccbs.conf.aop.RptLogExecution;
import ccbs.conf.base.Bp01Config.Bp01f0007Config;
import ccbs.conf.base.Bp01Config.Bp01f0015Config;
import ccbs.data.entity.AccountAggregationDto;
import ccbs.data.entity.RptBillMain;
import ccbs.data.repository.BillDeviceRepo;
import ccbs.data.repository.BillRelsRepo;
import ccbs.data.repository.RptAccountRepository;
import ccbs.data.repository.RptBillMainRepo;
import ccbs.model.batch.PersonalInfoMaskStr;
import ccbs.model.batch.dData;
import ccbs.model.batch.dData.dDataBuilder;
import ccbs.model.bp01.Bp01f0007Form.FormData;
import ccbs.model.bp01.Bp01f0007Form.FormData.FormDataBuilder;
import ccbs.service.intf.Bp01Service;
import ccbs.template.Bp01f0007TemplaeTxt;
import ccbs.template.Bp01f0015TemplaeCsv;
import ccbs.util.DateUtils;
import ccbs.util.FileUtils;
import ccbs.util.comm01.Comm01ServiceImpl;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Bp01ServiceImpl implements Bp01Service {
  @Value("${ccbs.csvFilePath}") private String csvFilePath;
  @Autowired private RptAccountRepository rptAccountRepository;
  @Autowired private BillDeviceRepo billDeviceRepo;
  @Autowired private BillRelsRepo billRelsRepo;
  @Autowired private RptBillMainRepo rptBillMainRepo;
  @Autowired Bp01f0007Config config0007;
  @Autowired Bp01f0015Config config0015;
  @Autowired private Bp01f0007TemplaeTxt bp01f0007TemplaeTxt;
  @Autowired private Bp01f0015TemplaeCsv bp01f0015TemplaeCsv;

  @Override
  public Result process0003(String jobId, String opcDate, String opcYearMonth, String isRerun) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'process0003'");
  }

  @Override
  @RptLogExecution(rptCode = "BPGSM010")
  public Result process0007(
      String jobId, String opcDate, String opcYearMonth, String isRerun, String inputType) {
    List<Path> paths = new ArrayList<>();
    try {
      if ("1".equals(inputType)) {
        paths = FileUtils.moveFilesMatchingPattern(
            config0007.getFileSource(), null, config0007.getFilePattern1());
      }
      if ("2".equals(inputType)) {
        paths = FileUtils.moveFilesMatchingPattern(
            config0007.getFileSource(), null, config0007.getFilePattern2());
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    List<dData> dDataList = new ArrayList<>();
    for (Path path : paths) {
      List<String> report = new ArrayList<>();
      List<String> fileContent;
      try {
        fileContent = Files.readAllLines(path);
      } catch (IOException e) {
        log.error(String.format("read file error, path: %s", path), e);
        continue;
      }

      dDataBuilder dDataBuilder = dData.builder()
                                      .rptTimes("1")
                                      .billMonth(DateUtils.toRocYearMonth(opcYearMonth))
                                      .rptDate(opcDate)
                                      .rptQuarter(DateUtils.calculateQuarter(opcYearMonth));
      boolean rptSecretMark = false;
      for (String line : fileContent) {
        FormDataBuilder formDataBuilder = bp01f0007TemplaeTxt.fromFixedLengthString(line);
        FormData formData = formDataBuilder.build();
        if (formData.getTela() == null) {
          continue;
        }
        if (formData.getEmpIdOff() != null) {
          dDataBuilder.billOff(formData.getEmpIdOff());
        }
        List<RptBillMain> rptBillMainList =
            billRelsRepo.findBySubTela(formData.getTela())
                .stream()
                .flatMap(billRels
                    -> rptBillMainRepo
                           .findByBillOffAndBillTelAndBillMonthAndBillId(billRels.getBillOff(),
                               billRels.getBillTel(), billRels.getBillMonth(), billRels.getBillId())
                           .stream())
                .collect(Collectors.toList());
        rptBillMainList.addAll(billDeviceRepo.findByBillTela(formData.getTela())
                                   .stream()
                                   .flatMap(billDevice
                                       -> rptBillMainRepo
                                              .findByBillOffAndBillTel(
                                                  billDevice.getBillOff(), billDevice.getBillTel())
                                              .stream())
                                   .collect(Collectors.toList()));

        List<RptBillMain> filterRptBillMainList =
            rptBillMainList.stream()
                .filter(rptBillMain
                    -> formData.getIdno() == null
                        || formData.getIdno().equals(rptBillMain.getBillIdno()))
                .collect(Collectors.toList());

        if ("2".equals(inputType)) {
          if (filterRptBillMainList.size() > 0) {
            Map<String, String> billIdnoMask =
                filterRptBillMainList.parallelStream()
                    .collect(Collectors.toMap(rptBillMain
                        -> rptBillMain.getBillIdno(),
                        rptBillMain -> rptBillMain, (existing, duplicate) -> existing))
                    .values()
                    .parallelStream()
                    .filter(rptBillMain -> Comm01ServiceImpl.COMM01_0001(rptBillMain.getBillIdno()))
                    .map(rptBillMain
                        -> Map.entry(rptBillMain.getBillIdno(),
                            Comm01ServiceImpl
                                .COMM01_0002(PersonalInfoMaskStr.builder()
                                                 .maskIDNumber(rptBillMain.getBillIdno())
                                                 .build())
                                .getMaskIDNumber()))
                    .filter(entry -> entry.getValue() != null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            rptSecretMark = billIdnoMask.keySet().size() > 0;

            report.addAll(
                filterRptBillMainList.stream()
                    .map(rptBillMain
                        -> bp01f0007TemplaeTxt.toFixedLengthString(
                            formDataBuilder.result("Y")
                                .billOff(rptBillMain.getBillOff())
                                .billTel(rptBillMain.getBillTel())
                                .billIdMark(rptBillMain.getBillIdMark())
                                .billMonth(rptBillMain.getBillMonth())
                                .billAmt(rptBillMain.getBillAmt())
                                .idno(billIdnoMask.getOrDefault(
                                    rptBillMain.getBillIdno(), rptBillMain.getBillIdno()))
                                .payLimit(rptBillMain.getPayLimit())
                                .telStatus(rptBillMain.getTelStatus())
                                .drDate(rptBillMain.getDrDate())
                                .remark(rptBillMain.getRemark())
                                .greed(rptBillMain.getGreed())
                                .payType(rptBillMain.getPayType())
                                .billCycle(rptBillMain.getBillCycle())
                                .comboAccNoMark(
                                    Strings.isBlank(rptBillMain.getComboAccNo()) ? "N" : "Y")
                                .debtMark(rptBillMain.getDebtMark())
                                .billIdno(rptBillMain.getBillIdno())
                                .build()))
                    .collect(Collectors.toList()));
          } else {
            report.add(
                bp01f0007TemplaeTxt.toFixedLengthString(formDataBuilder.result("N").build()));
          }
        } else {
          report.add(bp01f0007TemplaeTxt.toFixedLengthString(
              formDataBuilder.result(filterRptBillMainList.size() > 0 ? "Y" : "N").build()));
        }
      }

      try {
        String filePath =
            Paths
                .get(csvFilePath,
                    String.format(config0007.getFilename(), "2".equals(inputType) ? "CCBS" : "MBMS",
                        opcDate, rptSecretMark ? "_MASK" : ""))
                .toString();
        File reportFile = FileUtils.generateFile(filePath, report);
        if ("2".equals(inputType)) {
          dDataList.add(dDataBuilder.rptFileName(reportFile.getName())
                            .rptFileCount(report.size())
                            .rptSecretMark(rptSecretMark ? "Y" : "N")
                            .build());
        }
      } catch (IOException e) {
        log.error("output file error", e);
      }
    }

    return Result.builder()
        .rptCode(config0007.getRptCode())
        .isRerun(isRerun)
        .opBatchno(jobId)
        .dDataList(dDataList)
        .build();
  }

  @Override
  public Result process0013(String jobId, String opcDate, String opcYearMonth, String isRerun) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'process0013'");
  }

  @Override
  @RptLogExecution(rptCode = "BP2230D6")
  public Result process0015(String jobId, String opcDate, String opcYearMonth, String isRerun) {
    String rocYearMonth = String.valueOf(Integer.valueOf(opcYearMonth) - 191100);
    Integer startROCYear =
        Integer.parseInt(rocYearMonth.substring(0, 3)) + config0015.getYears().getShift();
    Integer endROCYear = startROCYear - config0015.getYears().getLength() + 1;

    List<AccountAggregationDto> resultList =
        rptAccountRepository
            .analysisReceivableArrears(String.valueOf(endROCYear), String.valueOf(startROCYear))
            .stream()
            .map(AccountAggregationDto::new)
            .toList();

    Map<String, List<BigDecimal>> records =
        config0015.getReceiveItems().getReceiveTypes().stream().collect(
            Collectors.toMap(receiveType -> receiveType.getName(), receiveType -> {
              List<BigDecimal> record = new ArrayList<>(Collections.nCopies(13, BigDecimal.ZERO));
              List<AccountAggregationDto> filterResults =
                  resultList.stream()
                      .filter(result -> result.getAccItem().matches(receiveType.getType()))
                      .collect(Collectors.toList());
              if (filterResults.size() > 0) {
                BigDecimal sum = BigDecimal.ZERO;
                IntStream.range(0, 12).forEach(index -> {
                  String key = String.format("%d%02d", startROCYear, index + 1);
                  BigDecimal value = filterResults.stream()
                                         .map(filterResult -> filterResult.getData().get(key))
                                         .filter(Objects::nonNull)
                                         .reduce(BigDecimal.ZERO, BigDecimal::add);
                  record.set(index, value);
                  sum.add(value);
                });
                record.set(12, sum);
              }
              return record;
            }));

    List<String> report =
        new ArrayList<>(bp01f0015TemplaeCsv.getTitle(opcDate, String.valueOf(startROCYear)));
    report.addAll(
        IntStream.range(config0015.getYears().getShift(), config0015.getYears().getLength())
            .mapToObj(index -> {
              List<String> cotent = new ArrayList<String>();
              cotent.addAll(
                  bp01f0015TemplaeCsv.getHeader(String.valueOf(startROCYear - index + 1)));
              cotent.addAll(records.entrySet()
                                .stream()
                                .map(entry
                                    -> new StringBuffer(entry.getKey())
                                           .append(entry.getValue()
                                                       .stream()
                                                       .map(BigDecimal::toPlainString)
                                                       .collect(Collectors.joining(",")))
                                           .append(System.lineSeparator())
                                           .toString())
                                .collect(Collectors.toList()));
              cotent.addAll(bp01f0015TemplaeCsv.getFooter(records.values()));
              return cotent;
            })
            .flatMap(List::stream)
            .collect(Collectors.toList()));

    try {
      String filePath =
          Paths.get(csvFilePath, String.format(config0015.getFilename(), opcYearMonth)).toString();
      File reportFile = FileUtils.generateFile(filePath, report);
      return Result.builder()
          .rptCode(config0015.getRptCode())
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
}
