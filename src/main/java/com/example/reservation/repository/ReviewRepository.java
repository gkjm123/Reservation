package com.example.reservation.repository;

import com.example.reservation.entity.ReviewEntity;
import com.example.reservation.entity.UserEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

  List<ReviewEntity> findAllByUserEntity(UserEntity userEntity);
}
