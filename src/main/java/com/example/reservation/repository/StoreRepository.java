package com.example.reservation.repository;

import com.example.reservation.entity.PartnerEntity;
import com.example.reservation.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
    List<StoreEntity> findAllByPartnerEntity(PartnerEntity partnerEntity);
    List<StoreEntity> findAllByStoreNameLike(String storeName);
}
