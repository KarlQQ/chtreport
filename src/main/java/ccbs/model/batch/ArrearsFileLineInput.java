package ccbs.model.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ArrearsFileLineInput {
    @Schema(description = "證號", example = "...")
    private String billIdno;

    @Schema(description = "機構", example = "Y")
    private String offCode;

    @Schema(description = "員工代號", example = "Y")
    private String empId;

    @Schema(description = "日期", example = "Y")
    private String fileDate;
}
