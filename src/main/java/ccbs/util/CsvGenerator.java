package ccbs.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvGenerator {

  private String filePath;
  private List<List<String>> csvData;
  private int numCols;
  private String delimiter;

  public CsvGenerator(String filePath, int numCols, String delimiter) {
    this.filePath = filePath;
    this.numCols = numCols;
    this.delimiter = delimiter != null ? delimiter : ",";
    this.csvData = new ArrayList<>();
  }

  public void writeData(int colIndex, List<String> values) {
    if (colIndex < 0 || colIndex >= numCols) {
      throw new IndexOutOfBoundsException("Column index out of bounds.");
    }
    List<String> row = new ArrayList<>();
    for (int i = 0; i < numCols; i++) {
      if (i >= colIndex && i < colIndex + values.size()) {
        row.add(values.get(i - colIndex));
      } else {
        row.add("");
      }
    }
    csvData.add(row);
  }

  public void nextRow() {
    List<String> emptyRow = new ArrayList<>();
    for (int i = 0; i < numCols; i++) {
      emptyRow.add("");
    }
    csvData.add(emptyRow);
  }

  public void save() throws IOException {
    try (BufferedWriter writer = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"))) {
      if (!csvData.isEmpty()) {
        writer.write('\uFEFF');
      }
      for (List<String> row : csvData) {
        String line = String.join(delimiter, row);
        writer.write(line);
        writer.newLine();
      }
      writer.flush();
    }
  }
}