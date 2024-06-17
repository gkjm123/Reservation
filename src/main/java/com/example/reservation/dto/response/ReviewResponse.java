package com.example.reservation.dto.response;

import com.example.reservation.entity.ReviewEntity;
import lombok.*;

import java.time.LocalDateTime;

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
