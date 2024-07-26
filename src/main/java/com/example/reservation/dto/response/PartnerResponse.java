package com.example.reservation.dto.response;

import com.example.reservation.entity.PartnerEntity;
import java.time.LocalDateTime;
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
