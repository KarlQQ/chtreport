package ccbs.model.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class BPGUSUB_ReportRowForm {

//    BPGUSUB{1}_{220487231130729}.{pdf}
//    BPGUSUB{2}_{220487231130729}.{csv}
//    BPGUSUB{1}_{220487231130729}{ _MASK}.{pdf}
//    BPGUSUB{2}_{220487231130729 }{_MASK}.{csv}
//    BPGUSUB{TYPE}_{INPUT_FILE_NAME} {SECRET}. {FILE_TYPE}

    @Schema(description = "員工代號", example = "Y")
    private String empId;

    @Schema(description = "證號", example = "...")
    private String billIdno;

    @Schema(description = "代表號機構", example = "")
    private String billOff;

    @Schema(description = "代表號", example = "")
    private String billTel;

    @Schema(description = "出帳年月", example = "")
    private String billMonth;

    @Schema(description = "帳單識別", example = "")
    private String billId;

    @Schema(description = "帳單金額", example = "")
    private String billAmt;

    @Schema(description = "子號機構", example = "")
    private String subOff;

    @Schema(description = "子號", example = "")
    private String subTel;

    @Schema(description = "子號金額", example = "")
    private String subAmt;

    @Schema(description = "序號", example = "")
    private BigDecimal seqNo;

    @Schema(description = "繳費期限", example = "")
    private String payLimit;

    @Schema(description = "繳費方式", example = "")
    private String payType;

    @Schema(description = "收據號碼", example = "")
    private String receiptNo;

    @Schema(description = "變動載具號碼", example = "")
    private String invCarrierNo;

    public boolean isSubOffNull() {
        return subOff == null;
    }
    public boolean isSubTelNull() {
        return subTel == null;
    }
    public boolean isSubAmtNull() {
        return subAmt == null;
    }
}
