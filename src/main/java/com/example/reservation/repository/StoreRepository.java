package com.example.reservation.repository;

import com.example.reservation.entity.PartnerEntity;
import com.example.reservation.entity.StoreEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

  List<StoreEntity> findAllByPartnerEntity(PartnerEntity partnerEntity);

  List<StoreEntity> findAllByStoreNameLike(String storeName);
}
