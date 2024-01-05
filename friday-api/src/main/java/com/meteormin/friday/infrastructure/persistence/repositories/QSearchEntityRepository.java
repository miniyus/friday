package com.meteormin.friday.infrastructure.persistence.repositories;

import com.meteormin.friday.hosts.domain.searches.SearchFilter;
import com.meteormin.friday.infrastructure.persistence.entities.SearchEntity;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface QSearchEntityRepository {
    Optional<SearchEntity> findLastSearchByHostId(Long hostId);

    Page<SearchEntity> findSearches(SearchFilter searchFilter);
}
