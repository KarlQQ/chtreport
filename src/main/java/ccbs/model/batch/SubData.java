package ccbs.model.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SubData {
    @Schema(description = "子號機構", example = "")
    private String subOff;

    @Schema(description = "子號", example = "")
    private String subTel;

    @Schema(description = "子號金額", example = "")
    private String subAmt;

}
