package ccbs.model.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BatchSimpleRptInStrWithOpid extends BatchSimpleRptInStr {
    @Schema(description = "篩選名次數量", example = "10")
    private String keepCnt;

    @Schema(description = "篩選業物別", example = "")
    private String inputOPID;

    @Schema(description = "設備狀態", example = "")
    private String inputSTATUS;
}
