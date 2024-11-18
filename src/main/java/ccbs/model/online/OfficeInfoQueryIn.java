package ccbs.model.online;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfficeInfoQueryIn {
  @Schema(description = "機構代號", example = "") private String officeCode;

  @Schema(description = "轉換模式", example = "") private String transType;
}
