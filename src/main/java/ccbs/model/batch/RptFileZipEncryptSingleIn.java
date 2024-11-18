package ccbs.model.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RptFileZipEncryptSingleIn {
    @Schema(description = "員工編號", example = "048723")
    private String empID;

    @Schema(description = "目標檔名含路徑", example = "")
    private String rptFilePathAndName;

//    @Schema(description = "目標檔名", example = "")
//    private String rptFileName;

    @Schema(description = "壓縮檔名含路徑", example = "")
    private String zipRptFilePathAndName;

//    @Schema(description = "壓縮檔檔名", example = "")
//    private String zipRptFileName;

}
