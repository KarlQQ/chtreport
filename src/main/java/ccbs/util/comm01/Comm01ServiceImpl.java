package ccbs.util.comm01;

import ccbs.dao.core.entity.CommOffice;
import ccbs.model.batch.PersonalInfoMaskStr;
import ccbs.model.online.OfficeInfoQueryIn;
import ccbs.model.online.OfficeInfoQueryOut;
import ccbs.model.online.UserInfoQueryApiIn;
import ccbs.model.online.UserInfoQueryIn;
import ccbs.model.online.UserInfoQueryOut;
import ccbs.service.intf.CommOfficeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class Comm01ServiceImpl implements Comm01Service {
  @Value("${ccbs.userInfoApiUrl}") private String userInfoApiUrl;

  @Value("${ccbs.userInfoApiKey}") private String userInfoApiKey;

  @Value("${ccbs.userInfoApiIv}") private String userInfoApiIv;

  @Autowired private CommOfficeService commOfficeService;
  // @Autowired private RestTemplateConfig restTemplateConfig;

  private final RestClient restClient;

  @Autowired
  public Comm01ServiceImpl(RestClient restClient) {
    this.restClient = restClient;
  }

  //        RQBP002_以證號辨別是否為自然人
  public static boolean COMM01_0001(String inputStr) {
    //        inputStr = "D600449832";
    //        inputStr = "D600552890";
    Integer inputLength = inputStr.trim().length();

    if (inputLength == 10) {
      String regexForNatural = "[a-zA-Z]\\d{9}";
      Pattern pattern = Pattern.compile(regexForNatural);
      Matcher matcher = pattern.matcher(inputStr);
      boolean isMatch = matcher.matches();
      if (!isMatch) {
        return false;
      }

      int firstCharCheckValue = getFirstCharCheckValue(inputStr.substring(0, 1).toUpperCase());

      String checkIdStr = firstCharCheckValue + inputStr.substring(1);

      int endCharCheckValue = getEndCharCheckValue(checkIdStr);
      int endCharValue = Integer.parseInt(inputStr.substring(9));

      if (firstCharCheckValue != 99) {
        if (endCharCheckValue == endCharValue) {
          return true;
        }
      }
    }
    return false;
  }

  private static int getFirstCharCheckValue(String firstChar) {
    int representValue;

    switch (firstChar) {
      case "A":
        representValue = 10;
        break;
      case "B":
        representValue = 11;
        break;
      case "C":
        representValue = 12;
        break;
      case "D":
        representValue = 13;
        break;
      case "E":
        representValue = 14;
        break;
      case "F":
        representValue = 15;
        break;
      case "G":
        representValue = 16;
        break;
      case "H":
        representValue = 17;
        break;
      case "I":
        representValue = 34;
        break;
      case "J":
        representValue = 18;
        break;
      case "K":
        representValue = 19;
        break;
      case "L":
        representValue = 20;
        break;
      case "M":
        representValue = 21;
        break;
      case "N":
        representValue = 22;
        break;
      case "O":
        representValue = 35;
        break;
      case "P":
        representValue = 23;
        break;
      case "Q":
        representValue = 24;
        break;
      case "R":
        representValue = 25;
        break;
      case "S":
        representValue = 26;
        break;
      case "T":
        representValue = 27;
        break;
      case "U":
        representValue = 28;
        break;
      case "V":
        representValue = 29;
        break;
      case "W":
        representValue = 32;
        break;
      case "X":
        representValue = 30;
        break;
      case "Y":
        representValue = 31;
        break;
      case "Z":
        representValue = 33;
        break;
      default:
        representValue = 99;
    }

    if (representValue != 99) {
      int singleDigit = representValue % 10;
      int tensDigit = (representValue - singleDigit) / 10;

      return (singleDigit * 9 + tensDigit) % 10;
    } else {
      return 99;
    }
  }

  private static int getEndCharCheckValue(String inputStr) {
    // 1,8,7,6,5,4,3,2,1
    int char1 = Integer.parseInt(inputStr.substring(0, 1));
    int char2 = Integer.parseInt(inputStr.substring(1, 2));
    int char3 = Integer.parseInt(inputStr.substring(2, 3));
    int char4 = Integer.parseInt(inputStr.substring(3, 4));
    int char5 = Integer.parseInt(inputStr.substring(4, 5));
    int char6 = Integer.parseInt(inputStr.substring(5, 6));
    int char7 = Integer.parseInt(inputStr.substring(6, 7));
    int char8 = Integer.parseInt(inputStr.substring(7, 8));
    int char9 = Integer.parseInt(inputStr.substring(8, 9));

    int charSum = char1 * 1 + char2 * 8 + char3 * 7 + char4 * 6 + char5 * 5 + char6 * 4 + char7 * 3
        + char8 * 2 + char9 * 1;

    int finalComplement = (charSum % 10 == 0) ? 0 : 10 - charSum % 10;

    return finalComplement;
  }

  //        RQBP003_(共用)個資隱碼
  //序號 	資料項目名稱 	型態 	長度 	說明
  // 1 	姓名 	maskFirstName 	string 	128 	姓名只顯示姓
  // 2 	證號 	maskIDNumber 	string 	10 	證號(自然人)只顯示前六碼
  // 3 	地址 	maskAddress 	string 	256 	地址(顯示至路名或12Bytes) 惟『路』前面之一個中文字，
  // 必須隱碼 4 	銀行帳號或信用卡號碼 	maskBankAccount 	string 	25
  // 銀行帳號或信用卡號碼(需隱碼倒數2,4,6,8碼)
  // 5 	設備號碼 	maskTelno 	string 	20 	須隱碼倒數第 2、4、6 碼

  //        RQBP003_(共用)個資隱碼
  public static PersonalInfoMaskStr COMM01_0002(PersonalInfoMaskStr inputStr) {
    PersonalInfoMaskStr outputStr = new PersonalInfoMaskStr();

    //姓名
    if (!inputStr.isMaskFirstNameNull()) {
      Character firstChar = inputStr.getMaskFirstName().toCharArray()[0];
      Boolean isChinese = isChineseCharacter(firstChar);
      if (isChinese) {
        // Chinese replacement
        //  Regex pattern to match alphabetic characters
        String regex = ".";
        // Replacement string
        // White Circle (○)
        String replacement = "\u25CB";

        String outputMaskFirstNameCht =
            firstChar + inputStr.getMaskFirstName().substring(1).replaceAll(regex, replacement);
        outputStr.setMaskFirstName(outputMaskFirstNameCht);

      } else {
        // English replacement
        //  Regex pattern to match alphabetic characters
        String regex = ".";
        // Replacement string
        String replacement = "*";

        Character lastChar =
            inputStr.getMaskFirstName().toCharArray()[inputStr.getMaskFirstName().length() - 1];

        String outputMaskFirstNameEng = firstChar
            + inputStr.getMaskFirstName()
                  .substring(1, inputStr.getMaskFirstName().length() - 1)
                  .replaceAll(regex, replacement)
            + lastChar;
        outputStr.setMaskFirstName(outputMaskFirstNameEng);
      }
    }

    if (!inputStr.isMaskIDNumberNull()) {
      //編號
      // maskIDNumber
      if (inputStr.getMaskIDNumber().trim().length() == 10) {
        // Natural Person replacement
        //                // Regex pattern to match alphabetic characters
        //                String regex = ".";
        //                // Replacement string
        //                String replacement = "*";
        //                String outputMaskIDNumber =
        //                inputStr.getMaskIDNumber().substring(6).replaceAll(regex, replacement);
        String outputMaskIDNumber;

        if (COMM01_0001(inputStr.getMaskIDNumber())) {
          outputMaskIDNumber = inputStr.getMaskIDNumber().substring(0, 6) + "****";
        } else {
          outputMaskIDNumber = inputStr.getMaskIDNumber();
        }

        outputStr.setMaskIDNumber(outputMaskIDNumber);
      } else {
        outputStr.setMaskIDNumber(inputStr.getMaskIDNumber());
      }
    }

    if (!inputStr.isMaskAddressNull()) {
      //地址
      //                       // 市 區
      //                       //縣 市鎮鄉門
      //                       //島 島
      //                       //台臺 台臺
      byte[] byteArray = inputStr.getMaskAddress().getBytes();
      if (inputStr.getMaskAddress().indexOf("路") != -1) {
        int roadIdx = inputStr.getMaskAddress().indexOf("路");
        String whiteCircle = "\u25CB";
        String outputMaskAddress =
            inputStr.getMaskAddress().substring(0, roadIdx - 1) + whiteCircle + "路";
        outputStr.setMaskAddress(outputMaskAddress);
      } else {
        byte[] subByteArray = new byte[18];
        System.arraycopy(byteArray, 0, subByteArray, 0, 18);
        String outputMaskAddress = new String(subByteArray, StandardCharsets.UTF_8);
        outputStr.setMaskAddress(outputMaskAddress);
      }
    }

    if (!inputStr.isMaskBankAccountNull()) {
      //銀行帳號或信用卡號碼需隱碼倒數2,4,6,8碼)
      int inputBankAccounLength = inputStr.getMaskBankAccount().length();
      char[] bankAccountCharArray = inputStr.getMaskBankAccount().toCharArray();
      bankAccountCharArray[inputBankAccounLength - 2] = '*';
      bankAccountCharArray[inputBankAccounLength - 4] = '*';
      bankAccountCharArray[inputBankAccounLength - 6] = '*';
      bankAccountCharArray[inputBankAccounLength - 8] = '*';
      String outputMaskBankAccount = new String(bankAccountCharArray);
      outputStr.setMaskBankAccount(outputMaskBankAccount);
    }

    if (!inputStr.isMaskTelnoNull()) {
      //設備號碼
      int inputTelnoLength = inputStr.getMaskTelno().length();
      // White Circle (○)Circle
      char whiteCircle = '\u25CB';
      char[] telnoCharArray = inputStr.getMaskTelno().toCharArray();
      telnoCharArray[inputTelnoLength - 2] = whiteCircle;
      telnoCharArray[inputTelnoLength - 4] = whiteCircle;
      telnoCharArray[inputTelnoLength - 6] = whiteCircle;
      String outputMaskTelno = new String(telnoCharArray);
      outputStr.setMaskTelno(outputMaskTelno);
    }

    return outputStr;
  }

  private static boolean isChineseCharacter(char ch) {
    // Unicode ranges for Chinese characters
    return (ch >= '\u4E00' && ch <= '\u9FFF') || // CJK Unified Ideographs
        (ch >= '\u3400' && ch <= '\u4DBF') || // CJK Unified Ideographs Extension A
        (ch >= '\uF900' && ch <= '\uFAFF'); // CJK Compatibility Ideographs
    // comment out tmp
    //                        (ch >= '\u20000' && ch <= '\u2A6DF') || // CJK Unified Ideographs
    //                        Extension B (ch >= '\u2A700' && ch <= '\u2B73F') || // CJK Unified
    //                        Ideographs Extension C (ch >= '\u2B740' && ch <= '\u2B81F') || // CJK
    //                        Unified Ideographs Extension D (ch >= '\u2F800' && ch <= '\u2FA1F');
    //                        // CJK Compatibility Ideographs Supplement
  }

  @Override
  public UserInfoQueryOut COMM001_0005(UserInfoQueryIn userInfoQueryIn) {
    try {
      String url = userInfoApiUrl;
      log.debug("UserInfo API URL: " + url);

      UserInfoQueryApiIn userInfoQueryApiIn = new UserInfoQueryApiIn();
      userInfoQueryApiIn.setSYSTEM_ID(userInfoQueryIn.getSystemId());
      log.debug("SYSTEM_ID: " + userInfoQueryIn.getSystemId());

      userInfoQueryApiIn.setEMP_ID(
          userInfoQueryIn.getEmpId().substring(userInfoQueryIn.getEmpId().length() - 6));
      log.debug("EMP_ID: " + userInfoQueryApiIn.getEMP_ID());

      userInfoQueryApiIn.setDATETIME(userInfoQueryIn.getDateTime() + ".000Z");
      log.debug("DATETIME: " + userInfoQueryApiIn.getDATETIME());

      String requestBodyJsonString = new ObjectMapper().writeValueAsString(userInfoQueryApiIn);
      requestBodyJsonString = requestBodyJsonString.toUpperCase();
      log.debug("Request Body JSON String: " + requestBodyJsonString);

      String authorizationValue = authValEncrypt(requestBodyJsonString);
      log.debug("Authorization Value: " + authorizationValue);

      ResponseEntity<UserInfoQueryOut> response = restClient.post()
                                                      .uri(url)
                                                      .header("Authorization", authorizationValue)
                                                      .header("Content-Type", "application/json")
                                                      .body(userInfoQueryApiIn)
                                                      .retrieve()
                                                      .toEntity(UserInfoQueryOut.class);

      log.debug("Response: " + response);

      return response.getBody();

    } catch (Exception e) {
      log.error("Exception occurred in COMM001_0005: ", e);
      return null;
    }
  }

  @Override
  public String authValEncrypt(String inputStr) {
    //        String plaintext =
    //        "{\"SYSTEM_ID\":\"BP\",\"TAX_ID\":\"A\",\"QUERY_TYPE\":\"1\",\"ITEM_CODE_LIST\":null}";
    byte[] key = Base64.getDecoder().decode(userInfoApiKey);
    log.debug("key: " + key);
    byte[] iv = Base64.getDecoder().decode(userInfoApiIv);
    log.debug("iv: " + iv);
    // 進行加密
    try {
      String encryptedText = encrypt(inputStr, key, iv);

      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = digest.digest(encryptedText.getBytes());

      StringBuilder builder = new StringBuilder();

      for (byte hashByte : hashBytes) {
        builder.append(String.format("%02x", hashByte));
      }

      String strFinal = builder.toString();

      log.debug("strFinal: " + strFinal);
      return strFinal;

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private String encrypt(String inputStr, byte[] keyBytes, byte[] ivBytes) {
    try {
      // Example key and IV (should be securely generated in practice)
      //            byte[] keyBytes = key.getBytes(); // DES key must be 8 bytes
      //            byte[] ivBytes = new byte[8]; // DES IV must be 8 bytes

      //            // Generate a random IV
      //            SecureRandom random = new SecureRandom();
      //            random.nextBytes(ivBytes);

      Security.addProvider(new BouncyCastleProvider());

      // Convert key and IV to SecretKey and IvParameterSpec
      // des iv 8 bytes , aes iv 16 bytes
      SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
      IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

      // Initialize Cipher for encryption with DES, CFB mode, and PKCS7 padding
      Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

      // Data to encrypt
      byte[] dataBytes = inputStr.getBytes(StandardCharsets.UTF_8);
      log.debug("dataBytes: " + dataBytes);

      // Encrypt data
      byte[] encryptedBytes = cipher.doFinal(dataBytes);
      log.debug("encryptedBytes: " + encryptedBytes);

      // Convert encrypted bytes to Base64 for easier display
      String encryptedText = Base64.getEncoder().encodeToString(encryptedBytes);
      // String ivBase64 = Base64.getEncoder().encodeToString(ivBytes);

      // Print the results
      log.debug("encryptedText: " + encryptedBytes);
      log.debug("ivBase64: " + encryptedBytes);

      return encryptedText;

    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public static <T> T post(String callApiUrl, Object queryEntity, Class<T> returnClass)
      throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    HttpURLConnection connection = null;
    BufferedReader br = null;

    try {
      // 呼叫 api 取得回傳資訊(br)。
      if (callApiUrl.toLowerCase().startsWith("https")) {
        connection = postSSL(callApiUrl, queryEntity);
        // } else if (callApiUrl.toLowerCase().startsWith("http")) {
      } else {
        //                connection = post(callApiUrl, queryEntity);
      }

      br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

      StringBuilder sbReadline = new StringBuilder();
      String readLine;
      while ((readLine = br.readLine()) != null) {
        sbReadline.append(readLine);
      }
      // return sbReadline.toString();
      //            _log.info("[call apiUrl[" + callApiUrl + "] input[" +
      //            mapper.writeValueAsString(queryEntity) + "] return[" + sbReadline.toString() +
      //            "]");
      if (returnClass != null) {
        return mapper.readValue(sbReadline.toString(), returnClass);
      }
    } catch (Exception e) {
      //            _log.error("call apiUrl[" + callApiUrl + "] error", e);
      throw e;
    } finally {
      close(br);
      close(connection);
    }
    return null;
  }

  private static HttpsURLConnection postSSL(String callApiUrl, Object queryEntity)
      throws Exception {
    HttpsURLConnection connection = null;
    PrintStream printStream = null;
    try {
      // Create a trust manager that does not validate certificate chains
      TrustManager[] trustAllCerts = new TrustManager[] {
          new X509TrustManager(){public java.security.cert.X509Certificate[] getAcceptedIssuers(){
              return new java.security.cert.X509Certificate[0];
    }

    public void checkClientTrusted(X509Certificate[] certs, String authType) {}

    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
  }
};

// Install the all-trusting trust manager
SSLContext sc = SSLContext.getInstance("TLSv1.2");
sc.init(null, trustAllCerts, new java.security.SecureRandom());
HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

// Create all-trusting host name verifier
HostnameVerifier allHostsValid = new HostnameVerifier() {
  public boolean verify(String hostname, SSLSession session) {
    // Use the default hostname verification method
    return HttpsURLConnection.getDefaultHostnameVerifier().verify(hostname, session);
  }
};

// Install the all-trusting host verifier
HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

//            @SuppressWarnings("restriction")
//            URL url = new URL(null, callApiUrl, new sun.net.www.protocol.https.Handler());
//            connection = (HttpsURLConnection) url.openConnection();
URL url = new URL(callApiUrl);
connection = (HttpsURLConnection) url.openConnection();

connection.setRequestMethod("POST");
connection.setRequestProperty("Content-Type", "application/json");
connection.setDoOutput(true);

printStream = new PrintStream(connection.getOutputStream());

String queryJson = new ObjectMapper().writeValueAsString(queryEntity);
printStream.print(queryJson);

return connection;
}
catch (Exception e) {
  throw e;
}
finally {
  close(printStream);
}
}

private static void close(AutoCloseable obj) {
  try {
    if (obj != null) {
      obj.close();
    }
  } catch (Exception e) {
    //            _log.error("", e);
  }
}

private static void close(HttpURLConnection obj) {
  try {
    if (obj != null) {
      obj.disconnect();
    }
  } catch (Exception e) {
    //            _log.error("", e);
  }
}

//(共用)機構代號轉中文名
@Override
public OfficeInfoQueryOut COMM01_0003(OfficeInfoQueryIn inputStr) {
  OfficeInfoQueryOut returnInfoQueryOut = new OfficeInfoQueryOut();
  try {
    String officeCode = inputStr.getOfficeCode().trim();
    String transType = inputStr.getTransType().trim();
    Optional<CommOffice> returnCommOffice =
        Optional.ofNullable(commOfficeService.getAllRelatedOffices(officeCode, transType));

    if (returnCommOffice.isPresent()) {
      CommOffice office = returnCommOffice.get();
      returnInfoQueryOut.setResultStatus("0");
      returnInfoQueryOut.setResultOfficeCode(office.getOffice().trim());
      returnInfoQueryOut.setResultOfficeCN(office.getOffName());
    } else {
      returnInfoQueryOut.setResultStatus("1");
      returnInfoQueryOut.setResultOfficeCode(officeCode);
    }

  } catch (Exception e) {
    log.error("Error in COMM01_0003: ", e);
    returnInfoQueryOut.setResultStatus("1");
    returnInfoQueryOut.setResultOfficeCode(inputStr.getOfficeCode());
  }

  return returnInfoQueryOut;
}
}
