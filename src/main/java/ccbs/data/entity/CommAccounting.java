package ccbs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Formula;

@Entity
@Table(name = "COMM_ACCOUNTING")
@Data
public class CommAccounting {
  @Id @Column(name = "SYSTEM_ID", length = 1) private String systemId;

  @Column(name = "EXPENSES_ID", length = 3) private String expensesId;

  @Column(name = "COLLECT_ID", length = 1) private String collectId;

  @Column(name = "BRANCH_FLAG", length = 1) private String branchFlag;

  @Column(name = "COLLECT_NAME", length = 80) private String collectName;

  @Column(name = "EXPENSES_NAME", length = 80) private String expensesName;

  @Column(name = "ACC_TYPE", length = 4) private String accType;

  @Column(name = "ACC_CODE", length = 5) private String accCode;

  @Column(name = "ACC_NAME", length = 80) private String accName;

  @Column(name = "REMARKS", length = 20) private String remarks;

  @Column(name = "DEBIT", length = 1) private String debit;

  @Column(name = "TAX_ID", length = 1) private String taxId;

  @Column(name = "OFFICE_ICMS", length = 4) private String officeIcms;

  @Column(name = "ADMIN", length = 1) private String admin;

  @Column(name = "TAX_TYPE", length = 1) private String taxType;

  @Column(name = "PROD_OWNER", length = 1) private String prodOwner;

  @Formula("acc_type || '-' || acc_code") private String accItem;
}