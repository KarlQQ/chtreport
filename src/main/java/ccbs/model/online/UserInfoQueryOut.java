package ccbs.model.online;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserInfoQueryOut {
    @JsonProperty("PROC_RESULT")
    @Schema(description = "處理結果", example = "")
    private String PROC_RESULT;

    @JsonProperty("Message")
    @Schema(description = "回傳訊息", example = "")
    private String Message;

    @JsonProperty("USER_DATA")
    @Schema(description = "", example = "")
    private UserData USER_DATA;
}
