package ccbs.model.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BPGNIDFileLineInput {
    @Schema(description = "機構所屬", example = "...")
    private String lbOffBelong;

    @Schema(description = "填充", example = "...")
    private String lbFiller;

    @Schema(description = "證號", example = "...")
    private String lbIdno;

    @Schema(description = "證號Mask", example = "...")
    private String lbIdnoMask;

    @Schema(description = "機構", example = "...")
    private String lbOff;

    @Schema(description = "電話", example = "...")
    private String lbTel;

    @Schema(description = "電話Mask", example = "...")
    private String lbTelMask;

    @Schema(description = "安裝日期", example = "...")
    private String lbInstallDate;

    @Schema(description = "帳單名稱", example = "...")
    private String lbBillName;

    @Schema(description = "郵遞區號", example = "...")
    private String lbZipCode;

    @Schema(description = "地址", example = "...")
    private String lbAdr;

    @Schema(description = "員工代號", example = "...")
    private String lbEmpId;

    @Schema(description = "ADSL交換機", example = "...")
    private String lbAdslExg;
}
