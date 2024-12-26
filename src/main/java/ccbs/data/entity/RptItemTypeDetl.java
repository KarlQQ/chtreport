package ccbs.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "RPT_ITEM_TYPE_DETL")
@Data
public class RptItemTypeDetl {
  @Id @Column(name = "BILL_ITEM_CODE") private String billItemCode;
  @Column(name = "BILL_ITEM_TYPE") private String billItemType;
}