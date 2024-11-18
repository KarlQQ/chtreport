package ccbs.model.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RptWatermarkSingleOut {
    @Schema(description = "處理結果", example = "")
    private Boolean codeResult;
}
