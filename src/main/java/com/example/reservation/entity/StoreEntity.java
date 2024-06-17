package com.example.reservation.entity;

import com.example.reservation.dto.form.StoreForm;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String storeName;
    private String location;
    private String description;

    private LocalTime startTime;
    private LocalTime endTime;
    private Long tablePerTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private PartnerEntity partnerEntity;

    @OneToMany(fetch = FetchType.LAZY)
    private List<ReviewEntity> reviewEntityList;

    @CreationTimestamp
    private LocalDateTime createDate;

    public static StoreEntity fromForm(StoreForm storeForm) {
        return StoreEntity.builder()
                .storeName(storeForm.getStoreName())
                .location(storeForm.getLocation())
                .description(storeForm.getDescription())
                .startTime(storeForm.getStartTime())
                .endTime(storeForm.getEndTime())
                .tablePerTime(storeForm.getTablePerTime())
                .build();
    }

}
