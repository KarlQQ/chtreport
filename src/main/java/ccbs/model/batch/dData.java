package ccbs.model.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
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
public class dData {
  @Schema(description = "報表檔名", example = "") private String rptFileName;

  @Schema(description = "機構代號", example = "") private String billOff;

  @Schema(description = "報表產制頻率", example = "") private String rptTimes;

  @Schema(description = "報表產制年月", example = "") private String billMonth;

  @Schema(description = "出帳週期", example = "") private String billCycle;

  @Schema(description = "報表資料日期", example = "") private String rptDate;

  @Schema(description = "報表季度", example = "") private String rptQuarter;

  @Schema(description = "設備所屬區管局", example = "") private String offAdmin;

  @Schema(description = "報表檔資料筆數", example = "") private Integer rptFileCount;

  @Schema(description = "報表檔總金額", example = "") private BigDecimal rptFileAmt;

  @Schema(description = "報表機敏識別", example = "") private String rptSecretMark;
}
