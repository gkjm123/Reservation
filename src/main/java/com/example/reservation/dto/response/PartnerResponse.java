package com.example.reservation.dto.response;

import com.example.reservation.entity.PartnerEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerResponse {
    private Long id;
    private String loginId;
    private String name;
    private String phone;
    private String email;
    private LocalDateTime signupDate;

    public static PartnerResponse fromEntity(PartnerEntity partnerEntity) {
        return PartnerResponse.builder()
                .id(partnerEntity.getId())
                .loginId(partnerEntity.getLoginId())
                .name(partnerEntity.getName())
                .phone(partnerEntity.getPhone())
                .email(partnerEntity.getEmail())
                .signupDate(partnerEntity.getSignupDate())
                .build();
    }
}
