package ccbs.model.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddressVo {
    @Schema(description = "四段式地址(郵遞區號)")
    private String zipcode;
    @Schema(description = "四段式地址(縣市)")
    private String address1;
    @Schema(description = "四段式地址(鄉鎮)")
    private String address2;
    @Schema(description = "四段式地址(路段)")
    private String address3;
    @Schema(description = "四段式地址(巷弄)")
    private String address4;
}
