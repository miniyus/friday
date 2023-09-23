package com.miniyus.friday.infrastructure.persistence.repositories;

import com.miniyus.friday.domain.hosts.Host;
import com.miniyus.friday.infrastructure.persistence.entities.HostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QHostRepository {
    Page<HostEntity> findAll(
        Host.HostFilter hostFilter,
        Pageable pageable
    );
}
