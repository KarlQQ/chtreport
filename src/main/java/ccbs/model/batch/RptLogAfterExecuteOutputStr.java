package ccbs.model.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RptLogAfterExecuteOutputStr {

    @Schema(description = "執行結果", example = "00 or QQ")
    private String procResult;

    @Schema(description = "執行結果說明", example = "")
    private String procDescription;

}
