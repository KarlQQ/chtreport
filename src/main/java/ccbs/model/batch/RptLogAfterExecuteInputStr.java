package ccbs.model.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RptLogAfterExecuteInputStr {
  @Schema(description = "報表記錄序碼", example = "") private String rptLogsId;

  @Schema(description = "重新執行識別", example = "Y") private String isRerun;

  @Schema(description = "報表代號", example = "") private String rptCode;

  @Schema(description = "產制人員", example = "") private String createEmpid;

  @Schema(description = "產制檔案筆數", example = "") private Integer createCount;

  @Schema(description = "錯誤筆數", example = "") private Integer errorCount;

  @Schema(description = "作業批號", example = "") private String opBatchno;

  @Schema(description = "報表內部參數", example = "") private String paramIntValuse;

  @Schema(description = "報表內部參數", example = "") private String paramExtValuse;

  @Schema(description = "**資料集Ｄ", example = "") private List<dData> dDataSet;

  public boolean isOpBatchnoNull() {
    return opBatchno == null;
  }

  public boolean isParamIntValuseNull() {
    return paramIntValuse == null;
  }

  public boolean isParamExtValuseNull() {
    return paramExtValuse == null;
  }
}
