package com.example.reservation.dto.form;

import jakarta.validation.constraints.NotBlank;
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
public class SignInForm {

  @NotBlank(message = "아이디를 입력하세요.")
  private String loginId;

  @NotBlank(message = "비밀번호를 입력하세요.")
  private String password;
}
