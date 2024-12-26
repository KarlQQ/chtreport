package ccbs.util.comm02;

import ccbs.model.batch.RptFileZipEncryptSingleIn;
import ccbs.model.batch.RptFileZipEncryptSingleOut;
import ccbs.model.batch.RptWatermarkSingleIn;
import ccbs.model.batch.RptWatermarkSingleOut;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Comm02ServiceImpl implements Comm02Service {
  //   private boolean chkFileExist(String encryptedFile) {
  //     // Create a File object representing the file
  //     File file = new File(encryptedFile);

  //     // Check if the file exists
  //     if (file.exists() && file.isFile()) {
  //       System.out.println("File exists: " + file.getAbsolutePath());
  //       return true;
  //     } else {
  //       System.out.println("File does not exist.");
  //       return false;
  //     }
  //   }

  //    RQBP010_(共用)報表浮水印
  @Override
  public RptWatermarkSingleOut COMM02_0007_single_file_watermark(RptWatermarkSingleIn inputStr) {
    boolean addWatermarkOkFlg = false;
    // file extension chk
    String fileExtension =
        inputStr.getRptFileName().substring((inputStr.getRptFileName().indexOf('.') + 1));

    if (fileExtension.equals("pdf")) {
      addWatermarkOkFlg = addWatermarkToPdf(inputStr);
    }

    RptWatermarkSingleOut rptWatermarkSingleOut = new RptWatermarkSingleOut();
    if (addWatermarkOkFlg) {
      rptWatermarkSingleOut.setCodeResult(true);
    }

    return rptWatermarkSingleOut;
  }

  private boolean addWatermarkToPdf(RptWatermarkSingleIn inputStr) {
    //        String src = "input.pdf"; // Path to the input PDF
    //        String dest = "output_with_watermark.pdf"; // Path to the output PDF with watermark

    String inputPdfFile = inputStr.getRptFilePath() + inputStr.getRptFileName();
    String outputPdfFile = inputStr.getWatermarkRptFilePath() + inputStr.getWatermarkRptFileName();

    try {
      PdfReader reader = new PdfReader(inputPdfFile);
      FileOutputStream fileOutputStream = new FileOutputStream(outputPdfFile);
      PdfStamper stamper = new PdfStamper(reader, fileOutputStream);
      // tmp comment 2024/09/16 move add password to encrypt method
      //            stamper = setEncryption(stamper, inputStr.getEmpID());

      PdfContentByte canvas;
      int n = reader.getNumberOfPages();

      // Iterate over each page
      for (int i = 1; i <= n; i++) {
        canvas = stamper.getOverContent(i);
        canvas.setColorFill(new BaseColor(211, 211, 211));
        // Set watermark properties
        Phrase watermark = new Phrase(inputStr.getEmpID());
        // Get page size
        Rectangle pageSize = reader.getPageSize(i);

        // Add watermark to page
        //                ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, watermark,
        //                pageSize.getWidth() / 2, pageSize.getHeight() / 2, 45);

        float[][] XY = getWaterMarkPositionaArray(pageSize);
        for (int j = 0; j < 13; j++) {
          ColumnText.showTextAligned(
              canvas, Element.ALIGN_CENTER, watermark, XY[j][0], XY[j][1], 45);
        }

        //                pdfPageContents.ShowTextAligned(iTextSharp.text.pdf.PdfContentByte.ALIGN_
        //                        CENTER, sText, XY[j, 0],XY[j, 1],textAngle);
      }

      stamper.close();
      reader.close();
      System.out.println("Watermark added successfully.");
    } catch (IOException | DocumentException e) {
      e.printStackTrace();
    }

    return true;
  }

  @Override
  public boolean COMM02_0006_single_file_pdf_encrypt(RptWatermarkSingleIn inputStr) {
    //        String src = "input.pdf"; // Path to the input PDF
    //        String dest = "output_with_watermark.pdf"; // Path to the output PDF with watermark

    String inputPdfFile = inputStr.getWatermarkRptFilePath() + inputStr.getWatermarkRptFileName();
    String outputPdfFile = inputStr.getRptFilePath() + inputStr.getRptFileName();

    try {
      PdfReader reader = new PdfReader(inputPdfFile);
      FileOutputStream fileOutputStream = new FileOutputStream(outputPdfFile);
      PdfStamper stamper = new PdfStamper(reader, fileOutputStream);

      stamper = setEncryption(stamper, inputStr.getEmpID());
      stamper.close();

      reader.close();
      System.out.println("Watermark added successfully.");
    } catch (IOException | DocumentException e) {
      e.printStackTrace();
    }

    return true;
  }

  private PdfStamper setEncryption(PdfStamper stamper, String empID) throws DocumentException {
    // Set encryption
    stamper.setEncryption(empID.getBytes(), // User password
        empID.getBytes(), // Owner password
        PdfWriter.ALLOW_PRINTING, // Permissions
        PdfWriter.ENCRYPTION_AES_128 // Encryption type
    );

    return stamper;
  }

  private float[][] getWaterMarkPositionaArray(Rectangle pageSize) {
    float[][] XY = new float[13][2];
    float fSlope = pageSize.getHeight() / pageSize.getWidth();
    float fSpacing = 215f;

    if (pageSize.getWidth() < pageSize.getHeight()) {
      XY[0][0] = pageSize.getWidth() / 2;
      XY[0][1] = pageSize.getHeight() / 2;
      XY[1][0] = pageSize.getWidth() / 2 - fSpacing / 2;
      XY[1][1] = pageSize.getHeight() / 2 - fSpacing / 2 * fSlope;
      XY[2][0] = pageSize.getWidth() / 2 - fSpacing / 2;
      XY[2][1] = pageSize.getHeight() / 2 + fSpacing / 2 * fSlope;
      XY[3][0] = pageSize.getWidth() / 2 + fSpacing / 2;
      XY[3][1] = pageSize.getHeight() / 2 - fSpacing / 2 * fSlope;
      XY[4][0] = pageSize.getWidth() / 2 + fSpacing / 2;
      XY[4][1] = pageSize.getHeight() / 2 + fSpacing / 2 * fSlope;
      XY[5][0] = pageSize.getWidth() / 2 - fSpacing;
      XY[5][1] = pageSize.getHeight() / 2 - fSpacing * fSlope;
      XY[6][0] = pageSize.getWidth() / 2 - fSpacing;
      XY[6][1] = pageSize.getHeight() / 2 + fSpacing * fSlope;
      XY[7][0] = pageSize.getWidth() / 2 + fSpacing;
      XY[7][1] = pageSize.getHeight() / 2 - fSpacing * fSlope;
      XY[8][0] = pageSize.getWidth() / 2 + fSpacing;
      XY[8][1] = pageSize.getHeight() / 2 + fSpacing * fSlope;
      XY[9][0] = pageSize.getWidth() / 2;
      XY[9][1] = pageSize.getHeight() / 2 - fSpacing * fSlope;
      XY[10][0] = pageSize.getWidth() / 2;
      XY[10][1] = pageSize.getHeight() / 2 + fSpacing * fSlope;
      XY[11][0] = pageSize.getWidth() / 2 - fSpacing;
      XY[11][1] = pageSize.getHeight() / 2;
      XY[12][0] = pageSize.getWidth() / 2 + fSpacing;
      XY[12][1] = pageSize.getHeight() / 2;
    } else {
      XY[0][0] = pageSize.getWidth() / 2;
      XY[0][1] = pageSize.getHeight() / 2;
      XY[1][0] = pageSize.getWidth() / 2 - fSpacing / 2 * fSlope;
      XY[1][1] = pageSize.getHeight() / 2 - fSpacing / 2;
      XY[2][0] = pageSize.getWidth() / 2 - fSpacing / 2 * fSlope;
      XY[2][1] = pageSize.getHeight() / 2 + fSpacing / 2;
      XY[3][0] = pageSize.getWidth() / 2 + fSpacing / 2 * fSlope;
      XY[3][1] = pageSize.getHeight() / 2 - fSpacing / 2;
      XY[4][0] = pageSize.getWidth() / 2 + fSpacing / 2 * fSlope;
      XY[4][1] = pageSize.getHeight() / 2 + fSpacing / 2;
      XY[5][0] = pageSize.getWidth() / 2 - fSpacing * fSlope;
      XY[5][1] = pageSize.getHeight() / 2 - fSpacing;
      XY[6][0] = pageSize.getWidth() / 2 - fSpacing * fSlope;
      XY[6][1] = pageSize.getHeight() / 2 + fSpacing;
      XY[7][0] = pageSize.getWidth() / 2 + fSpacing * fSlope;
      XY[7][1] = pageSize.getHeight() / 2 - fSpacing;
      XY[8][0] = pageSize.getWidth() / 2 + fSpacing * fSlope;
      XY[8][1] = pageSize.getHeight() / 2 + fSpacing;
      XY[9][0] = pageSize.getWidth() / 2 - fSpacing * fSlope;
      XY[9][1] = pageSize.getHeight() / 2;
      XY[10][0] = pageSize.getWidth() / 2 + fSpacing * fSlope;
      XY[10][1] = pageSize.getHeight() / 2;
      XY[11][0] = pageSize.getWidth() / 2;
      XY[11][1] = pageSize.getHeight() / 2 - fSpacing;
      XY[12][0] = pageSize.getWidth() / 2;
      XY[12][1] = pageSize.getHeight() / 2 + fSpacing;
    }
    return XY;
  }

  @Override
  public RptFileZipEncryptSingleOut COMM02_0006_single_file_zip_encrypt(
      RptFileZipEncryptSingleIn inputStr) {
    String absoluteFileName = inputStr.getRptFilePathAndName();
    String absoluteZipFileNmae = inputStr.getZipRptFilePathAndName();
    String empIdAsPassword = inputStr.getEmpID();
    createPasswordProtectedZip(absoluteFileName, absoluteZipFileNmae, empIdAsPassword);
    System.out.println("ZIP file created successfully.");

    RptFileZipEncryptSingleOut rptFileZipEncryptSingleOut = new RptFileZipEncryptSingleOut();
    rptFileZipEncryptSingleOut.setCodeResult(true);
    return rptFileZipEncryptSingleOut;
  }

  public boolean createPasswordProtectedZip(
      String inpuptFileNameWithPath, String zipFileNameWithPath, String password) {
    ZipFile zipFile = new ZipFile(zipFileNameWithPath, password.toCharArray());
    ZipParameters zipParameters = new ZipParameters();
    zipParameters.setEncryptFiles(true);
    zipParameters.setEncryptionMethod(EncryptionMethod.AES);
    try {
      zipFile.addFile(new File(inpuptFileNameWithPath), zipParameters);
      zipFile.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    System.out.println("ZIP file with password created successfully.");

    return true;
  }
}

//    public RptCryptSingleOut COMM02_0006_single_file_encrypt(RptCryptSingleIn inputStr) {
//        String empIdAsPassword = inputStr.getEmpID();
//        String inputFile = inputStr.getRptFilePath() + inputStr.getRptFileName();
//        String encryptedFile = inputStr.getEncryptRptFilePath() +
//        inputStr.getEncryptRptFileName();
//
//        try {
//            encryptFile(inputFile, encryptedFile, empIdAsPassword);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        boolean fileExistFlg = chkFileExist(encryptedFile);
//
//        RptCryptSingleOut rptEncryptSingleOut = new RptCryptSingleOut();
//        rptEncryptSingleOut.setCodeResult(fileExistFlg);
//
//        return rptEncryptSingleOut;
//    }
//
//
//    public RptCryptSingleOut COMM02_0006_single_file_decrypt(RptCryptSingleIn inputStr) {
//        String empIdAsPassword = inputStr.getEmpID();
//        String encryptedFile = inputStr.getEncryptRptFilePath() +
//        inputStr.getEncryptRptFileName(); String decryptedFile = inputStr.getDecryptRptFilePath()
//        + inputStr.getDecryptRptFileName();
//
//        try {
//            decryptFile(encryptedFile, decryptedFile, empIdAsPassword);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        boolean fileExistFlg = chkFileExist(decryptedFile);
//
//        RptCryptSingleOut rptEncryptSingleOut = new RptCryptSingleOut();
//        rptEncryptSingleOut.setCodeResult(fileExistFlg);
//
//        return rptEncryptSingleOut;
//    }

//    RQBP009_(共用)報表加密
// pdf file with password
//    public RptCryptSingleOut COMM02_0006_single_pdf_file_encrypt(RptCryptSingleIn inputStr) {
//        String empIdAsPassword = inputStr.getEmpID();
//        String inputFile = inputStr.getRptFilePath() + inputStr.getRptFileName();
//        String encryptedFile = inputStr.getEncryptRptFilePath() +
//        inputStr.getEncryptRptFileName();
////        String src = "source.pdf"; // Path to the source PDF file
////        String dest = "protected.pdf"; // Path where the password-protected PDF will be saved
//
//        try {
////            // Load the existing PDF
////            PdfReader reader = new PdfReader(inputFile);
////            // Create a PdfStamper instance to modify the PDF
////            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(encryptedFile));
//
//            // Set user password and owner password
//            String userPassword = empIdAsPassword;
//            String ownerPassword = empIdAsPassword;
//
//            // Read the existing PDF
//            PdfReader reader = new PdfReader(inputFile);
//
//            // Create a PdfStamper to apply encryption
//            FileOutputStream fos = new FileOutputStream(encryptedFile);
//
//            PdfStamper stamper = new PdfStamper(reader, fos);
//
//            // Set encryption
//            stamper.setEncryption(
//                    userPassword.getBytes(),  // User password
//                    ownerPassword.getBytes(), // Owner password
//                    PdfWriter.ALLOW_PRINTING, // Permissions
//                    PdfWriter.ENCRYPTION_AES_128 // Encryption type
//            );
//
//            // Close PdfStamper
//            stamper.close();
//
//            // Close PdfReader
//            reader.close();
//
//            System.out.println("PDF file has been password-protected successfully.");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
////        String empIdAsPassword = inputStr.getEmpID();
////        String inputFile = inputStr.getRptFilePath() + inputStr.getRptFileName();
////        String encryptedFile = inputStr.getEncryptRptFilePath() +
/// inputStr.getEncryptRptFileName();
////
////        try {
////            encryptFile(inputFile, encryptedFile, empIdAsPassword);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//
//        boolean fileExistFlg = chkFileExist(encryptedFile);
//
//        RptCryptSingleOut rptEncryptSingleOut = new RptCryptSingleOut();
//        rptEncryptSingleOut.setCodeResult(fileExistFlg);
//
//        return rptEncryptSingleOut;
//    }

//    public static void encryptFile(String inputFilePath, String outputFilePath, String password)
//    throws Exception {
//        // Generate salt and initialization vector (IV)
//        byte[] salt = generateSalt();
//        byte[] iv = new byte[16];
//        SecureRandom random = new SecureRandom();
//        random.nextBytes(iv);
//
//        // Derive key from password and salt
//        SecretKey key = getKeyFromPassword(password, salt);
//
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
//
//        try (FileInputStream inputFile = new FileInputStream(inputFilePath);
//             FileOutputStream outputFile = new FileOutputStream(outputFilePath)) {
//
//            // Write salt and IV to the output file
//            outputFile.write(salt);
//            outputFile.write(iv);
//
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//            while ((bytesRead = inputFile.read(buffer)) != -1) {
//                byte[] encryptedBytes = cipher.update(buffer, 0, bytesRead);
//                if (encryptedBytes != null) {
//                    outputFile.write(encryptedBytes);
//                }
//            }
//            byte[] finalBytes = cipher.doFinal();
//            if (finalBytes != null) {
//                outputFile.write(finalBytes);
//            }
//        }
//    }

//    public static SecretKey getKeyFromPassword(String password, byte[] salt) throws Exception {
//        int iterations = 65536;
//        int keyLength = 256; // Key size in bits
//
//        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
//        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
//        byte[] key = factory.generateSecret(spec).getEncoded();
//        return new SecretKeySpec(key, "AES");
//    }
//
//    public static byte[] generateSalt() throws Exception {
//        byte[] salt = new byte[16]; // 16 bytes = 128 bits
//        SecureRandom random = new SecureRandom();
//        random.nextBytes(salt);
//        return salt;
//    }

//    public static void decryptFile(String inputFilePath, String outputFilePath, String password)
//    throws Exception {
//        try (FileInputStream inputFile = new FileInputStream(inputFilePath);
//             FileOutputStream outputFile = new FileOutputStream(outputFilePath)) {
//
//            // Read salt and IV from the input file
//            byte[] salt = new byte[16];
//            inputFile.read(salt);
//            byte[] iv = new byte[16];
//            inputFile.read(iv);
//
//            // Derive key from password and salt
//            SecretKey key = getKeyFromPassword(password, salt);
//
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
//
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//            while ((bytesRead = inputFile.read(buffer)) != -1) {
//                byte[] decryptedBytes = cipher.update(buffer, 0, bytesRead);
//                if (decryptedBytes != null) {
//                    outputFile.write(decryptedBytes);
//                }
//            }
//            byte[] finalBytes = cipher.doFinal();
//            if (finalBytes != null) {
//                outputFile.write(finalBytes);
//            }
//        }
//    }
