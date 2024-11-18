package ccbs.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ccbs.model.batch.BatchSimpleRptInStr;

public class ValidationUtils {

  public static ResponseEntity<String> validateBatchSimpleRptInStr(BatchSimpleRptInStr input) {
    ResponseEntity<String> response;

    if (input.getOpcDate() == null || input.getOpcDate().isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("opcDate 為必填欄位");
    }

    if (input.getOpcYearMonth() == null || input.getOpcYearMonth().isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("opcYYYMM 為必填欄位");
    }

    response = validateOpcDate(input.getOpcDate());
    if (response != null) {
      return response;
    }

    response = validateOpcYYYMM(input.getOpcYearMonth());
    if (response != null) {
      return response;
    }

    return null;
  }

  public static ResponseEntity<String> validateOpcDate(String opcDate) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    try {
      LocalDate.parse(opcDate, formatter);
    } catch (DateTimeParseException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("opcDate 格式不正確，必須為有效的 YYYYMMDD 日期");
    }
    return null;
  }

  public static ResponseEntity<String> validateOpcYYYMM(String opcYYYMM) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
    try {
      YearMonth.parse(opcYYYMM, formatter);
    } catch (DateTimeParseException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("opcYYYMM 格式不正確，必須為有效的 YYYYMM 日期");
    }
    return null;
  }

}