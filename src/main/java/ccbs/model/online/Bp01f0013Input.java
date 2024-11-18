package ccbs.model.online;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bp01f0013Input {
  @Schema(description = "作業批號", example = "2001") private String jobId;

  @Schema(description = "作業時間", example = "20241006") private String opcDate;

  @Schema(description = "資料年月", example = "202410") private String opcYearMonth;

  @Schema(description = "重新執行識別", example = "Y") private String isRerun;
}
