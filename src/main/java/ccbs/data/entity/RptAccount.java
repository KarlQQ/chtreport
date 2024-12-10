package ccbs.data.entity;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@NamedEntityGraph(
    name = "RptAccount.withItemType", attributeNodes = { @NamedAttributeNode("rptItemTypeDetl") })
@Table(name = "RPT_ACCOUNT")
@Data
public class RptAccount {
  @Id @Column(name = "BILL_OFF_BELONG") private String billOffBelong;

  @Column(name = "BILL_OFF") private String billOff;

  @Column(name = "BILL_MONTH") private String billMonth;

  @Column(name = "DEBT_MARK") private String debtMark;

  @Column(name = "TAX_PRINT_ID") private String taxPrintId;

  @Column(name = "RPT_CUST_TYPE") private String rptCustType;

  @Column(name = "BU_GROUP_MARK", length = 1) private String buGroupMark;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ACC_ITEM", referencedColumnName = "accItem", insertable = false, updatable = false)
  private AccountingView accounting;

  @Column(name = "RCV_ITEM", length = 12) private String rcvItem;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BILL_ITEM_CODE", referencedColumnName = "BILL_ITEM_CODE", insertable = false,
      updatable = false)
  private RptItemTypeDetl rptItemTypeDetl;

  @Column(name = "BILL_ITEM_AMT", precision = 12, scale = 0) private BigDecimal billItemAmt;

  @Column(name = "BILL_ITEM_TAX_AMT", precision = 9, scale = 0) private BigDecimal billItemTaxAmt;

  @Column(name = "BILL_ITEM_SALE_AMT", precision = 10, scale = 0)
  private BigDecimal billItemSaleAmt;

  @Column(name = "BILL_ITEM_ZERO_TAX_AMT", precision = 10, scale = 0)
  private BigDecimal billItemZeroTaxAmt;

  @Column(name = "BILL_ITEM_NO_TAX_AMT", precision = 10, scale = 0)
  private BigDecimal billItemNoTaxAmt;

  @Column(name = "BILL_ITEM_FREE_TAX_AMT", precision = 10, scale = 0)
  private BigDecimal billItemFreeTaxAmt;
}