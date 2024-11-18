package ccbs.model.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RptWatermarkSingleIn {
    @Schema(description = "員工編號", example = "048723")
    private String empID;

    @Schema(description = "目標檔案路徑", example = "")
    private String rptFilePath;

    @Schema(description = "目標檔名", example = "")
    private String rptFileName;

    @Schema(description = "加入浮水印後檔案路徑", example = "")
    private String watermarkRptFilePath;

    @Schema(description = "加入浮水印後後檔名", example = "")
    private String watermarkRptFileName;

}
