package com.example.reservation.dto.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArriveForm {

  @Min(value = 1, message = "상점 아이디를 입력하세요.")
  private Long storeId;

  @NotBlank(message = "이름을 입력하세요.")
  private String userName;

  @Pattern(regexp = "^010-([0-9]{4})-([0-9]{4})$", message = "010-xxxx-xxxx 형식으로 핸드폰 번호를 입력해주세요.")
  private String userPhone;
}
