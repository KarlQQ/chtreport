package ccbs.controller;

import ccbs.dao.core.entity.RptList;
import ccbs.model.batch.BatchArrearsInputStr;
import ccbs.model.batch.BatchSimpleRptInStr;
import ccbs.model.batch.BatchSimpleRptInStrWithType;
import ccbs.model.batch.RptFileZipEncryptSingleIn;
import ccbs.model.batch.RptFileZipEncryptSingleOut;
import ccbs.model.batch.RptLogAfterExecuteInputStr;
import ccbs.model.batch.RptWatermarkSingleIn;
import ccbs.model.batch.RptWatermarkSingleOut;
import ccbs.model.online.BatchRptDownIn;
import ccbs.model.online.BatchRptQueryIn;
import ccbs.model.online.BatchRptQueryOut;
import ccbs.model.online.Bp01f0003Input;
import ccbs.model.online.Bp01f0007Input;
import ccbs.model.online.Bp01f0013Input;
import ccbs.model.online.GetRpoCodeOptIn;
import ccbs.model.online.OfficeInfoQueryIn;
import ccbs.model.online.OfficeInfoQueryOut;
import ccbs.model.online.RptCategoryOut;
import ccbs.model.online.UserData;
import ccbs.model.online.UserInfoQueryIn;
import ccbs.model.online.UserInfoQueryOut;
import ccbs.model.online.ValidationIn;
import ccbs.model.online.ValidationOut;
import ccbs.service.intf.ArrearsService;
import ccbs.service.intf.Bp01Service;
import ccbs.service.intf.Bp01f0003Service;
import ccbs.service.intf.Bp01f0013Service;
import ccbs.service.intf.CommOfficeService;
import ccbs.service.intf.RptLogService;
import ccbs.util.ValidationUtils;
import ccbs.util.comm01.Comm01Service;
import ccbs.util.comm02.Comm02Service;
import io.swagger.v3.oas.annotations.Operation;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

// import need to be remove, tmp reserve
@RestController
@RequestMapping("/rpt/api")
@CrossOrigin(origins = "*", exposedHeaders = HttpHeaders.CONTENT_DISPOSITION,
    methods = {RequestMethod.GET, RequestMethod.POST})
public class RptController {
  protected final Logger log = LoggerFactory.getLogger(this.getClass());

  @Autowired private Bp01f0003Service bp01f0003Service;
  @Autowired private Bp01f0013Service bp01f0013Service;
  @Autowired private Bp01Service bp01Service;
  @Autowired private ArrearsService arrearsService;
  @Autowired RptLogService rptLogService;
  @Autowired private Comm01Service comm01Service;
  @Autowired private Comm02Service comm02Service;
  @Autowired private CommOfficeService commOfficeService;

  @Value("${ccbs.pdfFilePath}") private String pdfFilePath;
  @Value("${ccbs.watermarkFilePath}") private String watermarkFilePath;
  @Value("${ccbs.csvFilePath}") private String csvFilePath;

  @Operation(summary = "批次設備號查欠報表", tags = {"Reports"}, description = "批次設備號查欠報表")
  @PostMapping(value = "/batchDeviceArrears", produces = "application/json;charset=UTF-8")
  public ResponseEntity<?> batchDeviceArrears(@RequestBody Bp01f0007Input input) {
    try {
      bp01Service
          .process0007(input.getJobId(), input.getOpcDate(), input.getOpcYearMonth(),
              input.getIsRerun(), input.getInputType())
          .getReportFile();

      return ResponseEntity.ok().body("批次設備號查欠報作業成功!");
    } catch (Exception e) {
      log.error("批次設備號查欠報表作業失敗!, 錯誤訊息: ", e);
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @Operation(summary = "應收會計欠費統計表(非呆帳)", tags = {"Reports"},
      description = "應收會計欠費統計表(非呆帳)")
  @PostMapping(value = "/downloadReceivableArrears", produces = "application/json;charset=UTF-8")
  public ResponseEntity<?>
  downloadReceivableArrears(@RequestBody BatchSimpleRptInStr input) {
    try {
      ResponseEntity<String> response = ValidationUtils.validateBatchSimpleRptInStr(input);
      if (response != null)
        return response;

      arrearsService.batchBP2230D6Rpt(input);
      return ResponseEntity.ok("應收會計欠費統計表(非呆帳)產生成功");
    } catch (Exception e) {
      log.error("應收會計欠費統計表(非呆帳)產生失敗", e);
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @Operation(summary = "檔上欠費分析表", tags = {"Reports"}, description = "檔上欠費分析表")
  @PostMapping("/downloadOutstandingArrears")
  public ResponseEntity<?> downloadOutstandingArrears(@RequestBody Bp01f0013Input input) {
    try {
      File reportFile = bp01f0013Service
                            .process(input.getJobId(), input.getOpcDate(), input.getOpcYearMonth(),
                                input.getIsRerun())
                            .getReportFile();

      byte[] byteArray = Files.readAllBytes(reportFile.toPath());
      ByteArrayResource resource = new ByteArrayResource(byteArray);
      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION,
          String.format("attachment; filename=\"%s\"", reportFile.getName()));
      headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
      headers.add(
          HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, MediaType.APPLICATION_OCTET_STREAM_VALUE);

      return ResponseEntity.ok()
          .headers(headers)
          .contentLength(resource.contentLength())
          .body(resource);

    } catch (Exception e) {
      log.error("檔上欠費分析表", e);
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @Operation(summary = "逾期１個月欠費統計表", tags = {"Reports"},
      description = "統計逾期１個月欠費統計表")
  @PostMapping("/downloadBusinessODArrearys")
  public ResponseEntity<?>
  downloadBusinessODArrearys(@RequestBody Bp01f0003Input input) {
    try {
      File reportFile = bp01f0003Service
                            .process(input.getJobId(), input.getOpcDate(), input.getOpcYearMonth(),
                                input.getIsRerun())
                            .getReportFile();

      byte[] byteArray = Files.readAllBytes(reportFile.toPath());
      ByteArrayResource resource = new ByteArrayResource(byteArray);
      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION,
          String.format("attachment; filename=\"%s\"", reportFile.getName()));
      headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

      return ResponseEntity.ok()
          .headers(headers)
          .contentLength(resource.contentLength())
          .body(resource);

    } catch (Exception e) {
      log.error("下載逾期欠費統計表失敗", e);
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  // 批次證號傳入參數
  // {
  // "jobid": "BPGUSUB",
  // "opcDate": "1130729",
  // "isRerun": "N"
  // }
  // {
  // "jobid": "BPGUSUB",
  // "opcDate": "20240729",
  // "isRerun": "N"
  // }

  // 測試U變更為E
  // {
  // "jobid": "BPGUSUB",
  // "opcDate": "20240729",
  // "isRerun": "Y"
  // }
  @Operation(summary = "批次證號查欠", tags = {"Reports"}, description = "批次呼叫單筆證號查欠")
  @PostMapping("/batchIdNoArrearsQuery")
  public String batchIdNoArrearsQuery(@RequestBody BatchArrearsInputStr input) {
    try {
      arrearsService.batchArrearsQuery(input);
      return "批次證號查欠作業成功!";
    } catch (Exception e) {
      log.error("", e);
      return "批次證號查欠作業失敗!, 錯誤訊息: " + e.getMessage();
    }
    // return null;
  }

  @Operation(summary = "批次報表查詢", tags = {"Reports"}, description = "批次報表查詢")
  @PostMapping("/batchRptQuery")
  public List<BatchRptQueryOut> batchRptQuery(@RequestBody BatchRptQueryIn input) {
    try {
      // bill off 一定有值
      if (!input.getBillOff().trim().isEmpty()) {
        // String paddedStr = String.format("%-4s", input.getBillOff().trim());
        input.setBillOff(input.getBillOff().trim());
      }

      // 報表產制頻率 rpt times 0 代表為不分，設Null
      if ("0".equals(input.getRptTimes())) {
        input.setRptTimes(null);
      }

      // 週期 rpt period 0 代表為不分，設Null
      if ("0".equals(input.getRptPeriod())) {
        input.setRptPeriod(null);
      }

      // 種類 rpt 0 代表為不分，設Null
      if ("0".equals(input.getRptCategory())) {
        input.setRptCategory(null);
      }

      // 種類 rpt 0 代表為不分，設Null
      if ("0".equals(input.getRptCode())) {
        input.setRptCode(null);
      }

      if (!input.getRptDateStart().trim().isEmpty()) {
        // String rocDateStart = getRocDate(input.getRptDateStart());
        // input.setRptDateStart(rocDateStart);
        input.setRptDateStart(input.getRptDateStart().replace("-", ""));
      }

      if (!input.getRptDateEnd().trim().isEmpty()) {
        // String rocDateEnd = getRocDate(input.getRptDateEnd());
        // input.setRptDateEnd(rocDateEnd);
        input.setRptDateEnd(input.getRptDateEnd().replace("-", ""));
      }

      // 因為year month 還不能正常傳入，先給null
      if (!input.getRptYearMonth().trim().isEmpty()) {
        String rocYearMonth = getRocYearMonth(input.getRptYearMonth());
        input.setRptYearMonth(rocYearMonth);
      }

      if (!input.getRptSecretMark().trim().isEmpty() && "B".equals(input.getRptSecretMark())) {
        input.setRptSecretMark(null);
      } else {
        input.setRptSecretMark("N");
      }

      if (input.getRptYearMonth().trim().isEmpty()) {
        input.setRptYearMonth(null);
      }

      return rptLogService.batchRptQuery(input);
    } catch (Exception e) {
      log.error("", e);
    }
    return null;
  }

  private String getRocDate(String rptDate) {
    String yyyymmdd = rptDate.replace("-", "");
    int rocInt = Integer.parseInt(yyyymmdd) - 19110000;
    String cyyymmdd = String.valueOf(rocInt);
    return cyyymmdd;
  }

  private String getRocYearMonth(String rptDate) {
    String yyyymm = rptDate.replace("-", "");
    int rocInt = Integer.parseInt(yyyymm) - 191100;
    String cyyymm = String.valueOf(rocInt);
    return cyyymm;
  }

  @Operation(summary = "批次報表下載", tags = {"Reports"}, description = "批次報表下載")
  @PostMapping("/batchRptDownload")
  public ResponseEntity<Resource> batchRptDownload(@RequestBody BatchRptDownIn input) {
    // get file path from rpt_list wher rpt_code='BPGUSUB'
    RptLogAfterExecuteInputStr rptLogAfterExecuteInputStr = new RptLogAfterExecuteInputStr();
    rptLogAfterExecuteInputStr.setRptCode("BPGUSUB");
    RptList rptList = rptLogService.getRptList(rptLogAfterExecuteInputStr);

    // get file name from RPT_LOGS_DETLS where rpt_logs_id='286'

    String fileName = input.getRptFileName();
    int dotIdx = input.getRptFileName().indexOf(".");
    String extensionFileName = input.getRptFileName().substring(dotIdx + 1);

    // C:/Users/mds/Desktop/personal/innoagile/CHT/repo/rptapi3/rpt-api/src/main/resources/ArrearsFiles/download/
    // /tmp/rpt/download/
    String downFilePath = rptList.getRptFilePath();

    boolean downloadOkFlg = false;
    if (extensionFileName.equals("pdf")) {
      // downFilePath = watermarkFilePath;

      return downloadPdfFile(downFilePath, fileName, input.getDownloadRptEmpId());
    } else if (extensionFileName.equals("csv") || extensionFileName.equals("TXT")) {
      try {
        // input.setDownloadRptEmpId(input.getRptFileName().substring(11, 17));
        String empIdFromWeb = input.getDownloadRptEmpId();
        // String zipFileNameWithPath = downloadCsvFileStreamResource(downFilePath,
        // fileName, input.getDownloadRptEmpId());

        int zipDotIdx = fileName.indexOf('.');
        String csvFileNameWithPath = csvFilePath + fileName;
        String zipFileName = fileName.substring(0, zipDotIdx) + ".zip";
        String zipFileNameWithPath = downFilePath + zipFileName;

        RptFileZipEncryptSingleIn rptFileZipEncryptSingleIn = new RptFileZipEncryptSingleIn();
        rptFileZipEncryptSingleIn.setRptFilePathAndName(csvFileNameWithPath);
        rptFileZipEncryptSingleIn.setZipRptFilePathAndName(zipFileNameWithPath);
        rptFileZipEncryptSingleIn.setEmpID(input.getDownloadRptEmpId());

        RptFileZipEncryptSingleOut rptFileZipEncryptSingleOut = new RptFileZipEncryptSingleOut();

        rptFileZipEncryptSingleOut =
            comm02Service.COMM02_0006_single_file_zip_encrypt(rptFileZipEncryptSingleIn);

        if (rptFileZipEncryptSingleOut.getCodeResult()) {
          // Path to the zip file
          File file = new File(zipFileNameWithPath);

          // Create InputStreamResource
          InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

          HttpHeaders headers = new HttpHeaders();
          headers.add(HttpHeaders.CONTENT_DISPOSITION,
              String.format("attachment; filename=\"%s\"", file.getName()));
          // Set the content type and attachment header
          return ResponseEntity.ok()
              .headers(headers)
              .contentLength(file.length())
              .contentType(MediaType.APPLICATION_OCTET_STREAM)
              .body(resource);
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return null;
  }

  private ResponseEntity<Resource> downloadPdfFile(
      String downFilePath, String fileName, String downloadRptEmpId) {
    RptWatermarkSingleIn rptWatermarkSingleIn = new RptWatermarkSingleIn();
    rptWatermarkSingleIn.setRptFileName(fileName);
    rptWatermarkSingleIn.setRptFilePath(pdfFilePath);
    rptWatermarkSingleIn.setWatermarkRptFilePath(watermarkFilePath);
    rptWatermarkSingleIn.setWatermarkRptFileName(fileName);
    rptWatermarkSingleIn.setEmpID(downloadRptEmpId);

    RptWatermarkSingleOut rptWatermarkSingleOut = new RptWatermarkSingleOut();
    rptWatermarkSingleOut = comm02Service.COMM02_0007_single_file_watermark(rptWatermarkSingleIn);

    boolean addPasswordFLg = false;
    if (rptWatermarkSingleOut.getCodeResult()) {
      RptWatermarkSingleIn rptWatermarkSingleIn2 = new RptWatermarkSingleIn();
      rptWatermarkSingleIn2.setRptFileName(fileName);
      rptWatermarkSingleIn2.setRptFilePath(downFilePath);
      rptWatermarkSingleIn2.setWatermarkRptFileName(fileName);
      rptWatermarkSingleIn2.setWatermarkRptFilePath(watermarkFilePath);
      rptWatermarkSingleIn2.setEmpID(downloadRptEmpId);

      addPasswordFLg = comm02Service.COMM02_0006_single_file_pdf_encrypt(rptWatermarkSingleIn2);
    }

    if (addPasswordFLg) {
      String absolutePathFileName = downFilePath + fileName;
      File file = new File(absolutePathFileName);

      if (!file.exists()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(null); // Or return a custom error message
      }

      FileSystemResource resource = new FileSystemResource(file);
      return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    }

    return null;
  }

  @Operation(summary = "取得使用者資訊", tags = {"Reports"}, description = "取得使用者資訊")
  @PostMapping("/getUserInfo")
  public UserInfoQueryOut getUserInfo(@RequestBody UserInfoQueryIn input) {
    UserInfoQueryOut userInfoQueryOut = comm01Service.COMM001_0005(input);

    return userInfoQueryOut;
  }

  @Operation(
      summary = "取得使用者資訊-測試", tags = {"Reports"}, description = "取得使用者資訊-測試")
  @PostMapping("/getUserInfoTest")
  public UserInfoQueryOut
  getUserInfoTest(@RequestBody UserInfoQueryIn input) {
    UserInfoQueryOut userInfoQueryOut = new UserInfoQueryOut();
    userInfoQueryOut.setPROC_RESULT("00");
    userInfoQueryOut.setMessage("成功");

    UserData userData = new UserData();
    userData.setLDAP_UID("tsai150105");
    // userData.setEMP_ID("885764");
    // empid: 892982
    String empid = input.getEmpId().substring(input.getEmpId().length() - 6);
    userData.setEMP_ID(empid);
    userData.setEMP_NAME("蔡**");
    userData.setEMP_DEP(
        new String[] {"資訊技術分公司", "客戶帳務整合系統處", "後帳維護科", "四股", ""});
    userData.setEMP_MAIL("tsai150105@cht.com.tw");
    userData.setEMP_TEL("02-23444678");
    userData.setEMP_FAX("");
    userData.setEMP_MOBILE("0963379357");
    userData.setEMP_STATUS("");
    userData.setEMP_OFFICE("22");
    userData.setEMP_AGENT("");
    userData.setEMP_DEPTDESP("資訊技術分公司/客戶帳務整合系統處/後帳維護科/四股");
    userData.setEMP_ADMIN("I");
    userData.setEMP_TITLE("34");
    userData.setEMP_TITLEDESP("工程師");
    userData.setLOGINIP("::1");
    userData.setRACF_UID(null);
    userData.setSERVER_ADMIN("N");
    userData.setSERVER_NAME("WINDOWS-6OVD8K1");
    userData.setOPLEVEL("2");
    userData.setUSER_STATUS(null);
    userData.setLAST_UP_DATER("林**");

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    DateTimeFormatter formatterIso = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    String userExpiretimeStr = "0001-01-01T00:00:00";
    // LocalDateTime userExpiretime = LocalDateTime.parse(userExpiretimeStr,
    // formatter);
    // Date userExpiretimeDate =
    // Date.from(userExpiretime.atZone(ZoneId.of("UTC")).toInstant());
    userData.setUSER_EXPIRETIME(userExpiretimeStr);

    String lastLoginTimeStr = "2024-08-06T09:25:12";
    // LocalDateTime lastLoginTime = LocalDateTime.parse(lastLoginTimeStr,
    // formatter);
    // Date lastLoginTimeDate =
    // Date.from(lastLoginTime.atZone(ZoneId.of("UTC")).toInstant());
    userData.setLAST_LOGIN_TIME(lastLoginTimeStr);

    String loginExpireTimeStr = "2024-08-06T10:42:15.3303528+08:00";
    // LocalDateTime loginExpireTime = LocalDateTime.parse(loginExpireTimeStr,
    // formatterIso);
    // Date loginExpireTimeDate =
    // Date.from(loginExpireTime.atZone(ZoneId.of("UTC")).toInstant());
    userData.setLOGIN_EXPIRE_TIME(loginExpireTimeStr);

    String lastUpdateTimeStr = "2024-01-05T09:40:11";
    // LocalDateTime lastUpdateTime = LocalDateTime.parse(lastUpdateTimeStr,
    // formatter);
    // Date lastUpdateTimeDate =
    // Date.from(lastUpdateTime.atZone(ZoneId.of("UTC")).toInstant());
    userData.setLAST_UPDATE_TIME(lastUpdateTimeStr);

    userData.setIS_SUPER_SM(false);
    userData.setIS_SM(false);
    userData.setSESSIONTIME(20);

    userInfoQueryOut.setUSER_DATA(userData);

    return userInfoQueryOut;
  }

  @Operation(summary = "驗證key", tags = {"Reports"}, description = "驗證key")
  @PostMapping("/keyValidation")
  public ValidationOut keyValidation(@RequestBody ValidationIn keyInput) {
    String encryptKey = comm01Service.authValEncrypt(keyInput.getKey());

    ValidationOut validationOut = new ValidationOut();

    validationOut.setResult("00");
    validationOut.setEncryptKey(encryptKey);
    return validationOut;
  }

  @Operation(summary = "取得機構代號選單", tags = {"Reports"}, description = "取得機構代號選單")
  @PostMapping("/getOffAdminOptions")
  public List<RptCategoryOut> getOffAdminOptions(@RequestBody UserInfoQueryIn input) {
    List<String> billOffIds = commOfficeService.getDistinctBillOffId();
    return billOffIds.stream()
        .map(billOffId -> {
          OfficeInfoQueryOut comm01_0003 = comm01Service.COMM01_0003(
              OfficeInfoQueryIn.builder().officeCode(billOffId).transType("A").build());
          return RptCategoryOut.builder()
              .name("0".equals(comm01_0003.getResultStatus())
                      ? billOffId.concat(" ").concat(comm01_0003.getResultOfficeCN().trim())
                      : billOffId)
              .code(billOffId)
              .build();
        })
        .collect(Collectors.toList());
  }

  @Operation(summary = "取得報表種類選單", tags = {"Reports"}, description = "取得報表種類選單")
  @PostMapping("/getRptCodeOptions")
  public List<RptCategoryOut> getRptCodeOptions(@RequestBody GetRpoCodeOptIn input) {
    if ("0".equals(input.getFunCode())) {
      input.setFunCode(null);
    }
    List<RptCategoryOut> rptOptionList = rptLogService.getRptCodeOptions(input.getFunCode());
    return rptOptionList;
  }

  @Operation(summary = "取得報表名稱選單", tags = {"Reports"}, description = "取得報表名稱選單")
  @PostMapping("/getFunCodeOptions")
  public List<RptCategoryOut> getFunCodeOptions(@RequestBody UserInfoQueryIn input) {
    List<RptCategoryOut> funCodeOptionList = rptLogService.getFunCodeOptions();
    return funCodeOptionList;
  }

  @Operation(summary = "取得機構中文名稱", tags = {"Reports"},
      description = "透過機構代號取得機構中文名稱")
  @PostMapping("/getOfficeName")
  public OfficeInfoQueryOut
  getOfficeName(@RequestBody OfficeInfoQueryIn input) {
    return comm01Service.COMM01_0003(input);
  }

  @Operation(summary = "產生營運處年度欠費統計報表", tags = {"Reports"},
      description = "產生指定日期的營運處年度欠費統計報表")
  @PostMapping(value = "/batchBP221D6Rpt", produces = "application/json;charset=UTF-8")
  public ResponseEntity<String>
  batchBP221D6Rpt(@RequestBody BatchSimpleRptInStr input) {
    try {
      ResponseEntity<String> response = ValidationUtils.validateBatchSimpleRptInStr(input);
      if (response != null)
        return response;

      arrearsService.batchBP221D6Rpt(input);
      return ResponseEntity.ok("營運處年度欠費統計報表產生成功");
    } catch (Exception e) {
      log.error("營運處年度欠費統計報表產生失敗", e);
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @Operation(summary = "產生MOD催收轉列營收統計表", tags = {"Reports"},
      description = "產生指定日期的MOD催收轉列營收統計表")
  @PostMapping(value = "/batchBP2240D1Rpt", produces = "application/json;charset=UTF-8")
  public ResponseEntity<String>
  batchBP2240D1Rpt(@RequestBody BatchSimpleRptInStr input) {
    try {
      ResponseEntity<String> response = ValidationUtils.validateBatchSimpleRptInStr(input);
      if (response != null)
        return response;

      arrearsService.batchBP2240D1Rpt(input);
      return ResponseEntity.ok("MOD催收轉列營收統計表產生成功");
    } catch (Exception e) {
      log.error("MOD催收轉列營收統計表產生失敗", e);
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @Operation(summary = "產生應收會科或營運處欠費統計表(含呆帳)", tags = {"Reports"},
      description = "產生指定日期的應收會科或營運處欠費統計表(含呆帳)")
  @PostMapping(value = "/batchBP2230D4D5Rpt", produces = "application/json;charset=UTF-8")
  public ResponseEntity<String>
  batchBP2230D4D5Rpt(@RequestBody BatchSimpleRptInStr input) {
    try {
      ResponseEntity<String> response = ValidationUtils.validateBatchSimpleRptInStr(input);
      if (response != null)
        return response;

      arrearsService.batchBP2230D4D5Rpt(input);
      return ResponseEntity.ok("應收會科或營運處欠費統計表(含呆帳)產生成功");
    } catch (Exception e) {
      log.error("應收會科或營運處欠費統計表(含呆帳)產生失敗", e);
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @Operation(summary = "產生會計科目檔上欠費統計表", tags = {"Reports"},
  description = "產生會計科目檔上欠費統計表")
  @PostMapping(value = "/batchBP222OTRpt", produces = "application/json;charset=UTF-8")
  public ResponseEntity<String>
  batchBP222OTRpt(@RequestBody BatchSimpleRptInStr input) {
    try {
      ResponseEntity<String> response = ValidationUtils.validateBatchSimpleRptInStr(input);
      if (response != null)
        return response;

      arrearsService.batchBP222OTRpt(input);
      arrearsService.batchBP222OT2Rpt(input);
      return ResponseEntity.ok("會計科目檔上欠費統計表產生成功");
    } catch (Exception e) {
      log.error("會計科目檔上欠費統計表產生失敗", e);
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }
  
  @Operation(summary = "產生全區逾期應收帳款未收回金額比例分析表", tags = {"Reports"},
  description = "產生全區逾期應收帳款未收回金額比例分析表")
  @PostMapping(value = "/batchBP22TOTRpt", produces = "application/json;charset=UTF-8")
  public ResponseEntity<String>
  batchBP22TOTRpt(@RequestBody BatchSimpleRptInStr input) {
    try {
      ResponseEntity<String> response = ValidationUtils.validateBatchSimpleRptInStr(input);
      if (response != null)
        return response;

      arrearsService.batchBP22TOTRpt(input);
      return ResponseEntity.ok("全區逾期應收帳款未收回金額比例分析表產生成功");
    } catch (Exception e) {
      log.error("全區逾期應收帳款未收回金額比例分析表產生失敗", e);
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @Operation(summary = "產生 SaaS ERP指定系列設備欠費清單", tags = {"Reports"},
  description = "產生 SaaS ERP指定系列設備欠費清單")
  @PostMapping(value = "/batchBPGNERPRpt", produces = "application/json;charset=UTF-8")
  public ResponseEntity<String>
  batchBPGNERPRpt(@RequestBody BatchSimpleRptInStrWithType input) {
    try {
      ResponseEntity<String> response = ValidationUtils.validateBatchSimpleRptInStr(input);
      if (response != null)
        return response;

      arrearsService.batchBPGNERPRpt(input);
      return ResponseEntity.ok("SaaS ERP指定系列設備欠費清單產生成功");
    } catch (Exception e) {
      log.error("SaaS ERP指定系列設備欠費清單產生失敗", e);
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }
}
