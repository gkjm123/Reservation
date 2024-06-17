package com.example.reservation.dto.response;

import com.example.reservation.entity.StoreEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponse {
    private Long id;
    private String storeName;
    private String location;
    private String description;
    private String partnerName;
    private LocalDateTime createDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long tablePerTime;
    private List<ReviewResponse> reviewResponseList;

    public static StoreResponse fromEntity(StoreEntity storeEntity) {
        return StoreResponse.builder()
                .id(storeEntity.getId())
                .storeName(storeEntity.getStoreName())
                .location(storeEntity.getLocation())
                .description(storeEntity.getDescription())
                .partnerName(storeEntity.getPartnerEntity().getName())
                .createDate(storeEntity.getCreateDate())
                .startTime(storeEntity.getStartTime())
                .endTime(storeEntity.getEndTime())
                .tablePerTime(storeEntity.getTablePerTime())
                .build();
    }
}
