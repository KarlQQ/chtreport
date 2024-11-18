package ccbs.model.online;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RptCategoryOut {

    @Schema(description = "", example = "")
    private String id;

    @Schema(description = "", example = "")
    private String code;

    @Schema(description = "", example = "")
    private String name;

}
