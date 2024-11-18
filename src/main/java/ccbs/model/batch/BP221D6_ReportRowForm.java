package ccbs.model.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ccbs.dao.core.entity.RptAccountSummary;
import ccbs.util.comm01.Comm01Service;


@Getter
@Setter
@ToString
public class BP221D6_ReportRowForm {

    // BP221D6_T{OPC_YYYMM}.CSV
    // 營運處年度欠費統計表

    // BP221D6_GSM_T{OPC_YYYMM}.CSV
    // 行動業務營運處年度欠費統計表
    
    // BP221D6(X-2)_T(OPC_YYYMM).CSV
    // 營運處逾期一月欠費統計表
    
    // BP221D6_ORG_T{OPC_YYYMM}.CSV
    // 排除特定代收業務年度欠費統計表
    
    @Schema(description = "營運處機構代碼", example = "22")
    private String offCode;

    @Schema(description = "營運處機構名稱", example = "國際分公司")
    private String offName;

    @Schema(description = "非呆帳-統計至執行日的前2個月(含該月)", example = "0")
    private BigDecimal aNonBadDebt;

    @Schema(description = "呆帳-統計至執行日的前2個月(含該月)", example = "0")
    private BigDecimal aBadDebt;

    @Schema(description = "小記-統計至執行日的前2個月(含該月)", example = "0")
    private BigDecimal aSubtotal;

    @Schema(description = "非呆帳-統計至執行日的前1個月(含該月)", example = "0")
    private BigDecimal bNonBadDebt;

    @Schema(description = "呆帳-統計至執行日的前1個月(含該月)", example = "0")
    private BigDecimal bBadDebt;

    @Schema(description = "小記-統計至執行日的前1個月(含該月)", example = "0")
    private BigDecimal bSubtotal;

    @Schema(description = "非呆帳-單獨統計執行日的前1年該年度", example = "0")
    private BigDecimal cNonBadDebt;

    @Schema(description = "呆帳-單獨統計執行日的前1年該年度", example = "0")
    private BigDecimal cBadDebt;

    @Schema(description = "小記-單獨統計執行日的前1年該年度", example = "0")
    private BigDecimal cSubtotal;

    @Schema(description = "非呆帳-統計至執行日的前2年(含該年度)", example = "0")
    private BigDecimal dNonBadDebt;

    @Schema(description = "呆帳-統計至執行日的前2年(含該年度)", example = "0")
    private BigDecimal dBadDebt;

    @Schema(description = "小記-統計至執行日的前2年(含該年度)", example = "0")
    private BigDecimal dSubtotal;

    @Schema(description = "非呆帳-單獨統計執行日的前1年度的十一月", example = "0")
    private BigDecimal eNonBadDebt;

    @Schema(description = "呆帳-單獨統計執行日的前1年度的十一月", example = "0")
    private BigDecimal eBadDebt;

    @Schema(description = "小記-單獨統計執行日的前1年度的十一月", example = "0")
    private BigDecimal eSubtotal;

    @Schema(description = "非呆帳-單獨統計執行日的前1年度的十二月", example = "0")
    private BigDecimal fNonBadDebt;

    @Schema(description = "呆帳-單獨統計執行日的前1年度的十二月", example = "0")
    private BigDecimal fBadDebt;

    @Schema(description = "小記-單獨統計執行日的前1年度的十二月", example = "0")
    private BigDecimal fSubtotal;


    public void setFromOffNames(RptAccountSummary summary) {
        this.offName = summary.getOffName();
    }

    public void setFromAccountSummary(String offType, String dateType, RptAccountSummary summary) {
        if (offType != "D") {
            if (dateType == "a") {
                this.aNonBadDebt = summary.getNonBadDebt();
                this.aBadDebt = summary.getBadDebt();
                this.aSubtotal = summary.getSubtotal();           
            } else if (dateType == "b") {
                this.bNonBadDebt = summary.getNonBadDebt();
                this.bBadDebt = summary.getBadDebt();
                this.bSubtotal = summary.getSubtotal();
            } else if (dateType == "c") {
                this.cNonBadDebt = summary.getNonBadDebt();
                this.cBadDebt = summary.getBadDebt();
                this.cSubtotal = summary.getSubtotal();
            } else if (dateType == "d") {
                this.dNonBadDebt = summary.getNonBadDebt();
                this.dBadDebt = summary.getBadDebt();
                this.dSubtotal = summary.getSubtotal();
            } else if (dateType == "e") {
                this.eNonBadDebt = summary.getNonBadDebt();
                this.eBadDebt = summary.getBadDebt();
                this.eSubtotal = summary.getSubtotal();
            } else if (dateType == "f") {
                this.fNonBadDebt = summary.getNonBadDebt();
                this.fBadDebt = summary.getBadDebt();
                this.fSubtotal = summary.getSubtotal();
            }
        } else {
            if (dateType == "a") {
                this.aNonBadDebt = summary.getNonBadDebt();
                this.aBadDebt = summary.getBadDebt();
                this.aSubtotal = summary.getSubtotal();           
            }
        }
        
        
    }
}
