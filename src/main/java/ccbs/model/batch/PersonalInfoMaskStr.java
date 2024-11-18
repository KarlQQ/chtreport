package ccbs.model.batch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInfoMaskStr {
  @Schema(description = "姓名", example = "") private String maskFirstName;

  @Schema(description = "證號", example = "Y") private String maskIDNumber;

  @Schema(description = "地址", example = "") private String maskAddress;

  @Schema(description = "銀行帳號或信用卡號碼", example = "") private String maskBankAccount;

  @Schema(description = "設備號碼", example = "") private String maskTelno;

  public boolean isMaskFirstNameNull() {
    return maskFirstName == null;
  }
  public boolean isMaskIDNumberNull() {
    return maskIDNumber == null;
  }
  public boolean isMaskAddressNull() {
    return maskAddress == null;
  }
  public boolean isMaskBankAccountNull() {
    return maskBankAccount == null;
  }
  public boolean isMaskTelnoNull() {
    return maskTelno == null;
  }
}
