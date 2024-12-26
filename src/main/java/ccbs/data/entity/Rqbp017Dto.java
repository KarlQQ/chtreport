package ccbs.data.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityResult;
import jakarta.persistence.FieldResult;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Transient;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Entity
@NamedNativeQuery(name = "AnalysisReceivableArrears",
    query = "SELECT ACC_ITEM, ACC_NAME, JSON_OBJECTAGG(BILL_MONTH, BILL_SUM) AS AGG_DATA "
        + "FROM ( "
        + "  SELECT RAC.ACC_ITEM, AC.ACC_NAME, RAC.BILL_MONTH, SUM(RAC.BILL_ITEM_AMT) AS BILL_SUM "
        + "  FROM RPT_ACCOUNT RAC "
        + "  LEFT JOIN COMM_ACCOUNTING AC ON RAC.ACC_ITEM = CONCAT(CONCAT(AC.ACC_TYPE, '-'), AC.ACC_CODE) "
        + "  WHERE RAC.DEBT_MARK NOT IN ('Q','Y') AND SUBSTR(RAC.BILL_MONTH,0,3) BETWEEN :startBillYear AND :endBillYear "
        + "  GROUP BY RAC.ACC_ITEM, AC.ACC_NAME, RAC.BILL_MONTH"
        + ") "
        + "GROUP BY ACC_ITEM, ACC_NAME "
        + "ORDER BY ACC_ITEM",
    resultSetMapping = "Rqbp017DtoMapping")
@SqlResultSetMapping(name = "Rqbp017DtoMapping",
    entities =
    {
      @EntityResult(entityClass = Rqbp017Dto.class, fields = {
        @FieldResult(column = "ACC_ITEM", name = "accItem")
        , @FieldResult(column = "ACC_NAME", name = "accName"),
            @FieldResult(column = "AGG_DATA", name = "aggregatedData")
      })
    })
@Data
public class Rqbp017Dto {
  @Id private String accItem;
  @Id private String accName;
  private String aggregatedData;
  @Transient private Map<String, BigDecimal> data = new HashMap<String, BigDecimal>();

  @SuppressWarnings("unchecked")
  public void setAggregatedData(String aggregatedData)
      throws JsonMappingException, JsonProcessingException {
    this.aggregatedData = aggregatedData;
    this.data = new ObjectMapper().readValue(this.aggregatedData, Map.class);
  }
}
