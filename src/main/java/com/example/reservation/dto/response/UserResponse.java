package com.example.reservation.dto.response;

import com.example.reservation.entity.UserEntity;
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
public class UserResponse {

  private Long id;
  private String loginId;
  private String name;
  private String phone;
  private String email;
  private LocalDateTime signupDate;

  public static UserResponse fromEntity(UserEntity userEntity) {
    return UserResponse.builder()
        .id(userEntity.getId())
        .loginId(userEntity.getLoginId())
        .name(userEntity.getName())
        .phone(userEntity.getPhone())
        .email(userEntity.getEmail())
        .signupDate(userEntity.getSignupDate())
        .build();
  }
}
