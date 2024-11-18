package ccbs.util;

import java.util.HashMap;
import java.util.Map;

public class NameMapping {
    private static final Map<String, String> businessGroupMap = new HashMap<>();

    static {
        businessGroupMap.put("A", "個人家庭事業群");
        businessGroupMap.put("B", "企客事業群");
        businessGroupMap.put("C", "國際事業群");
    }

    public static String getBusinessGroupName(String code) {
        return businessGroupMap.getOrDefault(code, "未知事業群");
    }

    private static final Map<String, String> rptFunctionCodeMap = new HashMap<>();

    static {
        rptFunctionCodeMap.put("DBM", "欠費餘額管理");
        rptFunctionCodeMap.put("DBL", "欠費明細清單");
        rptFunctionCodeMap.put("DBA", "應收營收帳款");
        rptFunctionCodeMap.put("PAY", "繳費");
        rptFunctionCodeMap.put("ABS", "調改帳");
        rptFunctionCodeMap.put("NWO", "負數沖帳");
        rptFunctionCodeMap.put("PWO", "繳費銷帳");
        rptFunctionCodeMap.put("BDB", "呆帳");
        rptFunctionCodeMap.put("EAB", "立帳(併帳)");
    }

    public static String getRptFunctionName(String code) {
        return rptFunctionCodeMap.getOrDefault(code, "未知代碼");
    }
}