package ccbs.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "RPT_TEL_TYPE")
@Data
public class RptTelType {
  @Id @Column(name = "RPT_TEL_TYPE", length = 2) private String rptTelType;
  @Column(name = "RPT_TEL_NAME", length = 80) private String rptTelName;
}
