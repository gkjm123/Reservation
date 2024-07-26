package com.example.reservation.repository;

import com.example.reservation.entity.PartnerEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerRepository extends JpaRepository<PartnerEntity, Long> {

    Optional<PartnerEntity> findByLoginId(String loginId);
}
