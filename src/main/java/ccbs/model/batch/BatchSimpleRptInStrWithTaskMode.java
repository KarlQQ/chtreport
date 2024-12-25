package ccbs.model.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BatchSimpleRptInStrWithTaskMode extends BatchSimpleRptInStr {
    @Schema(description = "任務模式", example = "D")
    private String taskMode;
}
