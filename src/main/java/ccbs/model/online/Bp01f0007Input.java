package ccbs.model.online;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Bp01f0007Input extends BaseInput {
  @Schema(description = "資料檔來源", example = "1", allowableValues = {"1", "2"})
  private String inputType;
}
