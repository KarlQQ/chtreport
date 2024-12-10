package ccbs.model.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BatchSimpleRptInStrWithType extends BatchSimpleRptInStr {
    @Schema(description = "設備號碼前贅字", example = "EN07")
    private String type3;
}
