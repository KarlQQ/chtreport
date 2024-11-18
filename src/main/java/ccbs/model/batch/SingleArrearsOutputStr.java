package ccbs.model.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SingleArrearsOutputStr {
    @Schema(description = "執行結果代碼", example = "00 or 99")
    private String procResult;

    @Schema(description = "執行結果說明", example = "")
    private String procDescription;

    @Schema(description = "證號", example = "")
    private String biiIdno;

    @Schema(description = "欠費數量", example = "")
    private String billCnt;

    @Schema(description = "代表號資料集", example = "")
    private List<IdnoData> IdnoDataSet;
}
