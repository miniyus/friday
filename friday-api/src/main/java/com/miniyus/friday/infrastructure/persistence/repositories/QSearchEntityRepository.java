package com.miniyus.friday.infrastructure.persistence.repositories;

import com.miniyus.friday.hosts.domain.searches.SearchFilter;
import com.miniyus.friday.infrastructure.persistence.entities.SearchEntity;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface QSearchEntityRepository {
    Optional<SearchEntity> findLastSearchByHostId(Long hostId);

    Page<SearchEntity> findSearches(SearchFilter searchFilter);
}
