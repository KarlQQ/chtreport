package ccbs.model.online;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BatchRptQueryOut {
    @Schema(description = "檔名", example = "")
    private String rptFileName;

    @Schema(description = "報表名稱", example = "")
    private String rptName;

    @Schema(description = "日期", example = "")
    private String rptDate;

    @Schema(description = "筆數", example = "")
    private String rptFileCount;

    @Schema(description = "金額", example = "")
    private String rptAmt;

    @Schema(description = "報表狀態", example = "")
    private String rptStatus;

    @Schema(description = "遮罩註記", example = "")
    private String rptSecretMark;

    @Schema(description = "", example = "")
    private String rptLogsId;

}
