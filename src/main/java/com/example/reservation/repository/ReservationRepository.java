package com.example.reservation.repository;

import com.example.reservation.entity.ReservationEntity;
import com.example.reservation.entity.StoreEntity;
import com.example.reservation.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findAllByUserEntity_IdAndStoreEntity_Id(Long userId, Long storeId);
    List<ReservationEntity> findAllByStoreEntity_IdAndReserveDate(Long storeId, LocalDateTime reserveDate);
    List<ReservationEntity> findAllByUserEntity(UserEntity userEntity);
    List<ReservationEntity> findAllByStoreEntity(StoreEntity storeEntity);
    List<ReservationEntity> findAllByUserEntityAndStoreEntity(UserEntity userEntity, StoreEntity storeEntity);
}
