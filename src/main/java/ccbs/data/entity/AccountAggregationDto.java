package ccbs.data.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class AccountAggregationDto {
  private String accItem;

  private String accName;

  private String aggregatedData;

  private Map<String, BigDecimal> data = new HashMap<String, BigDecimal>();

  @SuppressWarnings("unchecked")
  public AccountAggregationDto(String accItem, String accName, String aggregatedData) {
    this.accItem = accItem;
    this.accName = accName;
    this.aggregatedData = aggregatedData;
    try {
      this.data = new ObjectMapper().readValue(this.aggregatedData, Map.class);
    } catch (JsonProcessingException e) {
      log.error(String.format("error parse aggregatedData: %s", aggregatedData), e);
    }
  }

  public AccountAggregationDto(AccountAggregationProjection projection) {
    this(projection.getAccItem(), projection.getAccName(), projection.getAggregatedData());
  }
}
