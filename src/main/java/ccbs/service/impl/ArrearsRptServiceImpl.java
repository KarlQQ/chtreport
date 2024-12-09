package ccbs.service.impl;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.select;

import ccbs.conf.aop.RptLogExecution;
import ccbs.conf.base.Bp01Config.Bp01f0013Config;
import ccbs.dao.core.constants.DateTypeConstants;
import ccbs.dao.core.entity.BillRels;
import ccbs.dao.core.entity.RptAccountSummary;
import ccbs.dao.core.entity.RptBP2230D4Summary;
import ccbs.dao.core.entity.RptBP2230D5Summary;
import ccbs.dao.core.entity.RptBP2230D6Summary;
import ccbs.dao.core.entity.RptBP2240D1Summary;
import ccbs.dao.core.entity.RptBillMain;
import ccbs.dao.core.mapper.BillRelsMapper;
import ccbs.dao.core.mapper.RptAccountSummaryMapper;
import ccbs.dao.core.mapper.RptBillMainMapper;
import ccbs.dao.core.sql.BillRelsDynamicSqlSupport;
import ccbs.dao.core.sql.RptBillMainDynamicSqlSupport;
import ccbs.model.batch.ArrearsFileLineInput;
import ccbs.model.batch.BP221D6_ReportRowForm;
import ccbs.model.batch.BPGUSUB_ReportRowForm;
import ccbs.model.batch.BatchArrearsInputStr;
import ccbs.model.batch.BatchSimpleRptInStr;
import ccbs.model.batch.IdnoData;
import ccbs.model.batch.PersonalInfoMaskStr;
import ccbs.model.batch.RptLogAfterExecuteInputStr;
import ccbs.model.batch.RptLogAfterExecuteOutputStr;
import ccbs.model.batch.RptLogBeforeExecuteInputStr;
import ccbs.model.batch.RptLogBeforeExecuteOutputStr;
import ccbs.model.batch.SingleArrearsInputStr;
import ccbs.model.batch.SingleArrearsOutputStr;
import ccbs.model.batch.SubData;
import ccbs.model.batch.dData;
import ccbs.model.online.OfficeInfoQueryIn;
import ccbs.model.online.OfficeInfoQueryOut;
import ccbs.service.intf.ArrearsService;
import ccbs.service.intf.Bp01Service.Result;
import ccbs.util.CsvGenerator;
import ccbs.util.DateUtils;
import ccbs.util.NameMapping;
import ccbs.util.StringUtils;
import ccbs.util.comm01.Comm01Service;
import ccbs.util.comm01.Comm01ServiceImpl;
import ccbs.util.comm02.Comm02Service;
import ccbs.util.comm03.Comm03Service;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ArrearsRptServiceImpl implements ArrearsService {
  @Autowired private RptAccountSummaryMapper rptAccountSummaryMapper;
  @Autowired private RptBillMainMapper rptBillMainMapper;
  @Autowired private BillRelsMapper billRelsMapper;
  RptBillMainDynamicSqlSupport.RptBillMain rptBillMain =
      RptBillMainDynamicSqlSupport.rptBillMain.withAlias("rptBillMain");
  BillRelsDynamicSqlSupport.BillRels billRels =
      BillRelsDynamicSqlSupport.billRels.withAlias("billRels");
  @Value("${ccbs.inputFilePath}") private String inputFilePath;
  @Value("${ccbs.pdfFilePath}") private String pdfFilePath;
  @Value("${ccbs.csvFilePath}") private String csvFilePath;
  // @Value("${ccbs.fontPath}")
  // private String fontPath;
  @Value("${ccbs.watermarkFilePath}") private String watermarkFilePath;

  @Value("${ccbs.zipFilePath}") private String zipFilePath;

  @Autowired private Comm03Service comm03Service;
  @Autowired private Comm02Service comm02Service;
  @Autowired private Comm01Service comm01Service;
  @Autowired private Bp01f0013Config config;

  private int inputFilesCnt = 0;
  private int genRptOkFilesCnt = 0;
  private int genRptFailFilesCnt = 0;

  // 字體路徑(正式)

  // private final static String fontPath =
  // "C:/Users/mds/Desktop/personal/innoagile/CHT/repo/rptapi/src/main/resources/ArrearsFiles/font/font.ttf";
  private final static String fontPath = "/usr/share/fonts/kai/font.ttf";

  @Override
  public SingleArrearsOutputStr singleArrearsQuery(SingleArrearsInputStr input) {
    // 1.
    // 依傳入｛BILL_IDNO｝查找資料表【RPT_BILL_MAIN】欄位【BILL_IDNO】=｛BILL_IDNO｝,查無資料則｛PROC_RESULT｝=「99」回傳;否則｛PROC_RESULT｝=「00」繼續執行
    // 2. 若傳入｛GET_SUB_MARK｝=「Y」則逐筆依取得【欠費資料】查找【BILL_RELS】:
    // 欄位【BILL_OFF】、【BILL_TEL】、【BILL_MONTH】、【BILL_ID】【BILL_AMT】相同，或【BILL_OFF】、【BILL_TEL】、【BILL_MONTH】【BILL_AMT】相同，則視為取得【子號資料】
    // 3. 若傳入｛查子號識別｝=「Y」且取得【子號資料】則｛WITH_SUB_MARK｝給
    // 「Y」回傳【含子號集欠費資料】 4. 否則｛WITH_SUB_MARK｝給
    // 「N」且回傳【不含子號集欠費資料】

    log.debug("singleArrearsQuery start, "
        + " time: " + getCurrentDateTime());
    String inputBillIdno = input.getBillIdno();
    String inputSubMark = input.getGetSubMark();
    Boolean dataSetFlg = false;
    SingleArrearsOutputStr singleArrearsOutputStr = new SingleArrearsOutputStr();

    try {
      // query data from RPT_BILL_MAIN
      log.debug("query data from RPT_BILL_MAIN start, "
          + " time: " + getCurrentDateTime());
      SelectStatementProvider rptBillMainProvider =
          select(rptBillMain.allColumns())
              .from(rptBillMain)
              .where(rptBillMain.billIdno, isEqualTo(inputBillIdno))
              .build()
              .render(RenderingStrategies.MYBATIS3);
      List<RptBillMain> rptBillMainresults = rptBillMainMapper.selectMany(rptBillMainProvider);
      log.debug("query data from RPT_BILL_MAIN end, "
          + " time: " + getCurrentDateTime());
      singleArrearsOutputStr.setBiiIdno(input.getBillIdno());

      // 查無資料
      if (rptBillMainresults.size() == 0) {
        singleArrearsOutputStr.setProcResult("99");
        singleArrearsOutputStr.setProcDescription("99"); // code 99 for data
        return singleArrearsOutputStr;
      } else {
        // 繼續執行
        singleArrearsOutputStr.setProcResult("00");
        singleArrearsOutputStr.setProcDescription(
            "10"); // code 10 for query data ok from rpt_bill_main
        singleArrearsOutputStr.setBillCnt(String.valueOf(rptBillMainresults.size()));

        List<IdnoData> idnoDataList = new ArrayList<>();

        for (int i = 0; i < rptBillMainresults.size(); i++) {
          IdnoData idnoData = new IdnoData();
          idnoData.setBillOff(rptBillMainresults.get(i).getBillOff());
          idnoData.setBillTel(rptBillMainresults.get(i).getBillTel());
          idnoData.setBillMonth(rptBillMainresults.get(i).getBillMonth());
          idnoData.setBillId(rptBillMainresults.get(i).getBillId());
          idnoData.setBillAmt(
              rptBillMainresults.get(i).getBillAmt().setScale(0).toString()); // 還要處理小數
          idnoData.setPaylimit(rptBillMainresults.get(i).getPaylimit());
          idnoData.setPaytype(rptBillMainresults.get(i).getPaytype());
          idnoData.setReceiptNo(rptBillMainresults.get(i).getReceiptNo());
          idnoData.setInvCarrierNo(rptBillMainresults.get(i).getInvCarrierNo());
          idnoData.setBillCycle(rptBillMainresults.get(i).getBillCycle());
          idnoData.setPaylimit(rptBillMainresults.get(i).getPaylimit());
          idnoData.setPaytype(rptBillMainresults.get(i).getPaytype());
          idnoData.setReceiptNo(rptBillMainresults.get(i).getReceiptNo());
          idnoData.setInvCarrierNo(rptBillMainresults.get(i).getInvCarrierNo());

          if (inputSubMark == "Y") {
            // select T1.*
            // from BILL_RELS T1 , RPT_BILL_MAIN T2
            // WHERE T1.BILL_OFF=T2.BILL_OFF
            // AND T1.BILL_TEL=T2.BILL_TEL
            // AND T1.BILL_MONTH=T2.BILL_MONTH
            // --AND T1.BILL_ID=T2.BILL_ID (2024/09/30 效能考量移除 bill_id)
            // AND T2. bill_idno='52399254 '

            // query data set from BILL_RELS base on RPT_BILL_MAIN
            log.debug("query data from BILL_RELS start, "
                + " time: " + getCurrentDateTime());
            SelectStatementProvider billRelsProvider =
                select(billRels.allColumns())
                    .from(billRels)
                    .where(billRels.billOff, isEqualTo(rptBillMainresults.get(i).getBillOff()))
                    .and(billRels.billTel, isEqualTo(rptBillMainresults.get(i).getBillTel()))
                    .and(billRels.billMonth, isEqualTo(rptBillMainresults.get(i).getBillMonth()))
                    .build()
                    .render(RenderingStrategies.MYBATIS3);
            List<BillRels> billRelsResults = billRelsMapper.selectMany(billRelsProvider);
            log.debug("query data from BILL_RELS end, "
                + " time: " + getCurrentDateTime());
            if (billRelsResults.size() == 0) {
              idnoData.setWithSubMark("N");
              idnoData.setSubCnt("0");

              List<SubData> subDataList = new ArrayList<>();
              SubData subData = new SubData();
              subData.setSubOff(rptBillMainresults.get(i).getBillOff());
              subData.setSubTel(rptBillMainresults.get(i).getBillTel());
              subData.setSubAmt(rptBillMainresults.get(i).getBillAmt().setScale(0).toString());
              subDataList.add(subData);

              idnoData.setSubDataSet(subDataList);

              log.debug("N1 idnoData: " + subDataList);
            } else {
              List<BillRels> finalBillRelsResults = new ArrayList<>();
              // 比對如果bill_id,bill_amt與rpt_bill_main一樣，則是為該筆資料
              for (int k = 0; k < billRelsResults.size(); k++) {
                if (billRelsResults.get(k).getBillId().equals(rptBillMainresults.get(i).getBillId())
                    && billRelsResults.get(k).getBillAmt().equals(
                        rptBillMainresults.get(i).getBillAmt())) {
                  finalBillRelsResults.add(billRelsResults.get(k));
                }
              }

              // 如果沒有資料，再比對如果bill_id不一樣但是bill_amt一樣，則也視為該筆資料
              if (finalBillRelsResults.size() == 0) {
                for (int l = 0; l < billRelsResults.size(); l++) {
                  if (!billRelsResults.get(l).getBillId().equals(
                          rptBillMainresults.get(i).getBillId())
                      && billRelsResults.get(l).getBillAmt().equals(
                          rptBillMainresults.get(i).getBillAmt())) {
                    finalBillRelsResults.add(billRelsResults.get(l));
                  }
                }
              }

              if (finalBillRelsResults.size() == 0) {
                idnoData.setWithSubMark("N");
                idnoData.setSubCnt("0");

                List<SubData> subDataList = new ArrayList<>();
                SubData subData = new SubData();
                subData.setSubOff(rptBillMainresults.get(i).getBillOff());
                subData.setSubTel(rptBillMainresults.get(i).getBillTel());
                subData.setSubAmt(rptBillMainresults.get(i).getBillAmt().setScale(0).toString());
                subDataList.add(subData);

                idnoData.setSubDataSet(subDataList);

                log.debug("N2 idnoData: " + subDataList);
              } else {
                idnoData.setWithSubMark("Y");
                List<SubData> subDataList = new ArrayList<>();
                for (int j = 0; j < finalBillRelsResults.size(); j++) {
                  SubData subData = new SubData();
                  subData.setSubOff(finalBillRelsResults.get(j).getSubOff());
                  subData.setSubTel(finalBillRelsResults.get(j).getSubTel());
                  subData.setSubAmt(finalBillRelsResults.get(j).getSubAmt().toString());
                  subDataList.add(subData);
                }

                idnoData.setSubCnt(String.valueOf(finalBillRelsResults.size()));
                idnoData.setSubDataSet(subDataList);

                log.debug("Y idnoData: " + subDataList);
              }
            }
          }
          idnoDataList.add(idnoData);
          log.debug("idnoDataList: " + idnoDataList);
        }
        singleArrearsOutputStr.setIdnoDataSet(idnoDataList);
        log.debug("singleArrearsOutputStr: " + singleArrearsOutputStr);

        log.debug("singleArrearsQuery end, "
            + " time: " + getCurrentDateTime());
      }
    } catch (Exception e) {
      log.error("", e);
      singleArrearsOutputStr.setProcResult("QQ");
    }
    return singleArrearsOutputStr;
  }

  @Override
  public void batchArrearsQuery(BatchArrearsInputStr input) throws Exception {
    String rptCode = "BPGUSUB";
    log.debug("inputFilePath is : " + inputFilePath);
    log.debug("pdfFilePath is : " + pdfFilePath);
    log.debug("csvFilePath is : " + csvFilePath);
    log.debug("watermarkFilePath is : " + watermarkFilePath);
    log.debug("zipFilePath is : " + zipFilePath);

    readBillIdnoListFromFilesAndGenRpt(inputFilePath, rptCode, input);
  }

  private String getCurrentDateTime() {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

  private void readBillIdnoListFromFilesAndGenRpt(
      String filePath, String rptCode, BatchArrearsInputStr input) {
    Path directoryPath = Paths.get(filePath);

    log.debug("directoryPath is : " + directoryPath.toAbsolutePath());

    try (DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath)) {
      for (Path entry : stream) {
        if (Files.isRegularFile(entry)) {
          readSingleFileAndGenRpts(entry, rptCode, input);
        }
      }
    } catch (IOException e) {
      System.err.println("An error occurred while reading files: " + e.getMessage());
    } catch (Exception e) {
      System.err.println("An error occurred while final wirte log " + e.getMessage());
      throw new RuntimeException(e);
    }
  }

  private void readSingleFileAndGenRpts(Path entry, String rptCode, BatchArrearsInputStr input) {
    try {
      String rptLogsId = null;

      // 2. 讀【查欠證號檔】
      String fileName = entry.getFileName().toString();
      log.debug(
          "read arrears file start, file name: " + fileName + " time: " + getCurrentDateTime());

      int fileNameLength = entry.getFileName().toString().length();
      String fileNameWithoutExtension =
          entry.getFileName().toString().substring(0, fileNameLength - 4);

      List<String> content = Files.readAllLines(entry);
      log.debug("File: " + entry.getFileName());
      log.debug("Content:\n" + content);

      // 1.
      // 使用〔報表產製日誌(執行前)〕新增寫報表產製記錄並取得【報表記錄序碼RPT_LOGS_ID】
      log.debug("write log before execute start, "
          + " time: " + getCurrentDateTime());
      RptLogBeforeExecuteInputStr rptLogBeforeExecuteInputStr = new RptLogBeforeExecuteInputStr();
      rptLogBeforeExecuteInputStr.setRptCode(rptCode);
      RptLogBeforeExecuteOutputStr rptLogBeforeExecuteOutputStr =
          comm03Service.COMM03_0001(rptLogBeforeExecuteInputStr);
      log.debug("write log before execute end, "
          + " time: " + getCurrentDateTime());

      log.debug("rptLogBeforeExecuteOutputStr: " + rptLogBeforeExecuteOutputStr);
      if (rptLogBeforeExecuteOutputStr.getProcResult() == "00") {
        rptLogsId = rptLogBeforeExecuteOutputStr.getRptLogsId();
      }
      log.debug("rptLogsId: " + rptLogsId);
      // 3.
      // 使用〔單筆證號查欠〕方法,傳入來源檔【證號】欄位及｛查子號識別｝=「Y」,取得【欠費資料】
      List<ArrearsFileLineInput> arrearsFileInputs = convertArrearsFileInputInput(content);
      log.debug("arrearsFileInputs: " + arrearsFileInputs);
      List<SingleArrearsInputStr> arrearsInputStrs = convertContentToSingleArrearsInputStr(content);

      log.debug("batch arrears query start, "
          + " time: " + getCurrentDateTime());
      List<SingleArrearsOutputStr> singleArrearsOutputStrs =
          batchArrearsQueryByFile(arrearsInputStrs);
      log.debug("batch arrears query end, "
          + " time: " + getCurrentDateTime());
      log.debug("singleArrearsOutputStrs: " + singleArrearsOutputStrs);
      boolean genMaskReportFlg = chkBillIdnoListIncludeNatural(singleArrearsOutputStrs);
      boolean naturalIncludeFlg = genMaskReportFlg;

      // 4.
      // 參考檔案格式及報表例,產製【代表號+子號】及【代表號】兩種類欠費報表(內容依來源檔【員工代號】及【證號】群組/排序顯示)
      // 5.
      // 若產製內容屬自然人號資料,需再另產製該兩種類隱碼報表(使用〔依證號辨別人種〕判斷自然人,
      // 依〔個資隱碼〕 處理欄位隱碼)
      log.debug("generateReportRows start, "
          + " time: " + getCurrentDateTime());
      List<BPGUSUB_ReportRowForm> bpgusub_reportRowsFormOut =
          generateReportRows(arrearsFileInputs, singleArrearsOutputStrs, false);
      log.debug("generateReportRows end, "
          + " time: " + getCurrentDateTime());

      log.debug("generateCsvRows start, "
          + " time: " + getCurrentDateTime());
      List<BPGUSUB_ReportRowForm> bpgusub_reportRowsFormCsvOut =
          generateCsvRows(arrearsFileInputs, singleArrearsOutputStrs, false);
      log.debug("bpgusub_reportRowsFormOut: " + bpgusub_reportRowsFormOut);
      log.debug("generateCsvRows end, "
          + " time: " + getCurrentDateTime());

      // default will gen non mask report
      log.debug("generateRpts start, "
          + " time: " + getCurrentDateTime());
      generateRpts(bpgusub_reportRowsFormOut, fileNameWithoutExtension, false, input,
          arrearsFileInputs, singleArrearsOutputStrs, rptLogsId, rptCode, fileName,
          naturalIncludeFlg, bpgusub_reportRowsFormCsvOut);
      log.debug("generateRpts end, "
          + " time: " + getCurrentDateTime());

      // generate mask report when genMaskReportFlg is true
      if (genMaskReportFlg) {
        // mask report rows
        log.debug("generateReportRows masked start, "
            + " time: " + getCurrentDateTime());
        List<BPGUSUB_ReportRowForm> bpgusub_reportRowsFormOut_mask =
            generateReportRows(arrearsFileInputs, singleArrearsOutputStrs, genMaskReportFlg);
        log.debug("generateReportRows masked end, "
            + " time: " + getCurrentDateTime());

        // mask cav rows
        log.debug("generateCsvRows masked start, "
            + " time: " + getCurrentDateTime());
        List<BPGUSUB_ReportRowForm> bpgusub_reportRowsFormCsvOut_mask =
            generateCsvRows(arrearsFileInputs, singleArrearsOutputStrs, genMaskReportFlg);
        log.debug("generateCsvRows masked end, "
            + " time: " + getCurrentDateTime());

        // mask report generate
        log.debug("generateRpts masked start, "
            + " time: " + getCurrentDateTime());
        generateRpts(bpgusub_reportRowsFormOut_mask, fileNameWithoutExtension, genMaskReportFlg,
            input, arrearsFileInputs, singleArrearsOutputStrs, rptLogsId, rptCode, fileName,
            naturalIncludeFlg, bpgusub_reportRowsFormCsvOut_mask);
        log.debug("generateRpts masked end, "
            + " time: " + getCurrentDateTime());
      }

      log.debug("read arrears file end, file name: " + fileName + " time: " + getCurrentDateTime());
    } catch (Exception e) {
      log.debug("readSingleFileAndGenRpts exception: " + e.getMessage());
    }
  }

  List<SingleArrearsInputStr> tmpInputList = new ArrayList<>();
  int inputIdx = 0;

  private List<SingleArrearsOutputStr> batchArrearsQueryByFile(
      List<SingleArrearsInputStr> arrearsInputStrs) {
    List<SingleArrearsOutputStr> singleArrearsOutputStrs = new CopyOnWriteArrayList<>();

    ExecutorService executor = Executors.newFixedThreadPool(10);

    Callable<String> task = new Callable<String>() {
      @Override
      public String call() throws Exception {
        SingleArrearsInputStr input = tmpInputList.get(inputIdx);
        inputIdx++;
        log.debug("single arrears query start, "
            + " current thread: " + Thread.currentThread().getName()
            + " time: " + getCurrentDateTime() + " ,billIdno: " + input.getBillIdno());
        SingleArrearsOutputStr singleArrearsOutputStr = singleArrearsQuery(input);
        log.debug("single arrears query end, "
            + " current thread: " + Thread.currentThread().getName()
            + " time: " + getCurrentDateTime() + " ,billIdno: " + input.getBillIdno());
        if (singleArrearsOutputStr.getProcResult().equals("00")) {
          singleArrearsOutputStrs.add(singleArrearsOutputStr);
        }
        return "Thread: " + Thread.currentThread().getName() + ", ok!";
      }
    };

    List<Future<String>> futures = new ArrayList<>();

    for (SingleArrearsInputStr input : arrearsInputStrs) {
      tmpInputList.add(input);
      futures.add(executor.submit(task));
    }

    for (Future<String> future : futures) {
      try {
        String result = future.get(600, TimeUnit.SECONDS);
        log.debug(result);
        // }catch (Exception e) {
        // log.debug(e.getMessage());
        // }
      } catch (TimeoutException e) {
        future.cancel(true);
        log.debug("TimeoutException:" + e.getMessage());
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        log.debug("InterruptedException:" + e.getMessage());
      } catch (ExecutionException e) {
        log.debug("ExecutionException:" + e.getMessage() + ", e.getCause()" + e.getCause());
      } finally {
        executor.shutdown();
      }
    }

    // executor.shutdown();
    // while (!executor.isTerminated()) {
    // // Waiting for all tasks to finish
    // }

    return singleArrearsOutputStrs;
  }

  private List<SingleArrearsInputStr> convertContentToSingleArrearsInputStr(List<String> content) {
    List<SingleArrearsInputStr> billIdnoList = new ArrayList<>();

    for (int i = 0; i < content.size(); i++) {
      String billIdno = content.get(i).substring(0, 10);

      SingleArrearsInputStr singleArrearsInputStr = new SingleArrearsInputStr();
      singleArrearsInputStr.setBillIdno(billIdno);
      singleArrearsInputStr.setGetSubMark("Y");

      billIdnoList.add(singleArrearsInputStr);
    }
    return billIdnoList;
  }

  private List<ArrearsFileLineInput> convertArrearsFileInputInput(List<String> content) {
    List<ArrearsFileLineInput> arrearsFileLineInputs = new ArrayList<>();

    for (int i = 0; i < content.size(); i++) {
      ArrearsFileLineInput arrearsFileLineInput = new ArrearsFileLineInput();
      arrearsFileLineInput.setBillIdno(content.get(i).substring(0, 10));
      arrearsFileLineInput.setOffCode(content.get(i).substring(10, 14).trim());
      arrearsFileLineInput.setEmpId(content.get(i).substring(14, 20));
      arrearsFileLineInput.setFileDate(content.get(i).substring(20));

      arrearsFileLineInputs.add(arrearsFileLineInput);
    }

    return arrearsFileLineInputs;
  }

  private boolean chkBillIdnoListIncludeNatural(
      List<SingleArrearsOutputStr> singleArrearsOutputStrs) {
    boolean includeNaturalFlg = false;
    for (int i = 0; i < singleArrearsOutputStrs.size(); i++) {
      if (Comm01ServiceImpl.COMM01_0001(singleArrearsOutputStrs.get(i).getBiiIdno().trim())) {
        includeNaturalFlg = true;
        break;
      }
    }
    return includeNaturalFlg;
  }

  public boolean generateRpts(List<BPGUSUB_ReportRowForm> bpgusub_reportRowsFormOut,
      String fileNameWithoutExtension, boolean genMaskReportFlg, BatchArrearsInputStr input,
      List<ArrearsFileLineInput> arrearsFileInputs,
      List<SingleArrearsOutputStr> singleArrearsOutputStrs, String rptLogsId, String rptCode,
      String fileName, boolean naturalIncludeFlg,
      List<BPGUSUB_ReportRowForm> bpgusub_reportRowsFormCsvOut) throws Exception {
    List<dData> dDataList = new ArrayList<>();

    if (!genMaskReportFlg) {
      // 1.generate pdf
      log.debug("generatePdf start, "
          + " time: " + getCurrentDateTime());
      dData dDataPdf = generatePdf(
          bpgusub_reportRowsFormOut, fileNameWithoutExtension, genMaskReportFlg, naturalIncludeFlg);
      dDataList.add(dDataPdf);
      log.debug("generatePdf end, "
          + " time: " + getCurrentDateTime());

      // 2.generate csv
      log.debug("generateCsv start, "
          + " time: " + getCurrentDateTime());
      dData dDataCsv = generateCsv(bpgusub_reportRowsFormCsvOut, fileNameWithoutExtension,
          genMaskReportFlg, naturalIncludeFlg);
      dDataList.add(dDataCsv);
      log.debug("generateCsv end, "
          + " time: " + getCurrentDateTime());

    } else {
      dData dDataPdfMask = new dData();
      dData dDataCsvMask = new dData();

      // 3.generate mask pdf
      log.debug("generatePdf mask start, "
          + " time: " + getCurrentDateTime());
      dDataPdfMask = generatePdf(
          bpgusub_reportRowsFormOut, fileNameWithoutExtension, genMaskReportFlg, naturalIncludeFlg);
      dDataList.add(dDataPdfMask);
      log.debug("generatePdf mask end, "
          + " time: " + getCurrentDateTime());

      // 4.generate mask csv
      log.debug("generateCsv mask start, "
          + " time: " + getCurrentDateTime());
      dDataCsvMask = generateCsv(bpgusub_reportRowsFormCsvOut, fileNameWithoutExtension,
          genMaskReportFlg, naturalIncludeFlg);
      dDataList.add(dDataCsvMask);
      log.debug("generateCsv mask end, "
          + " time: " + getCurrentDateTime());
    }

    log.debug("generateLogAfterExecuteDDataList start, "
        + " time: " + getCurrentDateTime());
    List<dData> dDataListForLogAfterExecute = generateLogAfterExecuteDDataList(
        singleArrearsOutputStrs, genMaskReportFlg, input, dDataList, arrearsFileInputs);
    log.debug("generateLogAfterExecuteDDataList end, "
        + " time: " + getCurrentDateTime());

    log.debug("updateLogAfterExecute start, "
        + " time: " + getCurrentDateTime());
    boolean logUpdateFlg = updateLogAfterExecute(
        rptLogsId, input, rptCode, fileName, dDataListForLogAfterExecute, genMaskReportFlg);
    log.debug("updateLogAfterExecute end, "
        + " time: " + getCurrentDateTime());
    return logUpdateFlg;
  }

  private boolean updateLogAfterExecute(String rptLogsId, BatchArrearsInputStr input,
      String rptCode, String fileName, List<dData> dDataListForLogAfterExecute,
      boolean genMaskReportFlg) {
    // 6.
    // 依【報表記錄序碼RPT_LOGS_ID】,使用〔報表產製日誌(執行後)〕更新及寫入報表產製日誌
    RptLogAfterExecuteInputStr rptLogAfterExecuteInputStr = new RptLogAfterExecuteInputStr();
    rptLogAfterExecuteInputStr.setRptLogsId(rptLogsId);
    rptLogAfterExecuteInputStr.setIsRerun(input.getIsRerun());
    rptLogAfterExecuteInputStr.setRptCode(rptCode);
    rptLogAfterExecuteInputStr.setCreateEmpid("SYSTEM");

    if (genMaskReportFlg) {
      // create count *2 due to gen file for non-mask and mask twice
      rptLogAfterExecuteInputStr.setCreateCount(dDataListForLogAfterExecute.size() * 2);
    } else {
      rptLogAfterExecuteInputStr.setCreateCount(dDataListForLogAfterExecute.size());
    }
    rptLogAfterExecuteInputStr.setErrorount(0);
    rptLogAfterExecuteInputStr.setParamIntValuse("");
    rptLogAfterExecuteInputStr.setParamExtValuse(fileName);
    rptLogAfterExecuteInputStr.setDDataSet(dDataListForLogAfterExecute);
    log.debug("COMM03_0002 start, "
        + " time: " + getCurrentDateTime());
    RptLogAfterExecuteOutputStr rptLogAfterExecuteOutputStr =
        comm03Service.COMM03_0002(rptLogAfterExecuteInputStr);
    log.debug("COMM03_0002 end, "
        + " time: " + getCurrentDateTime());
    if (rptLogAfterExecuteOutputStr.getProcResult() != "00") {
      inputFilesCnt++;
      genRptFailFilesCnt++;
    } else {
      inputFilesCnt++;
      genRptOkFilesCnt++;
      return true;
    }
    return false;
  }

  private List<dData> generateLogAfterExecuteDDataList(
      List<SingleArrearsOutputStr> singleArrearsOutputStrs, boolean genSecretReportFlg,
      BatchArrearsInputStr input, List<dData> fileDataList,
      List<ArrearsFileLineInput> arrearsFileInputs) {
    List<dData> dDataListOut = new ArrayList<>();
    for (int i = 0; i < fileDataList.size(); i++) {
      dData data = new dData();
      data.setRptFileName(fileDataList.get(i).getRptFileName());
      data.setBillOff(arrearsFileInputs.get(0).getOffCode());
      data.setRptTimes("1");
      data.setBillMonth(null);
      data.setBillCycle(null);
      data.setRptDate(input.getOpcDate());
      data.setRptQuarter(null);
      data.setOffAdmin(null);
      data.setRptFileCount(fileDataList.get(i).getRptFileCount());
      data.setRptFileAmt(null);
      data.setRptSecretMark(fileDataList.get(i).getRptSecretMark());

      dDataListOut.add(data);
    }
    return dDataListOut;
  }

  private BigDecimal getRptFileTotalAmt(List<SingleArrearsOutputStr> singleArrearsOutputStrs) {
    BigDecimal totalAmt = new BigDecimal('0');
    for (int i = 0; i < singleArrearsOutputStrs.size(); i++) {
      for (int j = 0; j < singleArrearsOutputStrs.get(i).getIdnoDataSet().size(); j++) {
        BigDecimal tmpAmt =
            new BigDecimal(singleArrearsOutputStrs.get(i).getIdnoDataSet().get(j).getBillAmt());
        totalAmt.add(tmpAmt);
      }
    }
    return totalAmt;
  }

  private dData generateCsv(List<BPGUSUB_ReportRowForm> rows, String fileNameWithoutExtension,
      boolean genSecretReportFlg, boolean naturalIncludeFlg) {
    // String csvFilePath = "example.csv"; // Path to the CSV file

    // TYPE應該是OP_TYPE, 我們這邊應該都是2:排程
    // BPGUSUB{TYPE}_{INPUT_FILE_NAME}.csv
    // BPGUSUB{TYPE}_{INPUT_FILE_NAME}_MASK.csv
    String csvFileName;
    if (genSecretReportFlg) {
      csvFileName = "BPGUSUB2_" + fileNameWithoutExtension + "_MASK.csv";
    } else {
      csvFileName = "BPGUSUB2_" + fileNameWithoutExtension + ".csv";
    }

    String csvFileAbsolutePath = csvFilePath + csvFileName;

    try (BufferedWriter writer = new BufferedWriter(
             new OutputStreamWriter(new FileOutputStream(csvFileAbsolutePath), "UTF-8"))) {
      writer.write('\uFEFF'); // BOM for UTF-8

      String ouputLine = "";
      int finalRowIdx = rows.size() - 1;

      // 員工代號, 證號, 代表號機構, 代表號, 出帳年月, 帳單識別, 繳費期限, 繳費方式,
      // 收據號碼, 變動載具號碼, 總金額, 序號 6, 10, 4, 12, 5, 2, 7, 1, 13, 30, 10, 10

      String header =
          "員工代號,證號,代表號機構,代表號,出帳年月,帳單識別,繳費期限,繳費方式,收據號碼,變動載具號碼,總金額,序號,"
          + "\n";
      ouputLine = header;
      for (int i = 0; i < rows.size(); i++) {
        // 文數字左靠、數字右靠
        // 代表號右靠
        // 繳費期限 輸出民國年月日

        String paddedEmpId = String.format("%-6s", rows.get(i).getEmpId());
        String paddedBillIdno = String.format("%-10s", rows.get(i).getBillIdno());
        String paddedBillOff = String.format("%-4s", rows.get(i).getBillOff());
        String paddedBillTel = String.format("%12s", rows.get(i).getBillTel());
        String paddedBillMonth = String.format("%5s", rows.get(i).getBillMonth());
        String paddedBillId = String.format("%-2s", rows.get(i).getBillId());

        String rocPayLimit = getRocDate(rows.get(i).getPayLimit());
        String paddedPayLimit = String.format("%7s", rocPayLimit);

        String paddedPayType = String.format("%1s", rows.get(i).getPayType());
        String paddedReceiptNo = String.format("%-13s", rows.get(i).getReceiptNo());
        String paddedInvCarrierNo = String.format("%-30s", rows.get(i).getInvCarrierNo());
        String paddedBillAmt = String.format("%10s", rows.get(i).getBillAmt());
        String paddedSeqNo = String.format("%10s", rows.get(i).getSeqNo());

        ouputLine += paddedEmpId + "," + paddedBillIdno + "," + paddedBillOff + "," + paddedBillTel
            + "," + paddedBillMonth + "," + paddedBillId + "," + paddedPayLimit + ","
            + paddedPayType + "," + paddedReceiptNo + "," + paddedInvCarrierNo + "," + paddedBillAmt
            + "," + paddedSeqNo;

        if (i < finalRowIdx) {
          ouputLine = ouputLine + ","
              + "\n";
        }
      }

      writer.write(ouputLine);

      writer.flush();

      dData result = new dData();
      result.setRptFileName(csvFileName);
      result.setRptFileCount(rows.size());
      if (naturalIncludeFlg && !genSecretReportFlg) {
        result.setRptSecretMark("Y");
      } else {
        result.setRptSecretMark("N");
      }
      return result;
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  private String getRocDate(String inputDate) {
    String yyyymmdd = inputDate.replace("-", "");
    int rocInt = Integer.parseInt(yyyymmdd) - 19110000;
    String cyyymmdd = String.valueOf(rocInt);
    return cyyymmdd;
  }

  public dData generatePdf(List<BPGUSUB_ReportRowForm> bpgusub_reportRowForms, String fileName,
      boolean genSecretReportFlg, boolean naturalIncludeFlg) throws Exception {
    // TYPE應該是OP_TYPE, 我們這邊應該都是2:排程
    // BPGUSUB{TYPE}_{INPUT_FILE_NAME}.pdf
    // BPGUSUB{TYPE}_{INPUT_FILE_NAME}_MASK.pdf
    String pdfFileName;
    if (genSecretReportFlg) {
      pdfFileName = "BPGUSUB1_" + fileName + "_MASK.pdf";
    } else {
      pdfFileName = "BPGUSUB1_" + fileName + ".pdf";
    }

    Document document = null;
    PdfWriter writer = null;

    try {
      // 產生縱向文件
      document = ArrearsRptServiceImpl.PdfUtils.createPortraitDocument(20, 20, 10, 20);
      // 建立一個writer(內存stream方式)
      writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath + pdfFileName));
      // 打開文檔
      document.open();

      String rptTitle = "證號查欠(含子費查欠)";
      // 首頁標題+表頭
      ArrearsRptServiceImpl.PdfUtils.creatHeader(document, writer, rptTitle);

      // 載入下一頁前置處理
      // PdfReportServiceImpl.PdfHeaderFooterEvent event = new
      // PdfReportServiceImpl.PdfHeaderFooterEvent(branchname);
      // writer.setPageEvent(event);

      // body
      ArrearsRptServiceImpl.PdfUtils.creatBody(document, bpgusub_reportRowForms);

      // footer
      // ArrearsServiceImpl.PdfUtils.creatFooter(document);
      // 5.關閉文檔
      document.close();
      // 6.關閉書寫器
      writer.close();

      dData result = new dData();
      if (naturalIncludeFlg && !genSecretReportFlg) {
        result.setRptSecretMark("Y");
      } else {
        result.setRptSecretMark("N");
      }
      result.setRptFileCount(bpgusub_reportRowForms.size());
      result.setRptFileName(pdfFileName);

      return result;

    } catch (Exception e) {
      e.printStackTrace();
    }
    // return new BPGUSUB_ReportRowForm();
    return null;
  }

  private List<BPGUSUB_ReportRowForm> generateReportRows(
      List<ArrearsFileLineInput> arrearsFileLineInputs,
      List<SingleArrearsOutputStr> singleArrearsOutputStrs, boolean genSecretReportFlg) {
    List<BPGUSUB_ReportRowForm> bpgusub_reportRowForms = new ArrayList<>();
    BigDecimal seqNo = new BigDecimal(1);

    for (int i = 0; i < singleArrearsOutputStrs.size(); i++) {
      BPGUSUB_ReportRowForm bpgusub_reportRowForm = new BPGUSUB_ReportRowForm();
      bpgusub_reportRowForm.setBillIdno(singleArrearsOutputStrs.get(i).getBiiIdno());
      bpgusub_reportRowForm.setEmpId(arrearsFileLineInputs.get(i).getEmpId());

      if (genSecretReportFlg) {
        PersonalInfoMaskStr personalInfoMaskStrIn = new PersonalInfoMaskStr();
        personalInfoMaskStrIn.setMaskIDNumber(arrearsFileLineInputs.get(i).getBillIdno());

        PersonalInfoMaskStr personalInfoMaskStrOut =
            Comm01ServiceImpl.COMM01_0002(personalInfoMaskStrIn);
        bpgusub_reportRowForm.setBillIdno(personalInfoMaskStrOut.getMaskIDNumber());
      }

      for (int j = 0; j < singleArrearsOutputStrs.get(i).getIdnoDataSet().size(); j++) {
        BPGUSUB_ReportRowForm bpgusubReportRowFormIn = new BPGUSUB_ReportRowForm();
        bpgusubReportRowFormIn.setBillIdno(bpgusub_reportRowForm.getBillIdno());
        bpgusubReportRowFormIn.setEmpId(bpgusub_reportRowForm.getEmpId());
        bpgusubReportRowFormIn.setBillIdno(bpgusub_reportRowForm.getBillIdno());
        bpgusubReportRowFormIn.setBillOff(
            singleArrearsOutputStrs.get(i).getIdnoDataSet().get(j).getBillOff());
        bpgusubReportRowFormIn.setBillTel(
            singleArrearsOutputStrs.get(i).getIdnoDataSet().get(j).getBillTel());
        bpgusubReportRowFormIn.setBillMonth(
            singleArrearsOutputStrs.get(i).getIdnoDataSet().get(j).getBillMonth());
        bpgusubReportRowFormIn.setBillId(
            singleArrearsOutputStrs.get(i).getIdnoDataSet().get(j).getBillId());
        bpgusubReportRowFormIn.setBillAmt(
            singleArrearsOutputStrs.get(i).getIdnoDataSet().get(j).getBillAmt());

        // add column for csv file
        bpgusubReportRowFormIn.setPayType(
            singleArrearsOutputStrs.get(i).getIdnoDataSet().get(j).getPaytype());
        bpgusubReportRowFormIn.setPayLimit(
            singleArrearsOutputStrs.get(i).getIdnoDataSet().get(j).getPaylimit());
        bpgusubReportRowFormIn.setReceiptNo(
            singleArrearsOutputStrs.get(i).getIdnoDataSet().get(j).getReceiptNo());
        bpgusubReportRowFormIn.setInvCarrierNo(
            singleArrearsOutputStrs.get(i).getIdnoDataSet().get(j).getInvCarrierNo());

        if (Integer.parseInt(singleArrearsOutputStrs.get(i).getIdnoDataSet().get(j).getSubCnt())
            >= 0) {
          for (int k = 0;
               k < singleArrearsOutputStrs.get(i).getIdnoDataSet().get(j).getSubDataSet().size();
               k++) {
            BPGUSUB_ReportRowForm bpgusubReportRowFormFinal = new BPGUSUB_ReportRowForm();

            bpgusubReportRowFormFinal.setEmpId(bpgusubReportRowFormIn.getEmpId());
            bpgusubReportRowFormFinal.setBillIdno(bpgusubReportRowFormIn.getBillIdno());
            bpgusubReportRowFormFinal.setBillOff(bpgusubReportRowFormIn.getBillOff());
            bpgusubReportRowFormFinal.setBillTel(bpgusubReportRowFormIn.getBillTel());
            bpgusubReportRowFormFinal.setBillMonth(bpgusubReportRowFormIn.getBillMonth());
            bpgusubReportRowFormFinal.setBillId(bpgusubReportRowFormIn.getBillId());
            bpgusubReportRowFormFinal.setBillAmt(bpgusubReportRowFormIn.getBillAmt());

            // add column for csv file
            bpgusubReportRowFormFinal.setPayType(bpgusubReportRowFormIn.getPayType());
            bpgusubReportRowFormFinal.setPayLimit(bpgusubReportRowFormIn.getPayLimit());
            bpgusubReportRowFormFinal.setReceiptNo(bpgusubReportRowFormIn.getReceiptNo());
            bpgusubReportRowFormFinal.setInvCarrierNo(bpgusubReportRowFormIn.getInvCarrierNo());

            bpgusubReportRowFormFinal.setSubOff(singleArrearsOutputStrs.get(i)
                                                    .getIdnoDataSet()
                                                    .get(j)
                                                    .getSubDataSet()
                                                    .get(k)
                                                    .getSubOff());
            bpgusubReportRowFormFinal.setSubTel(singleArrearsOutputStrs.get(i)
                                                    .getIdnoDataSet()
                                                    .get(j)
                                                    .getSubDataSet()
                                                    .get(k)
                                                    .getSubTel());
            bpgusubReportRowFormFinal.setSubAmt(singleArrearsOutputStrs.get(i)
                                                    .getIdnoDataSet()
                                                    .get(j)
                                                    .getSubDataSet()
                                                    .get(k)
                                                    .getSubAmt());
            bpgusubReportRowFormFinal.setSeqNo(seqNo);
            seqNo = seqNo.add(new BigDecimal(1));

            bpgusub_reportRowForms.add(bpgusubReportRowFormFinal);
          }
        } else {
          bpgusubReportRowFormIn.setSeqNo(seqNo);
          seqNo = seqNo.add(new BigDecimal(1));
          bpgusub_reportRowForms.add(bpgusubReportRowFormIn);
        }
      }
    }

    return bpgusub_reportRowForms;
  }

  private List<BPGUSUB_ReportRowForm> generateCsvRows(
      List<ArrearsFileLineInput> arrearsFileLineInputs,
      List<SingleArrearsOutputStr> singleArrearsOutputStrs, boolean genSecretReportFlg) {
    List<BPGUSUB_ReportRowForm> bpgusub_reportRowForms = new ArrayList<>();
    BigDecimal seqNo = new BigDecimal(1);

    for (int i = 0; i < singleArrearsOutputStrs.size(); i++) {
      BPGUSUB_ReportRowForm bpgusub_reportRowForm = new BPGUSUB_ReportRowForm();
      bpgusub_reportRowForm.setBillIdno(singleArrearsOutputStrs.get(i).getBiiIdno());
      bpgusub_reportRowForm.setEmpId(arrearsFileLineInputs.get(i).getEmpId());

      if (genSecretReportFlg) {
        PersonalInfoMaskStr personalInfoMaskStrIn = new PersonalInfoMaskStr();
        personalInfoMaskStrIn.setMaskIDNumber(arrearsFileLineInputs.get(i).getBillIdno());

        PersonalInfoMaskStr personalInfoMaskStrOut =
            Comm01ServiceImpl.COMM01_0002(personalInfoMaskStrIn);
        bpgusub_reportRowForm.setBillIdno(personalInfoMaskStrOut.getMaskIDNumber());
      }

      for (int j = 0; j < singleArrearsOutputStrs.get(i).getIdnoDataSet().size(); j++) {
        BPGUSUB_ReportRowForm bpgusubReportRowFormIn = new BPGUSUB_ReportRowForm();
        bpgusubReportRowFormIn.setBillIdno(bpgusub_reportRowForm.getBillIdno());
        bpgusubReportRowFormIn.setEmpId(bpgusub_reportRowForm.getEmpId());
        bpgusubReportRowFormIn.setBillIdno(bpgusub_reportRowForm.getBillIdno());
        bpgusubReportRowFormIn.setBillOff(
            singleArrearsOutputStrs.get(i).getIdnoDataSet().get(j).getBillOff());
        bpgusubReportRowFormIn.setBillTel(
            singleArrearsOutputStrs.get(i).getIdnoDataSet().get(j).getBillTel());
        bpgusubReportRowFormIn.setBillMonth(
            singleArrearsOutputStrs.get(i).getIdnoDataSet().get(j).getBillMonth());
        bpgusubReportRowFormIn.setBillId(
            singleArrearsOutputStrs.get(i).getIdnoDataSet().get(j).getBillId());
        bpgusubReportRowFormIn.setBillAmt(
            singleArrearsOutputStrs.get(i).getIdnoDataSet().get(j).getBillAmt());

        // add column for csv file
        bpgusubReportRowFormIn.setPayType(
            singleArrearsOutputStrs.get(i).getIdnoDataSet().get(j).getPaytype());
        bpgusubReportRowFormIn.setPayLimit(
            singleArrearsOutputStrs.get(i).getIdnoDataSet().get(j).getPaylimit());
        bpgusubReportRowFormIn.setReceiptNo(
            singleArrearsOutputStrs.get(i).getIdnoDataSet().get(j).getReceiptNo());
        bpgusubReportRowFormIn.setInvCarrierNo(
            singleArrearsOutputStrs.get(i).getIdnoDataSet().get(j).getInvCarrierNo());

        bpgusubReportRowFormIn.setSeqNo(seqNo);
        seqNo = seqNo.add(new BigDecimal(1));
        bpgusub_reportRowForms.add(bpgusubReportRowFormIn);
      }
    }

    return bpgusub_reportRowForms;
  }

  public static class PdfUtils {
    /**
     * @descriptoin: 創建縱向檔案
     */
    public static Document createPortraitDocument(
        float left, float right, float top, float bottom) {
      // 生成pdf
      Document document = new Document();
      // 頁面大小
      Rectangle rectangle = new Rectangle(PageSize.A4);
      // 頁面背景顏色
      rectangle.setBackgroundColor(BaseColor.WHITE);
      document.setPageSize(rectangle);
      // 頁邊距 左, 右, 上, 下
      document.setMargins(left, right, top, bottom);
      return document;
    }

    /**
     * @descriptoin: 創建段落內容
     * @param: text 段落內容
     * @param: font 字體
     */
    public static Paragraph createParagraph(String text, com.itextpdf.text.Font font, int align) {
      Paragraph elements = new Paragraph(text, font);
      // 設置段落前後間距
      elements.setSpacingBefore(5);
      elements.setSpacingAfter(5);
      // 設置段落對齊方式
      if (align != 0) {
        elements.setAlignment(align);
      }
      return elements;
    }

    /**
     * @descriptoin: 創建字體樣式
     * @param: fontSize  字體大小
     * @param: fontColor 字體顏色
     */
    public static com.itextpdf.text.Font createFont(
        int fontSize, int fontStyle, BaseColor fontColor) {
      // 中文字體,解決無法顯示中文問題
      BaseFont bf = null;
      if (fontSize == 0) {
        fontSize = com.itextpdf.text.Font.DEFAULTSIZE;
      }
      if (fontStyle == 0) {
        fontStyle = com.itextpdf.text.Font.NORMAL;
      }
      if (fontColor == null) {
        fontColor = BaseColor.BLACK;
      }
      // STSong-Light/UniGB-UCS2-H; MHei-Medium/UniCNS-UCS2-H
      try {
        bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return new com.itextpdf.text.Font(bf, fontSize, fontStyle, fontColor);
    }

    /**
     * @descriptoin: 創建居中的單元格
     */
    public static PdfPCell createCenterPdfPCell(Paragraph paragraph) {
      PdfPCell cell = new PdfPCell(paragraph);
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      return cell;
    }

    public static PdfPCell creaetLeftPdfPCell(Paragraph paragraph) {
      PdfPCell cell = new PdfPCell(paragraph);
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      cell.setHorizontalAlignment(Element.ALIGN_LEFT);
      return cell;
    }

    public static PdfPCell createRightPdfPCell(Paragraph paragraph) {
      PdfPCell cell = new PdfPCell(paragraph);
      cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
      return cell;
    }

    /**
     * @throws IOException
     * @descriptoin: 創建表頭
     */
    public static void creatHeader(Document document, PdfWriter writer, String rptTitle)
        throws DocumentException, IOException {
      int fontSize = 7;

      // title
      Paragraph title = ArrearsRptServiceImpl.PdfUtils.createParagraph(rptTitle,
          ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null), Element.ALIGN_LEFT);
      // Paragraph info = PdfReportServiceImpl.PdfUtils.createParagraph(
      // "報表產出日期：" + reportnameDate + "頁次：" + writer.getPageNumber() +
      // "頁/共" + "" + "頁", PdfReportServiceImpl.PdfUtils.createFont(8, 0,
      // null), Element.ALIGN_RIGHT);
      document.add(title);
      // document.add(info);

      // header
      PdfPTable header = new PdfPTable(11);
      int headerWidth[] = {10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10};
      header.setWidths(headerWidth); // 設置欄位寬度
      header.setHorizontalAlignment(0); // 表格置左
      header.setWidthPercentage(100); // 寬度100%填充

      PdfPCell cell1 = ArrearsRptServiceImpl.PdfUtils.createCenterPdfPCell(
          new Paragraph("員工代號", ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null)));
      cell1.setRowspan(2);
      cell1.disableBorderSide(15);
      header.addCell(cell1);

      PdfPCell cell2 = ArrearsRptServiceImpl.PdfUtils.createCenterPdfPCell(
          new Paragraph("證號", ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null)));
      cell2.setRowspan(2);
      cell2.disableBorderSide(15);
      header.addCell(cell2);

      PdfPCell cell3 = ArrearsRptServiceImpl.PdfUtils.createCenterPdfPCell(new Paragraph(
          "代表號機構", ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null)));
      cell3.setRowspan(2);
      cell3.disableBorderSide(15);
      header.addCell(cell3);

      PdfPCell cell4 = ArrearsRptServiceImpl.PdfUtils.createCenterPdfPCell(
          new Paragraph("代表號", ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null)));
      cell4.setRowspan(2);
      cell4.disableBorderSide(15);
      header.addCell(cell4);

      PdfPCell cell5 = ArrearsRptServiceImpl.PdfUtils.createCenterPdfPCell(
          new Paragraph("出帳年月", ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null)));
      cell5.setRowspan(2);
      cell5.disableBorderSide(15);
      header.addCell(cell5);

      PdfPCell cell6 = ArrearsRptServiceImpl.PdfUtils.createCenterPdfPCell(
          new Paragraph("帳單別", ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null)));
      cell6.setRowspan(2);
      cell6.disableBorderSide(15);
      header.addCell(cell6);

      PdfPCell cell7 = ArrearsRptServiceImpl.PdfUtils.createCenterPdfPCell(
          new Paragraph("帳單金額", ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null)));
      cell7.setRowspan(2);
      cell7.disableBorderSide(15);
      header.addCell(cell7);

      PdfPCell cell8 = ArrearsRptServiceImpl.PdfUtils.createCenterPdfPCell(
          new Paragraph("子號機構", ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null)));
      cell8.setRowspan(2);
      cell8.disableBorderSide(15);
      header.addCell(cell8);

      PdfPCell cell9 = ArrearsRptServiceImpl.PdfUtils.createCenterPdfPCell(
          new Paragraph("子號", ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null)));
      cell9.setRowspan(2);
      cell9.disableBorderSide(15);
      header.addCell(cell9);

      PdfPCell cell10 = ArrearsRptServiceImpl.PdfUtils.createCenterPdfPCell(
          new Paragraph("子號金額", ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null)));
      cell10.setRowspan(2);
      cell10.disableBorderSide(15);
      header.addCell(cell10);

      PdfPCell cell11 = ArrearsRptServiceImpl.PdfUtils.createCenterPdfPCell(
          new Paragraph("序號", ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null)));
      cell11.setRowspan(2);
      cell11.disableBorderSide(15);
      header.addCell(cell11);

      document.add(header);

      LineSeparator lineSeparator = new LineSeparator();
      lineSeparator.setLineWidth(1f); // Set the width of the line
      lineSeparator.setLineColor(new BaseColor(0, 0, 0)); // Set the color of the line (black)
      document.add(lineSeparator);
      // 指定頁眉
      // header.writeSelectedRows(0, -1, 20, PageSize.A4.getHeight() -20,
      // writer.getDirectContent());
    }

    /**
     * @descriptoin: 創建內容
     */
    public static void creatBody(Document document,
        List<BPGUSUB_ReportRowForm> bpgusub_reportRowForms) throws DocumentException {
      int fontSize = 7;

      for (int i = 0; i < bpgusub_reportRowForms.size(); i++) {
        BPGUSUB_ReportRowForm resultRow = bpgusub_reportRowForms.get(i);

        String paddedTmpSubOff =
            String.format("%4s", resultRow.getSubOff() == null ? "    " : resultRow.getSubOff());
        String paddedTmpSubTel = String.format(
            "%-12s", resultRow.getSubTel() == null ? "            " : resultRow.getSubTel());
        String paddedTmpSubAmt = String.format(
            "%10s", resultRow.getSubAmt() == null ? "          " : resultRow.getSubAmt());
        String paddedBillOff = String.format("%4s", resultRow.getBillOff());
        String paddedBillTel = String.format("%-12s", resultRow.getBillTel());
        String paddedBillAmt = String.format("%10s", resultRow.getBillAmt());
        String paddedSeqNo = String.format("%10s", resultRow.getSeqNo());

        BPGUSUB_ReportRowForm previousRow = new BPGUSUB_ReportRowForm();
        ; // 前一筆data
        BPGUSUB_ReportRowForm nextRow = new BPGUSUB_ReportRowForm();
        if (i > 0) {
          previousRow = bpgusub_reportRowForms.get(i - 1);
        }
        if (bpgusub_reportRowForms.size() > 1 && i < bpgusub_reportRowForms.size() - 1) {
          nextRow = bpgusub_reportRowForms.get(i + 1); // 後一筆資料
        }
        // if (i < bpgusub_reportRowForms.size() - 1) {
        // nextRow = bpgusub_reportRowForms.get(i + 1);
        // }

        PdfPTable pt02 = new PdfPTable(11);
        int contentWidth[] = {10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10};
        pt02.setWidths(contentWidth); // 設置欄位寬度
        pt02.setHorizontalAlignment(0);
        pt02.setWidthPercentage(100);

        PdfPCell cell001;
        PdfPCell cell002;

        if (resultRow.getEmpId().equals(previousRow.getEmpId())
            && resultRow.getBillIdno().equals(previousRow.getBillIdno())
            && i != 0) { // 同員工編號、證號去底線
          cell001 = ArrearsRptServiceImpl.PdfUtils.creaetLeftPdfPCell(
              new Paragraph(" ", ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null)));
          cell001.disableBorderSide(15);

          cell002 = ArrearsRptServiceImpl.PdfUtils.creaetLeftPdfPCell(
              new Paragraph(" ", ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null)));
          cell002.disableBorderSide(15);

        } else {
          cell001 = ArrearsRptServiceImpl.PdfUtils.creaetLeftPdfPCell(new Paragraph(
              resultRow.getEmpId(), ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null)));
          cell001.disableBorderSide(15);

          cell002 = ArrearsRptServiceImpl.PdfUtils.creaetLeftPdfPCell(
              new Paragraph(resultRow.getBillIdno(),
                  ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null)));
          cell002.disableBorderSide(15);
        }

        PdfPCell cell003 = ArrearsRptServiceImpl.PdfUtils.createRightPdfPCell(new Paragraph(
            paddedBillOff, ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null)));
        cell003.disableBorderSide(15);

        PdfPCell cell004 = ArrearsRptServiceImpl.PdfUtils.createRightPdfPCell(new Paragraph(
            paddedBillTel, ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null)));
        cell004.disableBorderSide(15);

        PdfPCell cell005 = ArrearsRptServiceImpl.PdfUtils.createRightPdfPCell(
            new Paragraph(resultRow.getBillMonth(),
                ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null)));
        cell005.disableBorderSide(15);

        PdfPCell cell006 = ArrearsRptServiceImpl.PdfUtils.createRightPdfPCell(new Paragraph(
            resultRow.getBillId(), ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null)));
        cell006.disableBorderSide(15);

        PdfPCell cell007 = ArrearsRptServiceImpl.PdfUtils.createRightPdfPCell(new Paragraph(
            paddedBillAmt, ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null)));
        cell007.disableBorderSide(15);

        PdfPCell cell008 = new PdfPCell();
        if (!resultRow.isSubOffNull()) {
          cell008 = ArrearsRptServiceImpl.PdfUtils.createRightPdfPCell(new Paragraph(
              paddedTmpSubOff, ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null)));
          cell008.disableBorderSide(15);
        }

        PdfPCell cell009 = new PdfPCell();
        if (!resultRow.isSubTelNull()) {
          cell009 = ArrearsRptServiceImpl.PdfUtils.createRightPdfPCell(new Paragraph(
              paddedTmpSubTel, ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null)));
          cell009.disableBorderSide(15);
        }

        PdfPCell cell010 = new PdfPCell();
        if (!resultRow.isSubAmtNull()) {
          cell010 = ArrearsRptServiceImpl.PdfUtils.createRightPdfPCell(new Paragraph(
              paddedTmpSubAmt, ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null)));
          cell010.disableBorderSide(15);
        }

        PdfPCell cell011 = ArrearsRptServiceImpl.PdfUtils.createRightPdfPCell(new Paragraph(
            paddedSeqNo, ArrearsRptServiceImpl.PdfUtils.createFont(fontSize, 0, null)));
        cell011.disableBorderSide(15);

        pt02.addCell(cell001);
        pt02.addCell(cell002);
        pt02.addCell(cell003);
        pt02.addCell(cell004);
        pt02.addCell(cell005);
        pt02.addCell(cell006);
        pt02.addCell(cell007);
        pt02.addCell(cell008);
        pt02.addCell(cell009);
        pt02.addCell(cell010);
        pt02.addCell(cell011);
        document.add(pt02);
      }
    }
  }

  @Override
  @RptLogExecution(rptCode = "BP221D6")
  public Result batchBP221D6Rpt(BatchSimpleRptInStr input) {
    String rptCode = "BP221D6";
    String rptLogsId = null;
    String jobId = input.getJobId();
    String opcDate = input.getOpcDate();
    String rocDate = DateUtils.convertToRocDate(opcDate);
    String opcYYYMM = input.getOpcYearMonth();
    String rocYYYMM = DateUtils.convertToRocYearMonth(opcYYYMM);
    String isRerun = input.getIsRerun();
    String csvFileName = "";

    Integer createCount = 0;
    Integer errorCount = 0;
    List<dData> dDataList = new ArrayList<>();
    List<String> outputFiles = new ArrayList<>();
    String[] offTypes = {"A", "B", "C", "D"};
    for (String offType : offTypes) {
      String errorMessage = "";

      List<RptAccountSummary> aAccountSummaries = null;
      List<RptAccountSummary> bAccountSummaries = null;
      List<RptAccountSummary> cAccountSummaries = null;
      List<RptAccountSummary> dAccountSummaries = null;
      List<RptAccountSummary> eAccountSummaries = null;
      List<RptAccountSummary> fAccountSummaries = null;
      if (!"D".equals(offType)) {
        aAccountSummaries = rptAccountSummaryMapper.selectRptAccountSummary(
            opcYYYMM, offType, DateTypeConstants.ACU_X_2M);
        bAccountSummaries = rptAccountSummaryMapper.selectRptAccountSummary(
            opcYYYMM, offType, DateTypeConstants.ACU_X_1M);
        cAccountSummaries = rptAccountSummaryMapper.selectRptAccountSummary(
            opcYYYMM, offType, DateTypeConstants.SUM_X_1Y);
        dAccountSummaries = rptAccountSummaryMapper.selectRptAccountSummary(
            opcYYYMM, offType, DateTypeConstants.ACU_X_2Y);
        eAccountSummaries = rptAccountSummaryMapper.selectRptAccountSummary(
            opcYYYMM, offType, DateTypeConstants.SUM_X_1Y_11);
        fAccountSummaries = rptAccountSummaryMapper.selectRptAccountSummary(
            opcYYYMM, offType, DateTypeConstants.SUM_X_1Y_12);
      } else {
        aAccountSummaries = rptAccountSummaryMapper.selectRptAccountSummary(
            opcYYYMM, offType, DateTypeConstants.ACU_X_2M);
      }

      // 將所有的 AccountSummaries 轉換並加入到 rows 中
      List<BP221D6_ReportRowForm> rows = new ArrayList<>();

      // 收集所有的 bill off belong 並去重
      Set<String> billOffBelongSet = new HashSet<>();
      for (RptAccountSummary summary : aAccountSummaries) {
        billOffBelongSet.add(summary.getBillOffBelong());
      }
      if (!"D".equals(offType)) {
        for (RptAccountSummary summary : bAccountSummaries) {
          billOffBelongSet.add(summary.getBillOffBelong());
        }
        for (RptAccountSummary summary : cAccountSummaries) {
          billOffBelongSet.add(summary.getBillOffBelong());
        }
        for (RptAccountSummary summary : dAccountSummaries) {
          billOffBelongSet.add(summary.getBillOffBelong());
        }
        for (RptAccountSummary summary : eAccountSummaries) {
          billOffBelongSet.add(summary.getBillOffBelong());
        }
        for (RptAccountSummary summary : fAccountSummaries) {
          billOffBelongSet.add(summary.getBillOffBelong());
        }
      }

      // 迭代 bill off belong 清單並填充 row 變數
      for (String billOffBelong : billOffBelongSet) {
        BP221D6_ReportRowForm row = new BP221D6_ReportRowForm();
        row.setOffCode(billOffBelong);
        row.setOffName(getOfficeName(billOffBelong));

        for (RptAccountSummary summary : aAccountSummaries) {
          if (billOffBelong.equals(summary.getBillOffBelong())) {
            row.setFromAccountSummary(offType, "a", summary);
          }
        }
        if (!"D".equals(offType)) {
          for (RptAccountSummary summary : bAccountSummaries) {
            if (billOffBelong.equals(summary.getBillOffBelong())) {
              row.setFromAccountSummary(offType, "b", summary);
            }
          }
          for (RptAccountSummary summary : cAccountSummaries) {
            if (billOffBelong.equals(summary.getBillOffBelong())) {
              row.setFromAccountSummary(offType, "c", summary);
            }
          }
          for (RptAccountSummary summary : dAccountSummaries) {
            if (billOffBelong.equals(summary.getBillOffBelong())) {
              row.setFromAccountSummary(offType, "d", summary);
            }
          }
          for (RptAccountSummary summary : eAccountSummaries) {
            if (billOffBelong.equals(summary.getBillOffBelong())) {
              row.setFromAccountSummary(offType, "e", summary);
            }
          }
          for (RptAccountSummary summary : fAccountSummaries) {
            if (billOffBelong.equals(summary.getBillOffBelong())) {
              row.setFromAccountSummary(offType, "f", summary);
            }
          }
        }

        rows.add(row);
      }

      // 合計
      BP221D6_ReportRowForm sumRow = new BP221D6_ReportRowForm();
      BigDecimal totalANonBadDebt = BigDecimal.ZERO;
      BigDecimal totalABadDebt = BigDecimal.ZERO;
      BigDecimal totalASubtotal = BigDecimal.ZERO;
      BigDecimal totalBNonBadDebt = BigDecimal.ZERO;
      BigDecimal totalBBadDebt = BigDecimal.ZERO;
      BigDecimal totalBSubtotal = BigDecimal.ZERO;
      BigDecimal totalCNonBadDebt = BigDecimal.ZERO;
      BigDecimal totalCBadDebt = BigDecimal.ZERO;
      BigDecimal totalCSubtotal = BigDecimal.ZERO;
      BigDecimal totalDNonBadDebt = BigDecimal.ZERO;
      BigDecimal totalDBadDebt = BigDecimal.ZERO;
      BigDecimal totalDSubtotal = BigDecimal.ZERO;
      BigDecimal totalENonBadDebt = BigDecimal.ZERO;
      BigDecimal totalEBadDebt = BigDecimal.ZERO;
      BigDecimal totalESubtotal = BigDecimal.ZERO;
      BigDecimal totalFNonBadDebt = BigDecimal.ZERO;
      BigDecimal totalFBadDebt = BigDecimal.ZERO;
      BigDecimal totalFSubtotal = BigDecimal.ZERO;

      for (BP221D6_ReportRowForm row : rows) {
        BigDecimal totalAllNonBadDebt = BigDecimal.ZERO;
        BigDecimal totalAllBadDebt = BigDecimal.ZERO;
        if (row.getANonBadDebt() != null) {
          totalANonBadDebt = totalANonBadDebt.add(row.getANonBadDebt());
          totalAllNonBadDebt = totalAllNonBadDebt.add(row.getANonBadDebt());
        }
        if (row.getABadDebt() != null) {
          totalABadDebt = totalABadDebt.add(row.getABadDebt());
          totalAllBadDebt = totalAllBadDebt.add(row.getABadDebt());
        }
        if (row.getASubtotal() != null) {
          totalASubtotal = totalASubtotal.add(row.getASubtotal());
        }
        if (!"D".equals(offType)) {
          if (row.getBNonBadDebt() != null) {
            totalBNonBadDebt = totalBNonBadDebt.add(row.getBNonBadDebt());
            totalAllNonBadDebt = totalAllNonBadDebt.add(row.getBNonBadDebt());
          }
          if (row.getBBadDebt() != null) {
            totalBBadDebt = totalBBadDebt.add(row.getBBadDebt());
            totalAllBadDebt = totalAllBadDebt.add(row.getBBadDebt());
          }
          if (row.getBSubtotal() != null) {
            totalBSubtotal = totalBSubtotal.add(row.getBSubtotal());
          }
          if (row.getCNonBadDebt() != null) {
            totalCNonBadDebt = totalCNonBadDebt.add(row.getCNonBadDebt());
            totalAllNonBadDebt = totalAllNonBadDebt.add(row.getCNonBadDebt());
          }
          if (row.getCBadDebt() != null) {
            totalCBadDebt = totalCBadDebt.add(row.getCBadDebt());
            totalAllBadDebt = totalAllBadDebt.add(row.getCBadDebt());
          }
          if (row.getCSubtotal() != null) {
            totalCSubtotal = totalCSubtotal.add(row.getCSubtotal());
          }
          if (row.getDNonBadDebt() != null) {
            totalDNonBadDebt = totalDNonBadDebt.add(row.getDNonBadDebt());
            totalAllNonBadDebt = totalAllNonBadDebt.add(row.getDNonBadDebt());
          }
          if (row.getDBadDebt() != null) {
            totalDBadDebt = totalDBadDebt.add(row.getDBadDebt());
            totalAllBadDebt = totalAllBadDebt.add(row.getDBadDebt());
          }
          if (row.getDSubtotal() != null) {
            totalDSubtotal = totalDSubtotal.add(row.getDSubtotal());
          }
          if (row.getENonBadDebt() != null) {
            totalENonBadDebt = totalENonBadDebt.add(row.getENonBadDebt());
            totalAllNonBadDebt = totalAllNonBadDebt.add(row.getENonBadDebt());
          }
          if (row.getEBadDebt() != null) {
            totalEBadDebt = totalEBadDebt.add(row.getEBadDebt());
            totalAllBadDebt = totalAllBadDebt.add(row.getEBadDebt());
          }
          if (row.getESubtotal() != null) {
            totalESubtotal = totalESubtotal.add(row.getESubtotal());
          }
          if (row.getFNonBadDebt() != null) {
            totalFNonBadDebt = totalFNonBadDebt.add(row.getFNonBadDebt());
            totalAllNonBadDebt = totalAllNonBadDebt.add(row.getFNonBadDebt());
          }
          if (row.getFBadDebt() != null) {
            totalFBadDebt = totalFBadDebt.add(row.getFBadDebt());
            totalAllBadDebt = totalAllBadDebt.add(row.getFBadDebt());
          }
          if (row.getFSubtotal() != null) {
            totalFSubtotal = totalFSubtotal.add(row.getFSubtotal());
          }
        }

        row.setTotalNonBadDebt(totalAllNonBadDebt);
        row.setTotalBadDebt(totalAllBadDebt);
        row.setTotalSubtotal(totalAllNonBadDebt.add(totalAllBadDebt));
      }

      sumRow.setOffName("總計");
      sumRow.setANonBadDebt(totalANonBadDebt);
      sumRow.setABadDebt(totalABadDebt);
      sumRow.setASubtotal(totalASubtotal);
      if (!"D".equals(offType)) {
        sumRow.setBNonBadDebt(totalBNonBadDebt);
        sumRow.setBBadDebt(totalBBadDebt);
        sumRow.setBSubtotal(totalBSubtotal);
        sumRow.setCNonBadDebt(totalCNonBadDebt);
        sumRow.setCBadDebt(totalCBadDebt);
        sumRow.setCSubtotal(totalCSubtotal);
        sumRow.setDNonBadDebt(totalDNonBadDebt);
        sumRow.setDBadDebt(totalDBadDebt);
        sumRow.setDSubtotal(totalDSubtotal);
        sumRow.setENonBadDebt(totalENonBadDebt);
        sumRow.setEBadDebt(totalEBadDebt);
        sumRow.setESubtotal(totalESubtotal);
        sumRow.setFNonBadDebt(totalFNonBadDebt);
        sumRow.setFBadDebt(totalFBadDebt);
        sumRow.setFSubtotal(totalFSubtotal);
      }

      sumRow.setTotalNonBadDebt(sumRow.getANonBadDebt()
                                    .add(sumRow.getBNonBadDebt())
                                    .add(sumRow.getCNonBadDebt())
                                    .add(sumRow.getDNonBadDebt())
                                    .add(sumRow.getENonBadDebt())
                                    .add(sumRow.getFNonBadDebt()));
      sumRow.setTotalBadDebt(sumRow.getABadDebt()
                                 .add(sumRow.getBBadDebt())
                                 .add(sumRow.getCBadDebt())
                                 .add(sumRow.getDBadDebt())
                                 .add(sumRow.getEBadDebt())
                                 .add(sumRow.getFBadDebt()));
      sumRow.setTotalSubtotal(sumRow.getTotalNonBadDebt().add(sumRow.getTotalBadDebt()));

      rows.add(sumRow);

      String titleName;
      if (offType == "A") {
        // BP221D6_T{OPC_YYYMM}.CSV
        csvFileName = "BP221D6_T" + opcDate + ".csv";
        titleName = "營運處年度欠費統計表";
      } else if (offType == "B") {
        // BP221D6_GSM_T{OPC_YYYMM}.CSV
        csvFileName = "BP221D6_GSM_T" + opcDate + ".csv";
        titleName = "行動業務營運處年度欠費統計表";
      } else if (offType == "C") {
        // BP221D6_ORG_T{OPC_YYYMM}.CSV
        csvFileName = "BP221D6_ORG_T" + opcDate + ".csv";
        titleName = "排除特定代收業務年度欠費統計表";
      } else if (offType == "D") {
        // BP221D6(X-2)_T{OPC_DATE}.CSV
        csvFileName = "BP221D6(X-2)_T" + opcDate + ".csv";
        titleName = "營運處逾期一月欠費統計表";
      } else {
        throw new IllegalArgumentException("Invalid offType: " + offType);
      }

      String csvFileAbsolutePath = csvFilePath + csvFileName;

      try (BufferedWriter writer = new BufferedWriter(
               new OutputStreamWriter(new FileOutputStream(csvFileAbsolutePath), "UTF-8"))) {
        writer.write('\uFEFF'); // BOM for UTF-8

        // 將執行日的字串（格式 YYYYMMDD） 的 opcDate 變數，切成六種
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate executionDate = LocalDate.parse(opcYYYMM + "01", dateFormatter);

        // 統計至執行日的前2個月(含該月)
        LocalDate twoMonthsAgo = executionDate.minusMonths(2);
        String twoMonthsAgoYear = String.valueOf(twoMonthsAgo.getYear() - 1911);
        String twoMonthsAgoMonth = String.format("%02d", twoMonthsAgo.getMonthValue());

        // 統計至執行日的前1個月(含該月)
        LocalDate oneMonthAgo = executionDate.minusMonths(1);
        String oneMonthAgoYear = String.valueOf(oneMonthAgo.getYear() - 1911);
        String oneMonthAgoMonth = String.format("%02d", oneMonthAgo.getMonthValue());

        // 單獨統計執行日的前1年該年度
        LocalDate oneYearAgo = executionDate.minusYears(1);
        String oneYearAgoYear = String.valueOf(oneYearAgo.getYear() - 1911);

        // 統計至執行日的前2年(含該年度)
        LocalDate twoYearsAgo = executionDate.minusYears(2);
        String twoYearsAgoYear = String.valueOf(twoYearsAgo.getYear() - 1911);

        // 單獨統計執行日的前1年度的十一月
        LocalDate lastYearNovember = LocalDate.of(oneYearAgo.getYear(), 11, 1);
        String lastYearNovemberYear = String.valueOf(lastYearNovember.getYear() - 1911);
        String lastYearNovemberMonth = String.format("%02d", lastYearNovember.getMonthValue());

        // 單獨統計執行日的前1年度的十二月
        LocalDate lastYearDecember = LocalDate.of(oneYearAgo.getYear(), 12, 1);
        String lastYearDecemberYear = String.valueOf(lastYearDecember.getYear() - 1911);
        String lastYearDecemberMonth = String.format("%02d", lastYearDecember.getMonthValue());

        String strLine = "------------------------------";
        // Write header
        if (!"D".equals(offType)) {
          String header1 = ",《" + titleName + "》,,,處理日期 " + rocDate + ",,,,,,,,,,,,,,,,\n";
          writer.write(header1);
          String header2 = "," + twoMonthsAgoYear + "年度" + twoMonthsAgoMonth + "月（含以前）,,,"
              + oneMonthAgoYear + "年度" + oneMonthAgoMonth + "月（含以前）,,," + oneYearAgoYear
              + "年度,,," + twoYearsAgoYear + "年度（含以前）,,," + lastYearNovemberYear + "年度"
              + lastYearNovemberMonth + "月,,," + lastYearDecemberYear + "年度"
              + lastYearDecemberMonth + "月,,,"
              + "合計"
              + ",\n";
          writer.write(header2);
          StringBuilder header3Builder = new StringBuilder(",");
          for (int i = 0; i < 21; i++) {
            header3Builder.append(strLine);
            if (i < 20) {
              header3Builder.append(",");
            }
          }
          header3Builder.append("\n");
          writer.write(header3Builder.toString());
          // 使用 for 迴圈生成 header4
          StringBuilder header4Builder = new StringBuilder(",");
          for (int i = 0; i < 7; i++) {
            header4Builder.append("非呆帳,呆帳,小計");
            if (i < 6) {
              header4Builder.append(",");
            }
          }
          header4Builder.append("\n");
          writer.write(header4Builder.toString());
        } else {
          String header1 = ",《" + titleName + "》,,處理日期 " + rocDate + "\n";
          writer.write(header1);
          String header2 = "," + twoMonthsAgoYear + "年度" + twoMonthsAgoMonth + "月,,\n";
          writer.write(header2);

          String header3 = "," + strLine + "," + strLine + "," + strLine + "\n";
          writer.write(header3);
          String header4 = ",非呆帳,呆帳,小計\n";
          writer.write(header4);
        }

        // Write data rows
        for (BP221D6_ReportRowForm row : rows) {
          if (row.equals(rows.get(rows.size() - 1))) {
            if (!"D".equals(offType)) {
              StringBuilder header5Builder = new StringBuilder();
              for (int i = 0; i < 22; i++) {
                header5Builder.append(strLine);
                if (i < 21) {
                  header5Builder.append(",");
                }
              }
              header5Builder.append("\n");
              writer.write(header5Builder.toString());
            } else {
              String header5 = strLine + "," + strLine + "," + strLine + "," + strLine + "\n";
              writer.write(header5);
            }
          }
          String rowData;
          if (!"D".equals(offType)) {
            rowData = "\"" + row.getOffName() + "\","
                + "\""
                + (row.getANonBadDebt() != null
                        ? String.format("%,d", row.getANonBadDebt().longValue())
                        : "0")
                + "\","
                + "\""
                + (row.getABadDebt() != null ? String.format("%,d", row.getABadDebt().longValue())
                                             : "0")
                + "\","
                + "\""
                + (row.getASubtotal() != null ? String.format("%,d", row.getASubtotal().longValue())
                                              : "0")
                + "\","
                + "\""
                + (row.getBNonBadDebt() != null
                        ? String.format("%,d", row.getBNonBadDebt().longValue())
                        : "0")
                + "\","
                + "\""
                + (row.getBBadDebt() != null ? String.format("%,d", row.getBBadDebt().longValue())
                                             : "0")
                + "\","
                + "\""
                + (row.getBSubtotal() != null ? String.format("%,d", row.getBSubtotal().longValue())
                                              : "0")
                + "\","
                + "\""
                + (row.getCNonBadDebt() != null
                        ? String.format("%,d", row.getCNonBadDebt().longValue())
                        : "0")
                + "\","
                + "\""
                + (row.getCBadDebt() != null ? String.format("%,d", row.getCBadDebt().longValue())
                                             : "0")
                + "\","
                + "\""
                + (row.getCSubtotal() != null ? String.format("%,d", row.getCSubtotal().longValue())
                                              : "0")
                + "\","
                + "\""
                + (row.getDNonBadDebt() != null
                        ? String.format("%,d", row.getDNonBadDebt().longValue())
                        : "0")
                + "\","
                + "\""
                + (row.getDBadDebt() != null ? String.format("%,d", row.getDBadDebt().longValue())
                                             : "0")
                + "\","
                + "\""
                + (row.getDSubtotal() != null ? String.format("%,d", row.getDSubtotal().longValue())
                                              : "0")
                + "\","
                + "\""
                + (row.getENonBadDebt() != null
                        ? String.format("%,d", row.getENonBadDebt().longValue())
                        : "0")
                + "\","
                + "\""
                + (row.getEBadDebt() != null ? String.format("%,d", row.getEBadDebt().longValue())
                                             : "0")
                + "\","
                + "\""
                + (row.getESubtotal() != null ? String.format("%,d", row.getESubtotal().longValue())
                                              : "0")
                + "\","
                + "\""
                + (row.getFNonBadDebt() != null
                        ? String.format("%,d", row.getFNonBadDebt().longValue())
                        : "0")
                + "\","
                + "\""
                + (row.getFBadDebt() != null ? String.format("%,d", row.getFBadDebt().longValue())
                                             : "0")
                + "\","
                + "\""
                + (row.getFSubtotal() != null ? String.format("%,d", row.getFSubtotal().longValue())
                                              : "0")
                + "\","
                + "\""
                + (row.getTotalNonBadDebt() != null
                        ? String.format("%,d", row.getTotalNonBadDebt().longValue())
                        : "0")
                + "\","
                + "\""
                + (row.getTotalBadDebt() != null
                        ? String.format("%,d", row.getTotalBadDebt().longValue())
                        : "0")
                + "\","
                + "\""
                + (row.getTotalSubtotal() != null
                        ? String.format("%,d", row.getTotalSubtotal().longValue())
                        : "0")
                + "\"\n";
          } else {
            rowData = "\"" + row.getOffName() + "\","
                + "\""
                + (row.getANonBadDebt() != null
                        ? String.format("%,d", row.getANonBadDebt().longValue())
                        : "0")
                + "\","
                + "\""
                + (row.getABadDebt() != null ? String.format("%,d", row.getABadDebt().longValue())
                                             : "0")
                + "\","
                + "\""
                + (row.getASubtotal() != null ? String.format("%,d", row.getASubtotal().longValue())
                                              : "0")
                + "\"\n";
          }
          writer.write(rowData);
        }

        writer.flush();
        createCount++;
        dDataList.add(dData.builder()
                          .rptFileName(csvFileName)
                          .billOff("2")
                          .rptTimes("3")
                          .billMonth(rocYYYMM)
                          .rptDate(opcDate)
                          .rptFileCount(rows.size() - 1)
                          .rptSecretMark("N")
                          .build());
      } catch (IOException e) {
        errorCount++;
        log.error("Error writing CSV file: " + csvFileAbsolutePath, e);
        errorMessage = e.getMessage();
      }

      outputFiles.add(csvFileAbsolutePath);
      log.debug("getAccountSummary end, result size: " + rows.size());
    }
    return Result.builder()
        .rptCode(rptCode)
        .isRerun(isRerun)
        .opBatchno(jobId)
        .dDataList(dDataList)
        .build();
  }

  private String getOfficeName(String billOffBelong) {
    OfficeInfoQueryIn officeInfoQueryIn = new OfficeInfoQueryIn();
    officeInfoQueryIn.setOfficeCode(billOffBelong);
    officeInfoQueryIn.setTransType("A");
    OfficeInfoQueryOut officeInfoQueryOut = comm01Service.COMM01_0003(officeInfoQueryIn);
    return officeInfoQueryOut.getResultOfficeCN() != null
        ? officeInfoQueryOut.getResultOfficeCN().trim()
        : "";
  }

  @Override
  @RptLogExecution(rptCode = "BP2240D1")
  public Result batchBP2240D1Rpt(BatchSimpleRptInStr input) throws Exception {
    String rptCode = "BP2240D1";
    String jobId = input.getJobId();
    String opcDate = input.getOpcDate();
    String rocDate = DateUtils.convertToRocDate(opcDate);
    String opcYYYMM = input.getOpcYearMonth();
    String rocYYYMM = DateUtils.convertToRocYearMonth(opcYYYMM);
    String isRerun = input.getIsRerun();

    List<RptBP2240D1Summary> rptBP2240D1Summaries =
        rptAccountSummaryMapper.selectBP2240D1Summary(opcYYYMM);

    Map<String, Map<String, RptBP2240D1Summary>> groupedSummaries = new HashMap<>();

    for (RptBP2240D1Summary summary : rptBP2240D1Summaries) {
      String buGroupMark = summary.getBuGroupMark();
      String billOffBelong = summary.getBillOffBelong();

      groupedSummaries.computeIfAbsent(buGroupMark, k -> new HashMap<>())
          .put(billOffBelong, summary);
    }

    int nonNullBillOffCount = 0;
    Map<String, RptBP2240D1Summary> billOffMap = new HashMap<>();
    String csvFileName = "BP2240D1_T" + opcDate + ".csv";
    String csvFileAbsolutePath = csvFilePath + csvFileName;
    // 建立 CsvGenerator 物件
    CsvGenerator csvGenerator = new CsvGenerator(csvFileAbsolutePath, 12, ",");

    for (Map.Entry<String, Map<String, RptBP2240D1Summary>> buGroupEntry :
        groupedSummaries.entrySet()) {
      String buGroupMark = buGroupEntry.getKey();
      String buGroupName = NameMapping.getBusinessGroupName(buGroupMark);

      List<String> header01 = new ArrayList<>();
      header01.add(buGroupName);
      header01.add("");
      header01.add("MOD 催收轉列營收統計表");
      header01.add("");
      header01.add("報表日期：" + opcDate);
      csvGenerator.writeData(0, header01);

      List<String> header02 = new ArrayList<>();
      header02.add(buGroupName);
      csvGenerator.writeData(0, header02);

      List<String> header03 = new ArrayList<>();
      header03.add(buGroupName);
      header03.add("");
      header03.add("\"2825.2402\"");
      header03.add("\"2825.2403\"");
      header03.add("\"2825.2404\"");
      header03.add("\"2825.2405\"");
      header03.add("\"2825.2406\"");
      header03.add("\"1816.2401\"");
      header03.add("\"1816.2402A\"");
      header03.add("\"2253.02EA\"");
      header03.add("\"2253.02EC\"");
      header03.add("\"1178.29N/1178.29S\"");
      csvGenerator.writeData(0, header03);

      List<String> header04 = new ArrayList<>();
      header04.add(buGroupName);
      for (int i = 0; i < 11; i++) {
        header04.add("===============");
      }
      csvGenerator.writeData(0, header04);

      BigDecimal sumACol = BigDecimal.ZERO;
      BigDecimal sumBCol = BigDecimal.ZERO;
      BigDecimal sumCCol = BigDecimal.ZERO;
      BigDecimal sumDCol = BigDecimal.ZERO;
      BigDecimal sumECol = BigDecimal.ZERO;
      BigDecimal sumFCol = BigDecimal.ZERO;
      BigDecimal sumGCol = BigDecimal.ZERO;
      BigDecimal sumHCol = BigDecimal.ZERO;
      BigDecimal sumICol = BigDecimal.ZERO;
      BigDecimal sumJCol = BigDecimal.ZERO;
      billOffMap = buGroupEntry.getValue();

      for (Map.Entry<String, RptBP2240D1Summary> billOffEntry : billOffMap.entrySet()) {
        String billOffBelong = billOffEntry.getKey();
        if (billOffBelong != null) {
          nonNullBillOffCount++; // Increment the count for non-null billOffBelong
          RptBP2240D1Summary summary = billOffEntry.getValue();

          List<String> body = new ArrayList<>();
          body.add(buGroupName);
          body.add(getOfficeName(billOffBelong));
          body.add(StringUtils.formatNumberWithCommas(summary.getACol()));
          body.add(StringUtils.formatNumberWithCommas(summary.getBCol()));
          body.add(StringUtils.formatNumberWithCommas(summary.getCCol()));
          body.add(StringUtils.formatNumberWithCommas(summary.getDCol()));
          body.add(StringUtils.formatNumberWithCommas(summary.getECol()));
          body.add(StringUtils.formatNumberWithCommas(summary.getFCol()));
          body.add(StringUtils.formatNumberWithCommas(summary.getGCol()));
          body.add(StringUtils.formatNumberWithCommas(summary.getHCol()));
          body.add(StringUtils.formatNumberWithCommas(summary.getICol()));
          body.add(StringUtils.formatNumberWithCommas(summary.getJCol()));
          csvGenerator.writeData(0, body);

          sumACol = sumACol.add(summary.getACol());
          sumBCol = sumBCol.add(summary.getBCol());
          sumCCol = sumCCol.add(summary.getCCol());
          sumDCol = sumDCol.add(summary.getDCol());
          sumECol = sumECol.add(summary.getECol());
          sumFCol = sumFCol.add(summary.getFCol());
          sumGCol = sumGCol.add(summary.getGCol());
          sumHCol = sumHCol.add(summary.getHCol());
          sumICol = sumICol.add(summary.getICol());
          sumJCol = sumJCol.add(summary.getJCol());
        }
      }

      List<String> footer = new ArrayList<>();
      footer.add(buGroupName);
      footer.add("總計");
      footer.add(StringUtils.formatNumberWithCommas(sumACol));
      footer.add(StringUtils.formatNumberWithCommas(sumBCol));
      footer.add(StringUtils.formatNumberWithCommas(sumCCol));
      footer.add(StringUtils.formatNumberWithCommas(sumDCol));
      footer.add(StringUtils.formatNumberWithCommas(sumECol));
      footer.add(StringUtils.formatNumberWithCommas(sumFCol));
      footer.add(StringUtils.formatNumberWithCommas(sumGCol));
      footer.add(StringUtils.formatNumberWithCommas(sumHCol));
      footer.add(StringUtils.formatNumberWithCommas(sumICol));
      footer.add(StringUtils.formatNumberWithCommas(sumJCol));
      csvGenerator.writeData(0, footer);
      csvGenerator.writeData(0, new ArrayList<>());
    }

    // 儲存 CSV 檔案
    csvGenerator.save();

    return Result.builder()
        .rptCode(rptCode)
        .isRerun(isRerun)
        .opBatchno(jobId)
        .rptFileName(csvFileName)
        .billMonth(rocYYYMM)
        .rptDate(opcDate)
        .rptFileCount(nonNullBillOffCount)
        .build();
  }

  @Override
  @RptLogExecution(rptCode = "BP2230D4")
  public Result batchBP2230D4D5Rpt(BatchSimpleRptInStr input) throws Exception {
    String rptCode = "BP2230D4";
    String isRerun = input.getIsRerun();
    String jobId = input.getJobId();
    String opcDate = input.getOpcDate();
    String rocDate = DateUtils.convertToRocDate(opcDate);
    String opcYYYMM = input.getOpcYearMonth();
    String rocYYYMM = DateUtils.convertToRocYearMonth(opcYYYMM);
    Integer createCount = 0;
    Integer errorCount = 0;
    List<dData> dDataList = new ArrayList<>();

    String[] reportMethods = {"batchBP2230D4Rpt", "batchBP2230D5Rpt"};
    for (String method : reportMethods) {
      try {
        dData outputDData = (dData) this.getClass()
                                .getMethod(method, BatchSimpleRptInStr.class)
                                .invoke(this, input);
        dDataList.add(outputDData);
        createCount++;
      } catch (Exception e) {
        log.error("Error in " + method + ": " + e.getMessage(), e);
        errorCount++;
      }
    }

    return Result.builder()
        .rptCode(rptCode)
        .isRerun(isRerun)
        .opBatchno(jobId)
        .dDataList(dDataList)
        .build();
  }

  @Override
  public dData batchBP2230D4Rpt(BatchSimpleRptInStr input) throws Exception {
    String rptCode = "BP2230D4";
    String jobId = input.getJobId();
    String opcDate = input.getOpcDate();
    String rocDate = DateUtils.convertToRocDate(opcDate);
    String opcYYYMM = input.getOpcYearMonth();
    String rocYYYMM = DateUtils.convertToRocYearMonth(opcYYYMM);
    String isRerun = input.getIsRerun();

    List<RptBP2230D4Summary> rptBP2230D4Summaries =
        rptAccountSummaryMapper.selectBP2230D4Summary(opcYYYMM);

    String csvFileName = "BP2230D4_T" + opcDate + ".csv";
    String csvFileAbsolutePath = csvFilePath + csvFileName;
    // 建立 CsvGenerator 物件
    CsvGenerator csvGenerator = new CsvGenerator(csvFileAbsolutePath, 32, ",");

    String rocYYY = rocYYYMM.substring(0, 3);
    String rocMM = rocYYYMM.substring(3, 5);
    List<String> header01 = new ArrayList<>();
    String headerString = String.format(
        "%s 年 %s 月  欠費依會計科目(含呆帳)統計表       製表日期 %s", rocYYY, rocMM, rocDate);
    header01.add(headerString);
    csvGenerator.writeData(0, header01);
    List<String> header02 = new ArrayList<>();
    csvGenerator.writeData(0, header02);
    List<String> header03 = new ArrayList<>();
    header03.add("BP2230D4");
    header03.add("");
    header03.add("年度");
    header03.add("");
    header03.add(String.format("%s 年度 %02d 月（含）以後", rocYYY, Integer.parseInt(rocMM) + 1));
    header03.add("");
    header03.add(String.format("%s 年度 %s 月", rocYYY, rocMM));
    header03.add("");
    for (int year = 1; year <= 11; year++) {
      header03.add(String.format("%03d 年度", Integer.parseInt(rocYYY) - year));
      header03.add("");
    }
    header03.add("總計");
    header03.add("");
    csvGenerator.writeData(0, header03);
    List<String> header04 = new ArrayList<>();
    header04.add("應收帳款");
    header04.add("加");
    header04.add("催收款項");
    header04.add("");
    for (int i = 0; i < 14; i++) {
      header04.add("合計欠費╱非呆帳");
      header04.add("呆帳 1813");
    }
    csvGenerator.writeData(0, header04);

    BigDecimal[] totals = new BigDecimal[28]; // 4 + 22 (11 years * 2) + 2
    for (int i = 0; i < totals.length; i++) {
      totals[i] = BigDecimal.ZERO;
    }

    for (RptBP2230D4Summary summary : rptBP2230D4Summaries) {
      List<String> dataRow = new ArrayList<>();
      String accItem = summary.getAccItem();

      if ("OTHER".equals(accItem)) {
        dataRow.add("1144-11N/1144-11S");
        dataRow.add("");
        dataRow.add("");
        dataRow.add("1144-11N/1144-11S");
      } else if (accItem == null) {
        dataRow.add(summary.getOvdItem());
        dataRow.add("");
        dataRow.add("");
        dataRow.add(summary.getAccName());
      } else {
        dataRow.add(accItem);
        if (summary.getOvdItem() != null && !summary.getOvdItem().trim().isEmpty()) {
          dataRow.add("+");
          dataRow.add(summary.getOvdItem());
        } else {
          dataRow.add("");
          dataRow.add("");
        }
        dataRow.add(summary.getAccName());
      }

      BigDecimal futureMonthsNonBadDebt = summary.getFutureMonthsNonBadDebt() != null
          ? summary.getFutureMonthsNonBadDebt()
          : BigDecimal.ZERO;
      BigDecimal futureMonthsBadDebt = summary.getFutureMonthsBadDebt() != null
          ? summary.getFutureMonthsBadDebt()
          : BigDecimal.ZERO;
      BigDecimal currentMonthNonBadDebt = summary.getCurrentMonthNonBadDebt() != null
          ? summary.getCurrentMonthNonBadDebt()
          : BigDecimal.ZERO;
      BigDecimal currentMonthBadDebt = summary.getCurrentMonthBadDebt() != null
          ? summary.getCurrentMonthBadDebt()
          : BigDecimal.ZERO;
      BigDecimal totalNonBadDebt =
          summary.getTotalNonBadDebt() != null ? summary.getTotalNonBadDebt() : BigDecimal.ZERO;
      BigDecimal totalBadDebt =
          summary.getTotalBadDebt() != null ? summary.getTotalBadDebt() : BigDecimal.ZERO;

      dataRow.add(StringUtils.formatNumberWithCommas(futureMonthsNonBadDebt));
      dataRow.add(StringUtils.formatNumberWithCommas(futureMonthsBadDebt));
      dataRow.add(StringUtils.formatNumberWithCommas(currentMonthNonBadDebt));
      dataRow.add(StringUtils.formatNumberWithCommas(currentMonthBadDebt));

      totals[0] = totals[0].add(futureMonthsNonBadDebt);
      totals[1] = totals[1].add(futureMonthsBadDebt);
      totals[2] = totals[2].add(currentMonthNonBadDebt);
      totals[3] = totals[3].add(currentMonthBadDebt);

      for (int year = 1; year <= 11; year++) {
        BigDecimal yearNonBadDebt = (BigDecimal) summary.getClass()
                                        .getMethod("getYear" + year + "NonBadDebt")
                                        .invoke(summary);
        BigDecimal yearBadDebt =
            (BigDecimal) summary.getClass().getMethod("getYear" + year + "BadDebt").invoke(summary);

        if (yearNonBadDebt == null) {
          yearNonBadDebt = BigDecimal.ZERO;
        }
        if (yearBadDebt == null) {
          yearBadDebt = BigDecimal.ZERO;
        }

        dataRow.add(StringUtils.formatNumberWithCommas(yearNonBadDebt));
        dataRow.add(StringUtils.formatNumberWithCommas(yearBadDebt));

        totals[4 + (year - 1) * 2] = totals[4 + (year - 1) * 2].add(yearNonBadDebt);
        totals[5 + (year - 1) * 2] = totals[5 + (year - 1) * 2].add(yearBadDebt);
      }

      dataRow.add(StringUtils.formatNumberWithCommas(totalNonBadDebt));
      dataRow.add(StringUtils.formatNumberWithCommas(totalBadDebt));

      totals[26] = totals[26].add(totalNonBadDebt);
      totals[27] = totals[27].add(totalBadDebt);

      csvGenerator.writeData(0, dataRow);
    }

    List<String> formattedTotals = new ArrayList<>();
    for (BigDecimal total : totals) {
      formattedTotals.add(StringUtils.formatNumberWithCommas(total));
    }

    List<String> footer = new ArrayList<>();
    footer.add("");
    footer.add("");
    footer.add("總計");
    footer.add("");
    footer.addAll(formattedTotals);
    csvGenerator.writeData(0, footer);

    // 儲存 CSV 檔案
    csvGenerator.save();

    return dData.builder()
        .rptFileName(csvFileName)
        .billOff("2")
        .rptTimes("3")
        .billMonth(rocYYYMM)
        .rptDate(opcDate)
        .rptFileCount(rptBP2230D4Summaries.size())
        .rptSecretMark("N")
        .build();
  }

  @Override
  public dData batchBP2230D5Rpt(BatchSimpleRptInStr input) throws Exception {
    String rptCode = "BP2230D4";
    String jobId = input.getJobId();
    String opcDate = input.getOpcDate();
    String rocDate = DateUtils.convertToRocDate(opcDate);
    String opcYYYMM = input.getOpcYearMonth();
    String rocYYYMM = DateUtils.convertToRocYearMonth(opcYYYMM);
    String isRerun = input.getIsRerun();

    List<RptBP2230D5Summary> rptBP2230D5Summaries =
        rptAccountSummaryMapper.selectBP2230D5Summary(opcYYYMM);

    String csvFileName = "BP2230D5_T" + opcDate + ".csv";
    String csvFileAbsolutePath = csvFilePath + csvFileName;
    // 建立 CsvGenerator 物件
    CsvGenerator csvGenerator = new CsvGenerator(csvFileAbsolutePath, 29, ",");

    String rocYYY = rocYYYMM.substring(0, 3);
    String rocMM = rocYYYMM.substring(3, 5);
    List<String> header01 = new ArrayList<>();
    String headerString = String.format(
        "%s 年 %s 月  欠費依營運處(含呆帳)統計表       製表日期 %s", rocYYY, rocMM, rocDate);
    header01.add(headerString);
    csvGenerator.writeData(0, header01);
    List<String> header02 = new ArrayList<>();
    csvGenerator.writeData(0, header02);
    List<String> header03 = new ArrayList<>();
    header03.add("BP2230D5  年度");
    header03.add(String.format("%s 年度 %02d 月（含）以後", rocYYY, Integer.parseInt(rocMM) + 1));
    header03.add("");
    header03.add(String.format("%s 年度 %s 月", rocYYY, rocMM));
    header03.add("");
    for (int year = 1; year <= 11; year++) {
      header03.add(String.format("%03d 年度", Integer.parseInt(rocYYY) - year));
      header03.add("");
    }
    header03.add("總計");
    header03.add("");
    csvGenerator.writeData(0, header03);
    List<String> header04 = new ArrayList<>();
    header04.add("營運處");
    for (int i = 0; i < 14; i++) {
      header04.add("合計欠費╱非呆帳");
      header04.add("呆帳 1813");
    }
    csvGenerator.writeData(0, header04);

    BigDecimal[] totals = new BigDecimal[28]; // 4 + 22 (11 years * 2) + 2
    for (int i = 0; i < totals.length; i++) {
      totals[i] = BigDecimal.ZERO;
    }

    for (RptBP2230D5Summary summary : rptBP2230D5Summaries) {
      List<String> dataRow = new ArrayList<>();
      dataRow.add(getOfficeName(summary.getBillOffBelong()));

      BigDecimal futureMonthsNonBadDebt = summary.getFutureMonthsNonBadDebt() != null
          ? summary.getFutureMonthsNonBadDebt()
          : BigDecimal.ZERO;
      BigDecimal futureMonthsBadDebt = summary.getFutureMonthsBadDebt() != null
          ? summary.getFutureMonthsBadDebt()
          : BigDecimal.ZERO;
      BigDecimal currentMonthNonBadDebt = summary.getCurrentMonthNonBadDebt() != null
          ? summary.getCurrentMonthNonBadDebt()
          : BigDecimal.ZERO;
      BigDecimal currentMonthBadDebt = summary.getCurrentMonthBadDebt() != null
          ? summary.getCurrentMonthBadDebt()
          : BigDecimal.ZERO;
      BigDecimal totalNonBadDebt =
          summary.getTotalNonBadDebt() != null ? summary.getTotalNonBadDebt() : BigDecimal.ZERO;
      BigDecimal totalBadDebt =
          summary.getTotalBadDebt() != null ? summary.getTotalBadDebt() : BigDecimal.ZERO;

      dataRow.add(StringUtils.formatNumberWithCommas(futureMonthsNonBadDebt));
      dataRow.add(StringUtils.formatNumberWithCommas(futureMonthsBadDebt));
      dataRow.add(StringUtils.formatNumberWithCommas(currentMonthNonBadDebt));
      dataRow.add(StringUtils.formatNumberWithCommas(currentMonthBadDebt));

      totals[0] = totals[0].add(futureMonthsNonBadDebt);
      totals[1] = totals[1].add(futureMonthsBadDebt);
      totals[2] = totals[2].add(currentMonthNonBadDebt);
      totals[3] = totals[3].add(currentMonthBadDebt);

      for (int year = 1; year <= 11; year++) {
        BigDecimal yearNonBadDebt = (BigDecimal) summary.getClass()
                                        .getMethod("getYear" + year + "NonBadDebt")
                                        .invoke(summary);
        BigDecimal yearBadDebt =
            (BigDecimal) summary.getClass().getMethod("getYear" + year + "BadDebt").invoke(summary);

        if (yearNonBadDebt == null) {
          yearNonBadDebt = BigDecimal.ZERO;
        }
        if (yearBadDebt == null) {
          yearBadDebt = BigDecimal.ZERO;
        }

        dataRow.add(StringUtils.formatNumberWithCommas(yearNonBadDebt));
        dataRow.add(StringUtils.formatNumberWithCommas(yearBadDebt));

        totals[4 + (year - 1) * 2] = totals[4 + (year - 1) * 2].add(yearNonBadDebt);
        totals[5 + (year - 1) * 2] = totals[5 + (year - 1) * 2].add(yearBadDebt);
      }

      dataRow.add(StringUtils.formatNumberWithCommas(totalNonBadDebt));
      dataRow.add(StringUtils.formatNumberWithCommas(totalBadDebt));

      totals[26] = totals[26].add(totalNonBadDebt);
      totals[27] = totals[27].add(totalBadDebt);

      csvGenerator.writeData(0, dataRow);
    }

    List<String> formattedTotals = new ArrayList<>();
    for (BigDecimal total : totals) {
      formattedTotals.add(StringUtils.formatNumberWithCommas(total));
    }

    List<String> footer = new ArrayList<>();
    footer.add("總計");
    footer.addAll(formattedTotals);
    csvGenerator.writeData(0, footer);

    // 儲存 CSV 檔案
    csvGenerator.save();

    return dData.builder()
        .rptFileName(csvFileName)
        .billOff("2")
        .rptTimes("3")
        .billMonth(rocYYYMM)
        .rptDate(opcDate)
        .rptFileCount(rptBP2230D5Summaries.size())
        .rptSecretMark("N")
        .build();
  }

  @Override
  @RptLogExecution(rptCode = "BP2230D6")
  public Result batchBP2230D6Rpt(BatchSimpleRptInStr input) throws Exception {
    String rptCode = "BP2230D6";
    String jobId = input.getJobId();
    String opcDate = input.getOpcDate();
    String rocDate = DateUtils.convertToRocDate(opcDate);
    String opcYYYMM = input.getOpcYearMonth();
    String rocYYYMM = DateUtils.convertToRocYearMonth(opcYYYMM);
    String isRerun = input.getIsRerun();
    List<dData> dDataList = new ArrayList<>();

    String csvFileName = "BP2230D6_T" + opcDate + ".csv";
    String csvFileAbsolutePath = csvFilePath + csvFileName;
    // 建立 CsvGenerator 物件
    CsvGenerator csvGenerator = new CsvGenerator(csvFileAbsolutePath, 17, ",");

    String rocYYY = rocYYYMM.substring(0, 3);
    String rocMM = rocYYYMM.substring(3, 5);
    List<String> header01 = new ArrayList<>();
    String headerString =
        String.format("%s 年 %s 月  中華電信欠費依會計科目【非呆帳】統計表       製表日期 %s",
            rocYYY, rocMM, rocDate);
    header01.add(headerString);
    csvGenerator.writeData(0, header01);
    List<RptBP2230D6Summary> rptBP2230D6Summaries = new ArrayList<>();

    // 增加年份循環
    int currentYear = Integer.parseInt(opcYYYMM.substring(0, 4));
    String currentMonthStr = opcYYYMM.substring(4, 6);
    for (int yearOffset = 0; yearOffset <= 5; yearOffset++) {
      int year = currentYear - yearOffset;
      String searchYearMonth = String.valueOf(year) + currentMonthStr;

      rptBP2230D6Summaries = rptAccountSummaryMapper.selectBP2230D6Summary(searchYearMonth);

      List<String> header02 = new ArrayList<>();
      csvGenerator.writeData(0, header02);
      List<String> header03 = new ArrayList<>();
      header03.add(String.format("%d 年度", year - 1911));
      header03.add("");
      header03.add("");
      header03.add("");
      for (int month = 1; month <= 12; month++) {
        header03.add(String.format("%02d 月", month));
      }
      header03.add("總 計");
      csvGenerator.writeData(0, header03);
      List<String> header04 = new ArrayList<>();
      header04.add("應收帳款");
      header04.add("加");
      header04.add("催收款項");
      header04.add("");
      for (int i = 1; i <= 12; i++) {
        header04.add("合計欠費╱非呆帳");
      }
      header04.add("總計欠費╱非呆帳");
      csvGenerator.writeData(0, header04);

      BigDecimal[] totals = new BigDecimal[13];
      for (int i = 0; i < totals.length; i++) {
        totals[i] = BigDecimal.ZERO;
      }

      for (RptBP2230D6Summary summary : rptBP2230D6Summaries) {
        List<String> dataRow = new ArrayList<>();
        String accItem = summary.getAccItem();
        if ("OTHER".equals(accItem)) {
          dataRow.add("1144-11N/1144-11S");
          dataRow.add("");
          dataRow.add("");
          dataRow.add("1144-11N/1144-11S");
        } else if (accItem == null) {
          dataRow.add(summary.getOvdItem());
          dataRow.add("");
          dataRow.add("");
          dataRow.add(summary.getAccName());
        } else {
          dataRow.add(accItem);
          if (summary.getOvdItem() != null && !summary.getOvdItem().trim().isEmpty()) {
            dataRow.add("+");
            dataRow.add(summary.getOvdItem());
          } else {
            dataRow.add("");
            dataRow.add("");
          }
          dataRow.add(summary.getAccName());
        }

        for (int month = 1; month <= 12; month++) {
          BigDecimal yearNonBadDebt =
              (BigDecimal) summary.getClass().getMethod("getNonBadDebt_" + month).invoke(summary);

          if (yearNonBadDebt == null) {
            yearNonBadDebt = BigDecimal.ZERO;
          }

          dataRow.add(StringUtils.formatNumberWithCommas(yearNonBadDebt));
          totals[month - 1] = totals[month - 1].add(yearNonBadDebt);
        }

        BigDecimal totalNonBadDebt =
            summary.getTotalNonBadDebt() != null ? summary.getTotalNonBadDebt() : BigDecimal.ZERO;
        dataRow.add(StringUtils.formatNumberWithCommas(totalNonBadDebt));
        totals[12] = totals[12].add(totalNonBadDebt);

        csvGenerator.writeData(0, dataRow);
      }

      List<String> formattedTotals = new ArrayList<>();
      for (BigDecimal total : totals) {
        formattedTotals.add(StringUtils.formatNumberWithCommas(total));
      }

      List<String> footer = new ArrayList<>();
      footer.add("");
      footer.add("");
      footer.add("總計");
      footer.add("");
      footer.addAll(formattedTotals);
      csvGenerator.writeData(0, footer);
      csvGenerator.nextRow();
    }

    // 儲存 CSV 檔案
    csvGenerator.save();

    dDataList.add(dData.builder()
                      .rptFileName(csvFileName)
                      .billOff("2")
                      .rptTimes("3")
                      .billMonth(rocYYYMM)
                      .rptDate(opcDate)
                      .rptFileCount(rptBP2230D6Summaries.size())
                      .rptSecretMark("N")
                      .build());

    return Result.builder()
        .rptCode(rptCode)
        .isRerun(isRerun)
        .opBatchno(jobId)
        .dDataList(dDataList)
        .build();
  }
}
