package com.miniyus.friday.infrastructure.persistence.repositories;

import com.miniyus.friday.domain.hosts.HostFilter;
import com.miniyus.friday.domain.hosts.searches.SearchIds;
import com.miniyus.friday.infrastructure.persistence.entities.HostEntity;
import com.miniyus.friday.infrastructure.persistence.entities.SearchEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface QHostEntityRepository {
    Page<HostEntity> findHosts(
        HostFilter hostFilter,
        Pageable pageable
    );

}
