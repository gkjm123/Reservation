package com.example.reservation.dto.form;

import jakarta.validation.constraints.Email;
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
public class SignUpForm {
    @NotBlank(message = "아이디를 입력하세요.")
    private String loginId;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$", message = "소문자,대문자,숫자를 포함한 8~20자의 비밀번호를 입력해주세요.")
    private String password;

    @Pattern(regexp = "^010-([0-9]{4})-([0-9]{4})$", message = "010-xxxx-xxxx 형식으로 핸드폰 번호를 입력해주세요.")
    private String phone;

    @NotBlank(message = "이름을 입력하세요.")
    private String name;

    @Email
    private String email;

}
