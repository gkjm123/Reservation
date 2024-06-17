package com.example.reservation.dto.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {
    @NotBlank(message = "아이디를 입력하세요.")
    private String loginId;

    @Length(min = 10, message = "10자리 이상의 비밀번호를 입력하세요.")
    private String password;

    @Pattern(regexp = "010-([0-9])([0-9])([0-9])([0-9])-([0-9])([0-9])([0-9])([0-9])", message = "전화번호를 010-xxxx-xxxx 타입으로 입력하세요.")
    private String phone;

    @NotBlank(message = "이름을 입력하세요.")
    private String name;

    @NotBlank(message = "이메일을 입력하세요.")
    private String email;

}
