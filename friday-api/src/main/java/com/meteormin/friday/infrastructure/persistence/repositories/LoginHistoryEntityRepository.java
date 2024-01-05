package com.meteormin.friday.infrastructure.persistence.repositories;

import com.meteormin.friday.infrastructure.persistence.entities.LoginHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginHistoryEntityRepository extends JpaRepository<LoginHistoryEntity, Long> {
    Optional<LoginHistoryEntity> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}
