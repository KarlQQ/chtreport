package ccbs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "RPT_ITEM_TYPE_DETL")
@Data
public class RptItemTypeDetl {
  @Id @Column(name = "BILL_ITEM_CODE") private String billItemCode;

  @Column(name = "BILL_ITEM_TYPE") private String billItemType;
}