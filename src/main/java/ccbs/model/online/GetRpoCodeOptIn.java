package ccbs.model.online;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetRpoCodeOptIn {
  @Schema(description = "Function Code", example = "DBM") private String funCode;
}
