package ccbs.model.online;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RptCategoryOut {
  @Schema(description = "", example = "") private String id;

  @Schema(description = "", example = "") private String code;

  @Schema(description = "", example = "") private String name;
}
