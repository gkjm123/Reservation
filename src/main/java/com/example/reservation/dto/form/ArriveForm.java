package com.example.reservation.dto.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

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

    @Pattern(regexp = "010-([0-9])([0-9])([0-9])([0-9])-([0-9])([0-9])([0-9])([0-9])", message = "전화번호를 010-xxxx-xxxx 타입으로 입력하세요.")
    private String userPhone;
}
