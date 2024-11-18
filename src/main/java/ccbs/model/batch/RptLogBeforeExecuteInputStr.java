package ccbs.model.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RptLogBeforeExecuteInputStr {
  @Schema(description = "報表代號", example = "") private String rptCode;
}
