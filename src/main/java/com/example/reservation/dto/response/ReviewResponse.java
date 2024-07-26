package com.example.reservation.dto.response;

import com.example.reservation.entity.ReviewEntity;
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
public class ReviewResponse {

  private Long id;
  private Long storeId;
  private Long userId;
  private String content;
  private LocalDateTime reserveDate;
  private LocalDateTime createDate;

  public static ReviewResponse fromEntity(ReviewEntity reviewEntity) {
    return ReviewResponse.builder()
        .id(reviewEntity.getId())
        .storeId(reviewEntity.getStoreEntity().getId())
        .userId(reviewEntity.getUserEntity().getId())
        .content(reviewEntity.getContent())
        .reserveDate(reviewEntity.getReserveDate())
        .createDate(reviewEntity.getCreateDate())
        .build();
  }
}
