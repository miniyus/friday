package com.meteormin.friday.infrastructure.persistence.repositories;

import com.meteormin.friday.infrastructure.persistence.entities.HostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HostEntityRepository extends JpaRepository<HostEntity, Long>,
    QHostEntityRepository {
    boolean existsByHostAndUserId(String host, Long userId);

    Optional<HostEntity> findByHostAndUserId(String host, Long userId);

    Page<HostEntity> findByPublishAndUserId(boolean publish, Long userId, Pageable pageable);
}
