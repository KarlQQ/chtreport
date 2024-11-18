package ccbs.model.online;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserInfoQueryApiIn {
    @Schema(description = "系統別", example = "")
    private String SYSTEM_ID;

    @Schema(description = "使用者ID", example = "")
    private String EMP_ID;

    @Schema(description = "發送時間", example = "1130902")
    private String DATETIME;

}
