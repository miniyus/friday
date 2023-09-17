package com.miniyus.friday.infrastructure.persistence.repositories;

import com.miniyus.friday.api.hosts.domain.Host;
import com.miniyus.friday.infrastructure.persistence.entities.HostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface QHostRepository {
    Page<HostEntity> findAll(
        Host.HostFilter hostFilter,
        Pageable pageable
    );
}
