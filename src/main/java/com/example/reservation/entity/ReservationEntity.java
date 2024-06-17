package com.example.reservation.entity;

import com.example.reservation.type.ReservationType;
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
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private StoreEntity storeEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity userEntity;

    private LocalDateTime reserveDate;

    @Enumerated(value = EnumType.STRING)
    private ReservationType status;

    @CreationTimestamp
    private LocalDateTime createDate;
}
