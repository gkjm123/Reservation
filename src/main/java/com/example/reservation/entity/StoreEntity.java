package com.example.reservation.entity;

import com.example.reservation.dto.form.StoreForm;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

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

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "storeEntity")
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
