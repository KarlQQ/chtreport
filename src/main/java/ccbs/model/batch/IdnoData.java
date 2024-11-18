package ccbs.model.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class IdnoData {
    @Schema(description = "代表號機構", example = "")
    private String billOff;

    @Schema(description = "代表號", example = "")
    private String billTel;

    @Schema(description = "出帳年月", example = "")
    private String billMonth;

    @Schema(description = "帳單別", example = "")
    private String billId;

    @Schema(description = "帳單金額", example = "")
    private String billAmt;

    @Schema(description = "繳費期限", example = "")
    private String paylimit;

    @Schema(description = "繳費方式", example = "")
    private String paytype;

    @Schema(description = "收據號碼", example = "")
    private String receiptNo;

    @Schema(description = "變動載具號碼", example = "")
    private String invCarrierNo;

    @Schema(description = "含子號", example = "Y or N")
    private String withSubMark;

    @Schema(description = "子號筆數", example = "")
    private String subCnt;

    @Schema(description = "子號資料集", example = "")
    private List<SubData> SubDataSet;

    @Schema(description = "billCycle", example = "")
    private String billCycle;

}
