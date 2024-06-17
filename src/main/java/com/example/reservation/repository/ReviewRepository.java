package com.example.reservation.repository;

import com.example.reservation.entity.ReviewEntity;
import com.example.reservation.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findAllByUserEntity(UserEntity userEntity);
}
