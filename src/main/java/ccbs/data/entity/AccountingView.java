package ccbs.data.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.Subselect;

@Entity
@Subselect(
    "SELECT ACC_NAME, ACC_TYPE, ACC_CODE, (ACC_TYPE || '-' || ACC_CODE) AS ACC_ITEM FROM COMM_ACCOUNTING")
@Data
public class AccountingView {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  private String accName;
  private String accType;
  private String accCode;
  private String accItem;
}