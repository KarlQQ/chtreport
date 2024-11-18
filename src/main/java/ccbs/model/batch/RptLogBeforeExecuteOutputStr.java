package ccbs.model.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RptLogBeforeExecuteOutputStr {

    @Schema(description = "執行結果", example = "Y")
    private String procResult;

    @Schema(description = "執行結果說明", example = "")
    private String procDescription;

    @Schema(description = "報表記錄序碼", example = "")
    private String rptLogsId;

}
