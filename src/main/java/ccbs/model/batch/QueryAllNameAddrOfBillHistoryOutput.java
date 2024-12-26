package ccbs.model.batch;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "EAIRSP")
public class QueryAllNameAddrOfBillHistoryOutput {
  @XmlElement(name = "Status") private String status;

  @XmlElement(name = "Msg") private String message;

  @XmlElement(name = "區分公司") private String companyDivision;

  @XmlElement(name = "機構代號") private String officeCode;

  @XmlElement(name = "設備號碼") private String equipmentNumber;

  @XmlElement(name = "總筆數") private String totalCount;

  @XmlElement(name = "ListItem") private List<ListItem> listItems;

  // Getter 和 Setter 方法
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getCompanyDivision() {
    return companyDivision;
  }

  public void setCompanyDivision(String companyDivision) {
    this.companyDivision = companyDivision;
  }

  public String getOfficeCode() {
    return officeCode;
  }

  public void setOfficeCode(String officeCode) {
    this.officeCode = officeCode;
  }

  public String getEquipmentNumber() {
    return equipmentNumber;
  }

  public void setEquipmentNumber(String equipmentNumber) {
    this.equipmentNumber = equipmentNumber;
  }

  public String getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(String totalCount) {
    this.totalCount = totalCount;
  }

  public List<ListItem> getListItems() {
    return listItems;
  }

  public void setListItems(List<ListItem> listItems) {
    this.listItems = listItems;
  }

  public static class ListItem {
    public ListItem() {
      // 無參數構造函數
    }

    @XmlElement(name = "出帳年月") private String billMonth;

    @XmlElement(name = "帳單別") private String billType;

    @XmlElement(name = "出帳證號") private String billCertificateNumber;

    @XmlElement(name = "帳寄名稱") private String billName;

    @XmlElement(name = "郵遞區號") private String postalCode;

    @XmlElement(name = "帳寄地址") private String billAddress;

    @XmlElement(name = "名址類別") private String addressType;

    // Getter 和 Setter 方法
    public String getBillMonth() {
      return billMonth;
    }

    public void setBillMonth(String billMonth) {
      this.billMonth = billMonth;
    }

    public String getBillType() {
      return billType;
    }

    public void setBillType(String billType) {
      this.billType = billType;
    }

    public String getBillCertificateNumber() {
      return billCertificateNumber;
    }

    public void setBillCertificateNumber(String billCertificateNumber) {
      this.billCertificateNumber = billCertificateNumber;
    }

    public String getBillName() {
      return billName;
    }

    public void setBillName(String billName) {
      this.billName = billName;
    }

    public String getPostalCode() {
      return postalCode;
    }

    public void setPostalCode(String postalCode) {
      this.postalCode = postalCode;
    }

    public String getBillAddress() {
      return billAddress;
    }

    public void setBillAddress(String billAddress) {
      this.billAddress = billAddress;
    }

    public String getAddressType() {
      return addressType;
    }

    public void setAddressType(String addressType) {
      this.addressType = addressType;
    }
  }
}