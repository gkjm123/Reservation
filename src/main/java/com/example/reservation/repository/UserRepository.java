package com.example.reservation.repository;

import com.example.reservation.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByLoginId(String loginId);

  Optional<UserEntity> findByNameAndPhone(String name, String phone);
}
