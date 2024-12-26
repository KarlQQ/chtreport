package ccbs.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "RPT_TEL_TYPE_DETL")
@Data
public class RptTelTypeDetl {
  @Id @Column(name = "BILL_OPID_RULE", length = 2) private String billOpidRule;
  @Column(name = "PATTERN", length = 100) private String pattern;
}
