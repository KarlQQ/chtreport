package ccbs.model.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BatchArrearsInputStr {
    @Schema(description = "作業批號", example = "RPT001")
    private String jobid;

    @Schema(description = "作業時間", example = "20241105")
    private String opcDate;

    @Schema(description = "重新執行識別", example = "Y")
    private String isRerun;

    @Schema(description = "資料種類", example = "1")
    private String inputType;

    @Schema(description = "費用類別", example = "G3")
    private String inputItem;
}
