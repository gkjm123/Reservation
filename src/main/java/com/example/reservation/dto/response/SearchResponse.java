package com.example.reservation.dto.response;

import com.example.reservation.entity.StoreEntity;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    private Long storeId;
    private String storeName;
    private String location;

    public static SearchResponse fromEntity(StoreEntity storeEntity) {
        return SearchResponse.builder()
                .storeId(storeEntity.getId())
                .storeName(storeEntity.getStoreName())
                .location(storeEntity.getLocation())
                .build();
    }
}
