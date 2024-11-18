package ccbs.model.online;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserInfoQueryIn {

    @Schema(description = "系統別", example = "")
    private String systemId;

    @Schema(description = "使用者ID", example = "")
    private String empId;

    @Schema(description = "發送時間", example = "YYYY-MM-DDTHH:mm:ss:sssZ")
    private String dateTime;

}
