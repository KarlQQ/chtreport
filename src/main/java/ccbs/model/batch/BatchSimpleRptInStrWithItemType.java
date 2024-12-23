package ccbs.model.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BatchSimpleRptInStrWithItemType extends BatchSimpleRptInStr {
    @Schema(description = "費用代號類別", example = "G3")
    private String itemType;
}
