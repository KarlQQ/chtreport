package ccbs.model.online;

//import com.aspose.cells.DateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserData {
    @JsonProperty("LDAP_UID")
    @Schema(description = "LDAP帳號", example = "")
    private String LDAP_UID;

    @JsonProperty("EMP_ID")
    @Schema(description = "員工代號", example = "")
    private String EMP_ID;

    @JsonProperty("EMP_NAME")
    @Schema(description = "姓名", example = "")
    private String EMP_NAME;

    @JsonProperty("EMP_DEP")
    @Schema(description = "五級單位代碼", example = "")
    private String[] EMP_DEP;

    @JsonProperty("EMP_DEPNAME")
    @Schema(description = "五級單位名稱", example = "")
    private String[] EMP_DEPNAME;

    @JsonProperty("EMP_MAIL")
    @Schema(description = "Email", example = "")
    private String EMP_MAIL;

    @JsonProperty("EMP_TEL")
    @Schema(description = "聯絡電話", example = "")
    private String EMP_TEL;

    @JsonProperty("EMP_FAX")
    @Schema(description = "傳真電話", example = "")
    private String EMP_FAX;

    @JsonProperty("EMP_MOBILE")
    @Schema(description = "行動電話", example = "")
    private String EMP_MOBILE;

    @JsonProperty("EMP_STATUS")
    @Schema(description = "員工別(員工或委外)", example = "")
    private String EMP_STATUS;

//    對應 CCBS.OFFICE，如資分為 15，台北營運處為 22
    @JsonProperty("EMP_OFFICE")
    @Schema(description = "所offic處代號", example = "")
    private String EMP_OFFICE;

    @JsonProperty("EMP_AGENT")
    @Schema(description = "經收處代號", example = "")
    private String EMP_AGENT;

    @JsonProperty("EMP_DEPTDESP")
    @Schema(description = "單位中文全稱", example = "")
    private String EMP_DEPTDESP;

    @JsonProperty("EMP_ADMIN")
    @Schema(description = "所屬分公司", example = "")
    private String EMP_ADMIN;

    @JsonProperty("EMP_TITLE")
    @Schema(description = "職稱代碼", example = "")
    private String EMP_TITLE;

    @JsonProperty("EMP_TITLEDESP")
    @Schema(description = "職稱", example = "")
    private String EMP_TITLEDESP;

    @JsonProperty("LOGINIP")
    @Schema(description = "登入時IP", example = "")
    private String LOGINIP;

    @JsonProperty("RACF_UID")
    @Schema(description = "RACF帳號", example = "")
    private String RACF_UID;

    @JsonProperty("SERVER_ADMIN")
    @Schema(description = "登入時的CCBSWebServerAdmin", example = "")
    private String SERVER_ADMIN;

    @JsonProperty("SERVER_NAME")
    @Schema(description = "登入時的CCBSWebServeName", example = "")
    private String SERVER_NAME;

    @JsonProperty("OPLEVEL")
    @Schema(description = "一線或二線人員", example = "")
    private String OPLEVEL;

    @JsonProperty("USER_STATUS")
    @Schema(description = "使用者狀態", example = "")
    private String USER_STATUS;

    @JsonProperty("LAST_UP_DATER")
    @Schema(description = "上次權限異動者", example = "")
    private String LAST_UP_DATER;

    @JsonProperty("USER_EXPIRETIME")
    @Schema(description = "帳號到期時間", example = "")
    private String USER_EXPIRETIME;

    @JsonProperty("LAST_LOGIN_TIME")
    @Schema(description = "上次登入時間", example = "")
    private String LAST_LOGIN_TIME;

    @JsonProperty("LOGIN_EXPIRE_TIME")
    @Schema(description = "使用者登入到期時間", example = "")
    private String LOGIN_EXPIRE_TIME;

    @JsonProperty("LAST_UPDATE_TIME")
    @Schema(description = "上次權限異動時間", example = "")
    private String LAST_UPDATE_TIME;

    @JsonProperty("IS_SUPER_SM")
    @Schema(description = "是否為超級管理員", example = "")
    private boolean IS_SUPER_SM;

    @JsonProperty("IS_SM")
    @Schema(description = "是否為一般管理員", example = "")
    private boolean IS_SM;

    @JsonProperty("SESSIONTIME")
    @Schema(description = "WebSession 的持續時間", example = "")
    private int SESSIONTIME;
}
