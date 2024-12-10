package ccbs.model.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BatchSimpleRptInStr {
    @Schema(description = "作業批號", example = "RPT001")
    private String jobId;

    @Schema(description = "作業時間", example = "20241105")
    private String opcDate;

    @Schema(description = "資料年月", example = "202405")
    private String opcYearMonth;

    @Schema(description = "重新執行識別", example = "Y")
    private String isRerun;

    @Schema(description = "設備號碼前贅字", example = "EN07")
    private String type3;

}
