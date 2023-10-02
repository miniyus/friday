package com.miniyus.friday.infrastructure.persistence.repositories;

import com.miniyus.friday.domain.hosts.searches.SearchFilter;
import com.miniyus.friday.domain.hosts.searches.SearchIds;
import com.miniyus.friday.infrastructure.persistence.entities.SearchEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface QSearchEntityRepository {
    Optional<SearchEntity> findLastSearchByHostId(Long hostId);

    Page<SearchEntity> findAllByHostId(Long hostId, Pageable pageable);

    Page<SearchEntity> findSearches(SearchFilter searchFilter, Pageable pageable);
}
