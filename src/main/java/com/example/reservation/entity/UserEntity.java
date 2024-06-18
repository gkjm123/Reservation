package com.example.reservation.entity;

import com.example.reservation.dto.form.SignUpForm;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;
    private String password;

    private String name;
    private String phone;
    private String email;

    private String role;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return "";
    }
}
