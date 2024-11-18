package ccbs.model.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SingleArrearsInputStr {
    @Schema(description = "證號", example = "...")
    private String billIdno;

    @Schema(description = "查子號識別", example = "Y")
    private String getSubMark;
}
