package ccbs.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import ccbs.service.impl.ArrearsRptServiceImpl;

public class TxtGenerator {

  private String filePath;
  private List<String> currentRow;
  private List<List<String>> allRows;

  public TxtGenerator(String filePath) {
    this.filePath = filePath;
    this.currentRow = new ArrayList<>();
    this.allRows = new ArrayList<>();
  }

  public void writeValue(int startIndex, int endIndex, String value) {
    writeValue(startIndex, endIndex, value, false);
  }

  public void writeValue(int startIndex, int endIndex, String value, boolean alignLeft) {
    startIndex -= 1;
    if (startIndex < 0 || endIndex <= startIndex) {
      throw new IndexOutOfBoundsException("Invalid start or end index.");
    }
    while (currentRow.size() < endIndex) {
      currentRow.add(" ");
    }
    int fixedLength = endIndex - startIndex;
    if (alignLeft) {
      value = String.format("%-" + fixedLength + "s", value);
    } else {
      value = String.format("%" + fixedLength + "s", value);
    }
    for (int i = 0; i < fixedLength; i++) {
      currentRow.set(startIndex + i, String.valueOf(value.charAt(i)));
    }
  }

  public void writeValueCentered(int startIndex, int endIndex, String value) {
    startIndex -= 1;
    if (startIndex < 0 || endIndex <= startIndex) {
      throw new IndexOutOfBoundsException("Invalid start or end index.");
    }
    while (currentRow.size() < endIndex) {
      currentRow.add(" ");
    }
    int fixedLength = endIndex - startIndex;
    int padding = (fixedLength - value.length()) / 2;
    value = String.format("%" + (padding + value.length()) + "s", value);
    value = String.format("%-" + fixedLength + "s", value);
    for (int i = 0; i < fixedLength; i++) {
      currentRow.set(startIndex + i, String.valueOf(value.charAt(i)));
    }
  }

  public void nextRow() {
    allRows.add(new ArrayList<>(currentRow));
    currentRow = new ArrayList<>();
  }

  public void save() throws IOException {
    try (BufferedWriter writer = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"))) {
      writer.write('\uFEFF');
      for (List<String> row : allRows) {
        String line = String.join("", row);
        writer.write(line);
        writer.newLine();
      }
      writer.flush();
    }
  }

  public void saveAsPdf(String pdfFilePath, String watermarkText) throws DocumentException, IOException {
    Document document = new Document();
    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
    document.open();
    
    Font font = ArrearsRptServiceImpl.PdfUtils.createFont(7, 0, null);
    
    // Write allRows content to PDF
    for (List<String> row : allRows) {
        String line = String.join("", row);
        document.add(new Paragraph(line, font)); // 使用支持中文的字型
    }
    
    // Add watermark to each page
    PdfContentByte canvas = writer.getDirectContentUnder();
    Phrase watermark = new Phrase(watermarkText);
    int totalPages = writer.getPageNumber();
    for (int i = 1; i <= totalPages; i++) {
        document.newPage();
        canvas = writer.getDirectContentUnder();
        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, watermark, 298, 421, 45);
    }
    
    document.close();
  }
}