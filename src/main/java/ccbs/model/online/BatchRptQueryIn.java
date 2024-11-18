package ccbs.model.online;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BatchRptQueryIn {
    @Schema(description = "機構代號", example = "")
    private String billOff;

    @Schema(description = "處理日期(起)", example = "1130902")
    private String rptDateStart;

    @Schema(description = "處理日期(迄", example = "1130902")
    private String rptDateEnd;

    @Schema(description = "報表產製頻率", example = "")
    private String rptTimes;

    @Schema(description = "遮罩註記", example = "")
    private String rptSecretMark;

    @Schema(description = "報表種類", example = "")
    private String rptCategory;

    @Schema(description = "週期", example = "")
    private String rptPeriod;

    @Schema(description = "年月", example = "")
    private String rptYearMonth;

    public boolean isBillOffNull() {
        return billOff == null;
    }

    public boolean isRptTimesNull() {
        return rptTimes == null;
    }

    public boolean isRrptSecretMarkNull() {
        return rptSecretMark == null;
    }
}
