package ccbs.data.entity;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "EAIRSP")
public class QueryAllNameAddrOfBillHistoryInput {

    private String employeeId;
    private String companyDivision;
    private String institutionCode;
    private String deviceNumber;
    private String billType;
    private String billingMonthStart;
    private String billingMonthEnd;
    private String billingCertificateNumber;
    private String remittanceNumber;
    private String systemType;
    private String loginIP;
    private String loginDate;
    private String loginTime;

    @XmlElement(name = "員工代號")
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @XmlElement(name = "區分公司")
    public String getCompanyDivision() {
        return companyDivision;
    }

    public void setCompanyDivision(String companyDivision) {
        this.companyDivision = companyDivision;
    }

    @XmlElement(name = "機構代號")
    public String getInstitutionCode() {
        return institutionCode;
    }

    public void setInstitutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
    }

    @XmlElement(name = "設備號碼")
    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    @XmlElement(name = "帳單別")
    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    @XmlElement(name = "出帳年月起")
    public String getBillingMonthStart() {
        return billingMonthStart;
    }

    public void setBillingMonthStart(String billingMonthStart) {
        this.billingMonthStart = billingMonthStart;
    }

    @XmlElement(name = "出帳年月迄")
    public String getBillingMonthEnd() {
        return billingMonthEnd;
    }

    public void setBillingMonthEnd(String billingMonthEnd) {
        this.billingMonthEnd = billingMonthEnd;
    }

    @XmlElement(name = "出帳證號")
    public String getBillingCertificateNumber() {
        return billingCertificateNumber;
    }

    public void setBillingCertificateNumber(String billingCertificateNumber) {
        this.billingCertificateNumber = billingCertificateNumber;
    }

    @XmlElement(name = "彙寄編號")
    public String getRemittanceNumber() {
        return remittanceNumber;
    }

    public void setRemittanceNumber(String remittanceNumber) {
        this.remittanceNumber = remittanceNumber;
    }

    @XmlElement(name = "系統別")
    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    @XmlElement(name = "登入IP")
    public String getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
    }

    @XmlElement(name = "登入日期")
    public String getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }

    @XmlElement(name = "登入時間")
    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }
}