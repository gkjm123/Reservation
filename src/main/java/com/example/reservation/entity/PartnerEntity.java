package com.example.reservation.entity;

import com.example.reservation.dto.form.SignUpForm;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;
    private String password;

    private String name;
    private String phone;
    private String email;

    @CreationTimestamp
    private LocalDateTime signupDate;

    public static PartnerEntity fromForm(SignUpForm form) {
        return PartnerEntity.builder()
                .loginId(form.getLoginId())
                .name(form.getName())
                .phone(form.getPhone())
                .email(form.getEmail())
                .build();
    }
}
