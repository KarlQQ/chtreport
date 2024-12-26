package ccbs.model.online;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Rqbp019Input extends BaseInput {
  @Schema(description = "資料檔來源", example = "1", allowableValues = {"1", "2"})
  private String inputType;
  @Schema(description = "設備字頭參數", example = "EN") private String inputSCAN;
  @Schema(description = "下載營運處", example = "4040") private String inputOFF;
}
