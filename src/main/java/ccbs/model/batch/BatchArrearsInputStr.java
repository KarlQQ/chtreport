package ccbs.model.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BatchArrearsInputStr {
    @Schema(description = "作業批號", example = "...")
    private String jobid;

    @Schema(description = "作業時間", example = "CYYYMMDD")
    private String opcDate;

    @Schema(description = "重新執行識別", example = "Y")
    private String isRerun;

}
