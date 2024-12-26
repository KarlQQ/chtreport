package ccbs.service.impl;

import ccbs.conf.aop.RptLog;
import ccbs.conf.aop.RptLogAspect.Result;
import ccbs.conf.base.Bp01Config.Rqbp019Config;
import ccbs.conf.base.Bp01Config.Rqbp019Config.TypeConfig;
import ccbs.data.entity.RptBillMain;
import ccbs.data.entity.Rqbp019Type3Dto;
import ccbs.data.repository.BillRelsRepo;
import ccbs.data.repository.RptBillMainRepo;
import ccbs.data.repository.RptTelTypeDetlRepo;
import ccbs.model.batch.PersonalInfoMaskStr;
import ccbs.model.batch.dData;
import ccbs.model.online.Rqbp019Input;
import ccbs.service.intf.Rqbp019Service;
import ccbs.template.Rqbp019TemplaeTxt;
import ccbs.template.Rqbp019TemplaeTxt.FormData;
import ccbs.util.DateUtils;
import ccbs.util.FileUtils;
import ccbs.util.comm01.Comm01ServiceImpl;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class Rqbp019ServiceImpl implements Rqbp019Service {
  @Autowired private Rqbp019Config config;
  @Autowired private Rqbp019TemplaeTxt template;
  @Autowired private BillRelsRepo billRelsRepo;
  @Autowired private RptBillMainRepo rptBillMainRepo;
  @Autowired private RptTelTypeDetlRepo rptTelTypeDetlRepo;

  @Override
  @RptLog(rptCode = RPT_CODE)
  public Result process(Rqbp019Input input) throws Exception {
    Response res = getInputType(input.getInputType())
                       .orElseThrow(()
                                        -> new RuntimeException(String.format(
                                            "未知的inputType: %s", input.getInputType())))
                       .handle(input);

    return Result.builder()
        .rptCode(RPT_CODE)
        .isRerun(input.getIsRerun())
        .opBatchno(input.getJobId())
        .errorCount(res.getErrorCount())
        .dDataList(res.getDDataList())
        .build();
  }

  private Optional<InputType> getInputType(String type) {
    switch (type) {
      case "1":
        return Optional.of(new MBMS(config, template, rptBillMainRepo, billRelsRepo));
      case "2":
        return Optional.of(new CCBS(config, template, rptBillMainRepo, billRelsRepo));
      case "3":
        return Optional.of(new BPGNERP(config, template, rptBillMainRepo, rptTelTypeDetlRepo));
      default:
        return Optional.empty();
    }
  }

  @AllArgsConstructor
  public static abstract class InputType {
    protected TypeConfig config;

    public TypeConfig getConfig() {
      return config;
    }

    public abstract Response handle(Rqbp019Input input) throws Exception;

    protected List<RptBillMain> queryRptBillMain(
        FormData formData, RptBillMainRepo rptBillMainRepo, BillRelsRepo billRelsRepo) {
      List<RptBillMain> rptBillMains = StringUtils.hasText(formData.getIdno())
          ? rptBillMainRepo.findByBillIdno(formData.getIdno())
          : rptBillMainRepo.findByBillTela(formData.getTela());

      if (rptBillMains.size() == 0) {
        rptBillMains =
            billRelsRepo.findBySubTela(formData.getTela())
                .stream()
                .flatMap(
                    billRels -> rptBillMainRepo.findByBillTela(billRels.getBillTela()).stream())
                .collect(Collectors.toList());
      }
      return rptBillMains;
    }

    protected Map<String, String> generateIdnoMask(List<String> billIdnos) {
      return billIdnos.parallelStream()
          .filter(Objects::nonNull)
          .distinct()
          .filter(Comm01ServiceImpl::COMM01_0001)
          .map(billIdno
              -> Map.entry(billIdno,
                  Comm01ServiceImpl
                      .COMM01_0002(PersonalInfoMaskStr.builder().maskIDNumber(billIdno).build())
                      .getMaskIDNumber()))
          .filter(entry -> Objects.nonNull(entry.getValue()))
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
  }

  @Data
  @Builder
  public static class Response {
    @Builder.Default private Integer errorCount = 0;
    @Builder.Default private List<dData> dDataList = Collections.emptyList();
  }

  public static class MBMS extends InputType {
    private String inputPath;
    private Rqbp019TemplaeTxt template;
    private RptBillMainRepo rptBillMainRepo;
    private BillRelsRepo billRelsRepo;

    public MBMS(Rqbp019Config rqbp019Config, Rqbp019TemplaeTxt template,
        RptBillMainRepo rptBillMainRepo, BillRelsRepo billRelsRepo) {
      super(rqbp019Config.getTypeConfigs()
                .stream()
                .filter(typeConfig -> "MBMS".equals(typeConfig.getName()))
                .findAny()
                .orElseThrow());
      this.inputPath = rqbp019Config.getInput();
      this.template = template;
      this.rptBillMainRepo = rptBillMainRepo;
      this.billRelsRepo = billRelsRepo;
    }

    @Override
    public Response handle(Rqbp019Input input) throws IOException {
      List<Path> paths = FileUtils.moveFilesMatchingPattern(inputPath, null, config.getPattern());

      AtomicInteger errorCount = new AtomicInteger(0);
      List<dData> dDataList = Collections.synchronizedList(new ArrayList<>());
      paths.parallelStream().forEach(path -> {
        try {
          handle(path, input).ifPresent(dDataList::add);
        } catch (Exception e) {
          log.error("批次處理失敗, path: {}, error: {}", path, e);
          errorCount.incrementAndGet();
        }
      });

      return Response.builder().errorCount(errorCount.get()).dDataList(dDataList).build();
    }

    public Optional<dData> handle(Path path, Rqbp019Input input)
        throws UnsupportedEncodingException, FileNotFoundException, IOException {
      List<String> fileContent = Files.readAllLines(path);
      AtomicBoolean rptSecretMark = new AtomicBoolean();
      AtomicReference<String> empIdOff = new AtomicReference<>();
      List<String> report =
          fileContent.parallelStream()
              .map(line -> {
                FormData formData = template.fromFixedLengthString(line).build();
                if (Objects.nonNull(empIdOff.get()) && Objects.nonNull(formData.getEmpIdOff())) {
                  empIdOff.set(formData.getEmpIdOff());
                }

                List<RptBillMain> rptBillMains =
                    queryRptBillMain(formData, rptBillMainRepo, billRelsRepo);
                if (rptBillMains.size() > 0) {
                  RptBillMain rptBillMain = rptBillMains.get(0);
                  Map<String, String> idnoMask =
                      generateIdnoMask(List.of(rptBillMain.getBillIdno()));
                  rptSecretMark.set(rptSecretMark.get() || idnoMask.size() > 0);
                  rptBillMain.setBillIdno(
                      idnoMask.getOrDefault(rptBillMain.getBillIdno(), rptBillMain.getBillIdno()));
                  return template.toFixedLengthString(rptBillMain);
                } else {
                  formData.setResult("N");
                  return template.toFixedLengthString(formData);
                }
              })
              .collect(Collectors.toList());

      Matcher matcher = Pattern.compile(config.getPattern()).matcher(path.toFile().getName());
      FileUtils.generateFile(
          Paths
              .get(config.getOutput(),
                  String.format(config.getFilename(), matcher.find() ? matcher.group(1) : "",
                      input.getOpcDate(), rptSecretMark.get() ? "_MASK" : ""))
              .toString()
              .toString(),
          report);

      return Optional.empty();
    }
  }

  public static class CCBS extends InputType {
    private String inputPath;
    private Rqbp019TemplaeTxt template;
    private RptBillMainRepo rptBillMainRepo;
    private BillRelsRepo billRelsRepo;

    public CCBS(Rqbp019Config rqbp019Config, Rqbp019TemplaeTxt template,
        RptBillMainRepo rptBillMainRepo, BillRelsRepo billRelsRepo) {
      super(rqbp019Config.getTypeConfigs()
                .stream()
                .filter(typeConfig -> "CCBS".equals(typeConfig.getName()))
                .findAny()
                .orElseThrow());
      this.inputPath = rqbp019Config.getInput();
      this.template = template;
      this.rptBillMainRepo = rptBillMainRepo;
      this.billRelsRepo = billRelsRepo;
    }

    @Override
    public Response handle(Rqbp019Input input) throws IOException {
      List<Path> paths = FileUtils.moveFilesMatchingPattern(inputPath, null, config.getPattern());

      AtomicInteger errorCount = new AtomicInteger(0);
      List<dData> dDataList = Collections.synchronizedList(new ArrayList<>());
      paths.parallelStream().forEach(path -> {
        try {
          handle(path, input).ifPresent(dDataList::add);
        } catch (Exception e) {
          log.error(String.format("批次處理失敗, 檔名: %s", path), e);
          errorCount.incrementAndGet();
        }
      });

      return Response.builder().errorCount(errorCount.get()).dDataList(dDataList).build();
    }

    public Optional<dData> handle(Path path, Rqbp019Input input)
        throws UnsupportedEncodingException, FileNotFoundException, IOException {
      List<String> fileContent = Files.readAllLines(path);
      AtomicBoolean rptSecretMark = new AtomicBoolean();
      AtomicReference<String> empIdOff = new AtomicReference<>();
      List<String> report =
          fileContent.parallelStream()
              .flatMap(line -> {
                FormData formData = template.fromFixedLengthString(line).build();
                if (Objects.nonNull(empIdOff.get()) && Objects.nonNull(formData.getEmpIdOff())) {
                  empIdOff.set(formData.getEmpIdOff());
                }

                List<RptBillMain> rptBillMains =
                    queryRptBillMain(formData, rptBillMainRepo, billRelsRepo);
                if (rptBillMains.size() > 0) {
                  Map<String, String> idnoMask = generateIdnoMask(
                      rptBillMains.stream().map(RptBillMain::getBillIdno).toList());
                  rptSecretMark.set(rptSecretMark.get() || idnoMask.size() > 0);

                  return rptBillMains.parallelStream().map(rptBillMain -> {
                    rptBillMain.setBillIdno(idnoMask.getOrDefault(
                        rptBillMain.getBillIdno(), rptBillMain.getBillIdno()));
                    return template.toFixedLengthString(rptBillMain);
                  });
                } else {
                  formData.setResult("N");
                  return List.<String>of(template.toFixedLengthString(formData)).stream();
                }
              })
              .collect(Collectors.toList());

      Matcher matcher = Pattern.compile(config.getPattern()).matcher(path.toFile().getName());
      File reportFile = FileUtils.generateFile(
          Paths
              .get(config.getOutput(),
                  String.format(config.getFilename(), matcher.find() ? matcher.group(1) : "",
                      input.getOpcDate(), rptSecretMark.get() ? "_MASK" : ""))
              .toString()
              .toString(),
          report);

      return Optional.of(dData.builder()
                             .rptTimes("1")
                             .billMonth(DateUtils.toRocYearMonth(input.getOpcYearMonth()))
                             .rptDate(input.getOpcDate())
                             .rptQuarter(DateUtils.calculateQuarter(input.getOpcYearMonth()))
                             .billOff(empIdOff.get())
                             .rptFileName(reportFile.getName())
                             .rptFileCount(report.size())
                             .rptSecretMark(rptSecretMark.get() ? "Y" : "N")
                             .build());
    }
  }

  public static class BPGNERP extends InputType {
    private Rqbp019TemplaeTxt template;
    private RptBillMainRepo rptBillMainRepo;
    private RptTelTypeDetlRepo rptTelTypeDetlRepo;

    public BPGNERP(Rqbp019Config rqbp019Config, Rqbp019TemplaeTxt template,
        RptBillMainRepo rptBillMainRepo, RptTelTypeDetlRepo rptTelTypeDetlRepo) {
      super(rqbp019Config.getTypeConfigs()
                .stream()
                .filter(typeConfig -> "BPGNERP".equals(typeConfig.getName()))
                .findAny()
                .orElseThrow());
      this.template = template;
      this.rptBillMainRepo = rptBillMainRepo;
      this.rptTelTypeDetlRepo = rptTelTypeDetlRepo;
    }

    @Override
    public Response handle(Rqbp019Input input) {
      List<dData> dDataList = new ArrayList<>();
      Integer errorCount = 0;

      String inputSCAN = input.getInputSCAN();
      if (inputSCAN.length() > 1) {
        String billOpid = input.getInputSCAN().substring(0, 1);
        List<Rqbp019Type3Dto> result;
        if ("0".equals(input.getInputSCAN().substring(1, 2))) {
          result = rptBillMainRepo.findbyBillOpidWithCname(billOpid);
        } else {
          result = rptTelTypeDetlRepo.getByBillOpidRule(input.getInputSCAN())
                       .map(rptTelTypeDetl
                           -> rptBillMainRepo.findbyBillOpidAndRegexpLikeBillTelaWithCname(
                               billOpid, rptTelTypeDetl.getPattern()))
                       .orElse(Collections.emptyList());
        }

        Map<String, String> idnoMask;
        List<String> report;
        if (result.size() > 0) {
          idnoMask = generateIdnoMask(result.stream().map(Rqbp019Type3Dto::getBillIdno).toList());
          report = result.parallelStream()
                       .map(rqbp019Type3DTO -> {
                         rqbp019Type3DTO.setBpgnerpIdno(idnoMask.getOrDefault(
                             rqbp019Type3DTO.getBpgnerpIdno(), rqbp019Type3DTO.getBpgnerpIdno()));
                         return template.toFixedLengthString(rqbp019Type3DTO);
                       })
                       .collect(Collectors.toList());
        } else {
          idnoMask = Collections.emptyMap();
          report = Collections.emptyList();
        }

        try {
          File reportFile =
              FileUtils.generateFile(Paths
                                         .get(config.getOutput(),
                                             String.format(config.getFilename(), input.getOpcDate(),
                                                 idnoMask.keySet().size() > 0 ? "_MASK" : ""))
                                         .toString()
                                         .toString(),
                  report);

          dDataList.add(dData.builder()
                            .rptTimes("1")
                            .billMonth(DateUtils.toRocYearMonth(input.getOpcYearMonth()))
                            .rptDate(input.getOpcDate())
                            .rptQuarter(DateUtils.calculateQuarter(input.getOpcYearMonth()))
                            .rptFileName(reportFile.getName())
                            .rptFileCount(report.size())
                            .rptSecretMark(idnoMask.keySet().size() > 0 ? "Y" : "N")
                            .build());
        } catch (IOException e) {
          e.printStackTrace();
          errorCount++;
        }
      }

      return Response.builder().errorCount(errorCount).dDataList(dDataList).build();
    }
  }
}
