package com.example.reservation.entity;

import com.example.reservation.dto.form.SignUpForm;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
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

    public static UserEntity fromForm(SignUpForm form) {
        return UserEntity.builder()
                .loginId(form.getLoginId())
                .name(form.getName())
                .phone(form.getPhone())
                .email(form.getEmail())
                .build();
    }
}
