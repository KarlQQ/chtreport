package ccbs.model.online;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OfficeInfoQueryOut {

    @Schema(description = "轉換結果代碼", example = "")
    private String resultStatus;

    @Schema(description = "轉換後機構代號", example = "")
    private String resultOfficeCode;

    @Schema(description = "轉換後中文名稱", example = "")
    private String resultOfficeCN;
}
