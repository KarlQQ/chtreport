package ccbs.model.online;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OffAdminOptionsOut {

    @Schema(description = "處理結果", example = "")
    private String result;

    @Schema(description = "回傳訊息", example = "")
    private String message;

    @Schema(description = "", example = "")
    private List<String> rptOptionList;
}
