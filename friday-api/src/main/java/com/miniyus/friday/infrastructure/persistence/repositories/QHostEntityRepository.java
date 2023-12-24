package com.miniyus.friday.infrastructure.persistence.repositories;

import com.miniyus.friday.hosts.domain.HostFilter;
import com.miniyus.friday.infrastructure.persistence.entities.HostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QHostEntityRepository {
    Page<HostEntity> findHosts(
        HostFilter hostFilter,
        Pageable pageable
    );

}
