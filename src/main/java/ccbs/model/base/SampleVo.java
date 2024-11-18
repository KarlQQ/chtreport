package ccbs.model.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>Sample</p> mapping object
 */
@Getter
@Setter
@ToString
@Schema(description = "Sample model")
public class SampleVo {
    @Schema(description = "序號", example = "01")
    private BigDecimal id;
    @Schema(description = "User ID", example = "John Wick")
    private String userId;
    @Schema(description = "User Name", example = "Any text")
    private String userName;
    @Schema(description = "建立時間", example = "1647249912782")
    private Date createDate;
}