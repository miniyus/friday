package com.miniyus.friday.infrastructure.persistence.repositories;

import com.miniyus.friday.infrastructure.persistence.entities.HostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HostRepository extends JpaRepository<HostEntity, Long>, QHostRepository {
    boolean existsByHostAndUserId(String host, Long userId);

    Optional<HostEntity> findByHostAndUserId(String host, Long userId);

    Page<HostEntity> findByPublishAndUserId(boolean publish, Long userId, Pageable pageable);
}
