package ccbs.model.online;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ValidationOut {

    @Schema(description = "處理結果", example = "")
    private String result;

    @Schema(description = "回傳訊息", example = "")
    private String message;

    @Schema(description = "加密後的key", example = "")
    private String encryptKey;

}
